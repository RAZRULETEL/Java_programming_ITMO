package Client.network;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandMessage;
import Shared.network.PacketType;
import Shared.other.Serializer;

public class NetworkTools {
    private static final int MAX_PACKET_SIZE = 65_507;// 65_507 an absolute maximum in IPv4
    private static final int ANSWER_STAND_BY_TIMEOUT = 3000, PING_INTERVAL = 5000;
    private static final int MIN_PORT = 1024, MAX_PORT = 65535;// 0-1024 service ports, 65535 max port number (2^16)
    private static NetworkTools instance;
    private int port = 4145;
    private final String host;
    private final HashMap<PacketType, ArrayList<PacketListener>> listeners = new HashMap<>();
    private DatagramSocket socket;
    private final ArrayBlockingQueue<ResultDTO> packetsReceived = new ArrayBlockingQueue<>(100, true);
    private final Lock resultGetLock = new ReentrantLock(true);

    private NetworkTools(String host, int port) throws SocketException{
        socket = new DatagramSocket();
        this.host = host;
        this.port = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){
                    Optional<ResultDTO> answer = receiveAnswer();
                    if(answer.isPresent()){
                        if(listeners.get(answer.get().getType()) != null)
                            listeners.get(answer.get().getType()).forEach(l -> l.onPacketReceived(answer.get()));
                        try {
                            if(answer.get().getType() == PacketType.ExecutionResult)
                                packetsReceived.put(answer.get());
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }).start();
    }

    public void sendObject(Object payload, int port){
        try {
            setPort(port);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        sendObject(payload);
    }
    public boolean sendObject(Object payloadObject){
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
            synchronized (this) {
                if (payload.length > MAX_PACKET_SIZE) {
                    packetSampling(payload);
                } else
                    socket.send(request);
            }
        } catch (IOException e) {
            System.out.println("Возникла ошибка при отправке команды");
            return false;
        }
        return true;
    }
    private Optional<ResultDTO> receiveAnswer(){
        byte[] answerBytes = new byte[0];
        try{
            answerBytes = receivePacket(new byte[MAX_PACKET_SIZE], 0);
            if(answerBytes == null)
                return Optional.empty();
            synchronized (this) {
                while (answerBytes != null && new String(answerBytes).trim().length() % MAX_PACKET_SIZE == 0) {
                    byte[] buff = answerBytes;
                    answerBytes = new byte[answerBytes.length + MAX_PACKET_SIZE];
                    System.arraycopy(buff, 0, answerBytes, 0, buff.length);
                    answerBytes = receivePacket(answerBytes, buff.length);
                }
            }
        }catch (SocketTimeoutException e){
            System.out.println("Сервер не ответил в течении "+ANSWER_STAND_BY_TIMEOUT+" миллисекунд, похоже что он не доступен");
            return Optional.of(new StringDTO(false, "Сервер не ответил в течении "+ANSWER_STAND_BY_TIMEOUT+" миллисекунд, похоже что он не доступен"));
        }catch (IOException e){
            System.out.println("Возникла ошибка при получении ответа от сервера");
            return Optional.of(new StringDTO(false, "Возникла ошибка при получении ответа от сервера"));

        }
        try {
            return Optional.ofNullable((ResultDTO) Serializer.deserialize(answerBytes));
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            System.out.println("Ошибка десереализации ответа");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Получен ответ неправильного типа");
        }
        return Optional.empty();
    }
    private byte[] receivePacket(byte[] container, int offset) throws IOException {
        DatagramPacket answerPacket = new DatagramPacket(container, offset, MAX_PACKET_SIZE);
//        socket.setSoTimeout(ANSWER_STAND_BY_TIMEOUT);
        socket.receive(answerPacket);
        return answerPacket.getData();
    }
    public void setPort(int port) {
        if(port < MIN_PORT || port > MAX_PORT)
            throw new IllegalArgumentException("Port must be between "+MIN_PORT+" and "+MAX_PORT);
        this.port = port;
    }

    private void packetSampling(byte[] payload) {
      byte[][] packets = new byte[(payload.length - payload.length % MAX_PACKET_SIZE)/MAX_PACKET_SIZE+1][];
        try {
            for(int i = 0; i < packets.length; i++) {
                packets[i] = new byte[Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE)];
                System.arraycopy(payload, i * MAX_PACKET_SIZE, packets[i], 0, Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE));
                socket.send(new DatagramPacket(packets[i], Math.min(payload.length - MAX_PACKET_SIZE * i, MAX_PACKET_SIZE), InetAddress.getByName(host), port));
                sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Разделить команду не удалось, отправка прервана");
        }
    }
    public Optional<ResultDTO> receiveNextResultPacket()  {
        resultGetLock.lock();
        try {
            ResultDTO res = null;
            try {
                res = packetsReceived.poll(ANSWER_STAND_BY_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
            if (res == null)
                return Optional.of(new StringDTO(false, CommandMessage.ServerNotResponded.toString()));
            return Optional.of(res);
        }finally {
            resultGetLock.unlock();
        }
    }

    public void addPacketListener(PacketType requiredType, PacketListener listener){
        if(!listeners.containsKey(requiredType))
            listeners.put(requiredType, new ArrayList<>());
        listeners.get(requiredType).add(listener);
    }

    public void removePacketListener(PacketListener listener){
        for (List<PacketListener> listenerList : listeners.values()) {
            listenerList.remove(listener);
        }
    }

    public static NetworkTools getInstance(String host, int port) throws SocketException {
        if(instance == null){
            instance = new NetworkTools(host, port);
        }
        return instance;
    }

    public static NetworkTools getInstance(){
        return instance;
    }
}
