package Server.network_modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import Shared.command_processing.ResultDTO;
import Shared.other.Serializer;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final int MAX_PACKET_SIZE = 65_507;// 65_507 an absolute maximum in IPv4
    private static final int MIN_PORT = 1024, MAX_PORT = 65535;// 0-1024 service ports, 65535 max port number (2^16)

    private DatagramChannel channel;
    private int port;
    private RequestsManager requests = new RequestsManager();


    public ConnectionManager(int port){
        this.port = port;
        try {
            connect(port);
            logger.info("Server succesfully started on port {}", port);
        }catch (BindException e){
            logger.error("Failed to start server, it looks like the port {} is already busy", port);
        }catch (IOException e) {
            logger.error("Unexpected error opening channel: {}", e.getMessage());
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
            ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
            clientAdress = channel.receive(buffer);
            if(buffer.position() != 0){
                byte[] request = new byte[buffer.position()];
                System.arraycopy(buffer.array(), 0, request, 0, buffer.position());
                requests.addRequest(clientAdress, request);

                logger.info("Received {} bytes packet from {}", request.length, clientAdress);

                return clientAdress;
            }
            return null;
        } catch (IOException e) {
            logger.error("Error receiving packet from {}", clientAdress);
        }
        return null;
    }

    /**
     * Метод для получения байтов запроса
     * @param address адресс, с которого запрос был получен
     * @return последний запрос, полученный от даного адресса
     */
    public byte[] getRequestBytes(SocketAddress address){
        return requests.getUnprocessedRequest(address);
    }

    /**
     * Метод для отправки ответа клиенту
     * @param address адрес получателя
     * @param message данные для отправки
     */
    public boolean answerClient(SocketAddress address, ResultDTO message){
        byte[] payload;
        try {
            payload = Serializer.serialize(message);
        } catch (IOException e) {
            logger.error("Error serializing ResultDTO message");
            return false;
        }
        try {
            if(payload.length > MAX_PACKET_SIZE)
                packetSampling(address, payload);
            else
                channel.send(ByteBuffer.wrap(payload), address);
            logger.info("Sended {} bytes packet to {}", payload.length, address);
            return true;
        }catch (IOException e){
            logger.error("Error sending {} bytes packet to {}: {}", payload.length, address, e.getMessage());
            return false;
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
            try {
                Thread.sleep(30);//Client loses packets if sends them so fast
            } catch (InterruptedException ignored) {}
            channel.send(ByteBuffer.wrap(packets[i]), address);
        }

    }
    public static byte[] increaseByteArray(byte[] array, int newLength){
        if(newLength < array.length)
            throw new IllegalArgumentException("Новая длина должна быть больше текущей");
        byte[] buff = new byte[newLength];
        System.arraycopy(array, 0, buff, 0, newLength);
        return buff;
    }

    public boolean isClosed() {
        return !channel.isOpen();
    }
}
