package Server.network_modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import Server.Main;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.other.Serializer;

public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private static final int MAX_PACKET_SIZE = 65_507;// 65_507 an absolute maximum in IPv4
    private static final int MIN_PORT = 1024, MAX_PORT = 65535;// 0-1024 service ports, 65535 max port number (2^16)

    private DatagramChannel channel;
    private int port;
    private ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
    private final HashMap<SocketAddress, byte[]> requests = new HashMap<>();

    public ConnectionManager(int port){
        this.port = port;
        try {
            connect(port);
            LOGGER.info("Server succesfully started on port {}", port);
        }catch (BindException e){
            LOGGER.error("Failed to start server, it looks like the port {} is already busy", port);
        }catch (IOException e) {
            LOGGER.error("Unexpected error opening channel: {}", e.getMessage());
        }
    }
    public void reconnect() throws IOException {
        connect(port);
    }
    private void connect(int port) throws IOException {
        if(port < MIN_PORT || port > MAX_PORT)
            throw new IllegalArgumentException("Port must be between 1024 and 65535");
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
    }

    /**
     * Метод для проверки наличия поступления данных, при наличии сохраняет их в HashMap с ключом - адресом постпления
     * @return возвращает адрес откуда пришли данные, либо null если никакие данные не были получены
     */
    public SocketAddress receiveNextPacket(){
        SocketAddress clientAdress = null;
        try {
            clientAdress = channel.receive(buffer);
            if(buffer.position() != 0){
                System.out.println("Received "+buffer.position()+" bytes");

                byte[] request = receiveData();
                requests.put(clientAdress, request);

                LOGGER.info("Received {} bytes packet from {}", request.length, clientAdress);

                return clientAdress;
            }
            return null;
        } catch (IOException e) {
            LOGGER.error("Error receiving packet from {}", clientAdress);
        }
        return null;
    }

    /**
     * Метод для получения байтов запроса
     * @param address адресс, с которого запрос был получен
     * @return последний запрос, полученный от даного адресса
     */
    public byte[] getRequestBytes(SocketAddress address){
        return requests.get(address);
    }

    /**
     * Метод для отправки ответа клиенту
     * @param address адрес получателя
     * @param message данные для отправки
     */
    public void answerClient(SocketAddress address, ResultDTO message){
        requests.remove(address);
        byte[] payload;
        try {
            payload = Serializer.serialize(message);
        } catch (IOException e) {
            LOGGER.error("Error serializing ResultDTO message");
            return;
        }
        try {
            if(payload.length > MAX_PACKET_SIZE)
                packetSampling(address, payload);
            else
                channel.send(ByteBuffer.wrap(payload), address);
            LOGGER.info("Sended {} bytes packet to {}", payload.length, address);
        }catch (IOException e){
            LOGGER.error("Error sending {} bytes packet to {}: {}", payload.length, address, e.getMessage());
        }
    }

    /**
     * Метод для отправки данных несколькими пакетами, если они не помещаются в один пакет
     * @param address адрес получателя
     * @param message данные для отправки
     */
    private void packetSampling(SocketAddress address, byte[] message) throws IOException {
        byte[][] packets = new byte[(message.length - message.length % MAX_PACKET_SIZE)/MAX_PACKET_SIZE+1][];
        for(int i = 0; i < packets.length; i++) {
            packets[i] = new byte[Math.min(message.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE)];
            System.arraycopy(message, i * MAX_PACKET_SIZE, packets[i], 0, Math.min(message.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE));
            channel.send(ByteBuffer.wrap(packets[i]), address);
        }
    }

    /**
     * Увеличивает буффер до определённого размера
     * @param size размер, до которого нужно увеличить буффер
     * @deprecated
     */
    private void increaseBufferSize(int size){
        if(buffer.array().length < size) {
            buffer = ByteBuffer.allocate(size);
            System.out.println("Buffer increased to "+size);
        }
    }

    /**
     * Получает один набор данных, получение пакета меньше максимального размера означает конец набора
     * @return массив содержащих только полученные данные (бех нулей)
     */
    private byte[] receiveData() throws IOException {
        byte[] tempBuffer = new byte[MAX_PACKET_SIZE];
        while (buffer.position() == 0)
            channel.receive(buffer);
        if(buffer.position() < MAX_PACKET_SIZE){
            tempBuffer = new byte[buffer.position()];
            System.arraycopy(buffer.array(), 0, tempBuffer, 0, buffer.position());
            buffer.clear();
            return tempBuffer;
        }
        int i = 0;
        while(buffer.position() >= MAX_PACKET_SIZE) {
            int startPos = i++ * MAX_PACKET_SIZE;
            tempBuffer = increaseByteArray(tempBuffer, i * MAX_PACKET_SIZE);
            System.arraycopy(buffer.array(), 0, tempBuffer, startPos, MAX_PACKET_SIZE);
            buffer.clear();
            while (buffer.position() == 0)
                channel.receive(buffer);
        }
        if(tempBuffer.length > MAX_PACKET_SIZE) {
            tempBuffer = increaseByteArray(tempBuffer, tempBuffer.length + buffer.position());
            System.arraycopy(buffer.array(), 0, tempBuffer, i * MAX_PACKET_SIZE, buffer.position());
            buffer = ByteBuffer.wrap(tempBuffer);
            buffer.position(tempBuffer.length);
        }
        return tempBuffer;
    }
    public static byte[] increaseByteArray(byte[] array, int newLength){
        if(newLength < array.length)
            throw new IllegalArgumentException("Новая длина должна быть больше текущей");
        byte[] buff = new byte[newLength];
        System.arraycopy(array, 0, buff, 0, newLength);
        return buff;
    }

}
