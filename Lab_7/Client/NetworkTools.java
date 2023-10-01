package Client;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import Shared.command_processing.ResultDTO;
import Shared.other.Serializer;

public class NetworkTools {
    private static final int MAX_PACKET_SIZE = 65_507;// 65_507 an absolute maximum in IPv4
    private static final int ANSWER_STAND_BY_TIMEOUT = 10000;
    private static final int MIN_PORT = 1024, MAX_PORT = 65535;// 0-1024 service ports, 65535 max port number (2^16)

    private static int port = 4145;
    private static final String host = "localhost";
    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException ignored) {}
    }
    public static void sendOject(Object payload, int port){
        try {
            setPort(port);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        sendOject(payload);
    }
    public static boolean sendOject(Object payloadObject){
        if(payloadObject == null){
            System.out.println("Произошло что-то не хорошее, ошибка формирования объекта команды");
            return false;
        }
        if(!(payloadObject instanceof Serializable))
            throw new UnsupportedOperationException("Данный объект отправить нельзя");
        byte[] payload;
        try {
            payload = Serializer.serialize(payloadObject);
        } catch (IOException e) {
            System.out.println("Ошибка сериализации объекта");
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
    public static Optional<ResultDTO> receiveAnswer(){
        byte[] answerBytes = receivePacket(new byte[MAX_PACKET_SIZE], 0);
        if(answerBytes == null)
            return Optional.empty();
        int i = 0;
        while(answerBytes != null && new String(answerBytes).trim().length() % MAX_PACKET_SIZE == 0) {
            byte[] buff = answerBytes;
            answerBytes = new byte[answerBytes.length + MAX_PACKET_SIZE];
            System.arraycopy(buff, 0, answerBytes, 0, buff.length);
            answerBytes = receivePacket(answerBytes, buff.length);
        }
        try {
            return Optional.ofNullable((ResultDTO) Serializer.deserialize(answerBytes));
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            System.out.println("Ошибка десереализации ответа");
        } catch (ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Получен ответ неправильного типа");
        }
        return Optional.empty();
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
