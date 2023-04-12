package Client;

import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.LocalCommand;
import Shared.other.Serializer;

public class NetworkTools {
    private static final int MAX_PACKET_SIZE = 65_507;// 65_507 an absolute maximum in IPv4
    private static final int ANSWER_STAND_BY_TIMEOUT = 3000;
    private static final int MIN_PORT = 1024, MAX_PORT = 65535;// 0-1024 service ports, 65535 max port number (2^16)

    private static int port = 4145;
    private static final String host = "localhost";
    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException ignored) {}
    }
    public static void sendCommand(Command command, int port){
        try {
            setPort(port);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        sendCommand(command);
    }
    public static boolean sendCommand(Command command){
        if(command == null){
            System.out.println("Произошло что-то не хорошее, ошибка формирования объекта команды");
            return false;
        }
        if(!(command instanceof Serializable))
            throw new UnsupportedOperationException("Данную команду отправить нельзя");
        byte[] payload;
        try {
            payload = Serializer.serialize(command);
        } catch (IOException e) {
            System.out.println("Ошибка сериализации команды");
            return false;
        }
        try {
            if(socket == null || socket.isClosed())
                socket = new DatagramSocket();
            DatagramPacket request = new DatagramPacket(payload, payload.length, InetAddress.getByName(host), port);
            if(payload.length > MAX_PACKET_SIZE) {
                packetSampling(payload);
            }else
                socket.send(request);
        } catch (IOException e) {
            System.out.println("Возникла ошибка при отправке команды");
            return false;
        }
        return true;
    }
    public static ResultDTO receiveAnswer(){
        byte[] answerBytes = receivePacket(new byte[MAX_PACKET_SIZE], 0);
        if(answerBytes == null)
            return null;
        while(new String(answerBytes).trim().length() % MAX_PACKET_SIZE == 0) {
            byte[] buff = answerBytes;
            answerBytes = new byte[answerBytes.length + MAX_PACKET_SIZE];
            System.arraycopy(buff, 0, answerBytes, 0, buff.length);
            answerBytes = receivePacket(answerBytes, buff.length);
        }
        try {
            return (ResultDTO) Serializer.deserialize(answerBytes);
        } catch (IOException e) {
            System.out.println("Ошибка десереализации ответа");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Получен ответ неправильного типа");
        }
        return null;
    }
    private static byte[] receivePacket(byte[] container, int offset){
        try {
            DatagramPacket answerPacket = new DatagramPacket(container, offset, MAX_PACKET_SIZE);
            socket.setSoTimeout(ANSWER_STAND_BY_TIMEOUT);
            socket.receive(answerPacket);
            return answerPacket.getData();
        }catch (SocketTimeoutException e){
            System.out.println("Сервер не ответил в течении "+ANSWER_STAND_BY_TIMEOUT+" миллисекунд, похоже что он не доступен");
        }catch (IOException e){
            System.out.println("Возникла ошибка при получении ответа от сервера");
        }
        return null;
    }
    public static void setPort(int port) {
        if(port < MIN_PORT || port > MAX_PORT)
            throw new IllegalArgumentException("Port must be between "+MIN_PORT+" and "+MAX_PORT);
        NetworkTools.port = port;
    }
    private static void packetSampling(byte[] payload) {
      byte[][] packets = new byte[(payload.length - payload.length % MAX_PACKET_SIZE)/MAX_PACKET_SIZE+1][];
        try {
            for(int i = 0; i < packets.length; i++) {
                packets[i] = new byte[Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE)];
                System.arraycopy(payload, i * MAX_PACKET_SIZE, packets[i], 0, Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE));
                socket.send(new DatagramPacket(packets[i], Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE), InetAddress.getByName(host), port));
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Разделить команду не удалось, отправка прервана");
        }
    }
}
