package Server.thread_tasks;

import java.net.SocketAddress;
import java.util.concurrent.ForkJoinPool;

import Server.network_modules.ConnectionManager;

public class ReceiverTask implements Runnable {
    private static int threadsCount = 0;

    private ConnectionManager server = null;
    private ForkJoinPool commandExecutor = null;
    private CommandExecutorTask requestExecutor;

    public ReceiverTask(ConnectionManager server, ForkJoinPool commandExecutor, CommandExecutorTask dataProcessor) {
        if(server == null)
            throw new IllegalArgumentException("ConnectionManager cannot be null or closed");
        this.server = server;
        if(commandExecutor == null)
            throw new IllegalArgumentException("CommandExecutor cannot be null");
        this.commandExecutor = commandExecutor;
        if(dataProcessor == null)
            throw new IllegalArgumentException("DataProcessor cannot be null");
        this.requestExecutor = dataProcessor;
    }

    @Override
    public void run() {
        threadsCount++;
        SocketAddress clientAddress = server.receiveNextPacket();
        while (clientAddress == null && !server.isClosed())
            clientAddress = server.receiveNextPacket();
        if(server.isClosed()) {
            threadsCount--;
            throw new IllegalStateException();
        }
        byte[] request = server.getRequestBytes(clientAddress);
        threadsCount--;
        commandExecutor.execute(requestExecutor.setData(request).setNetworkManagerAndResponseAddress(server, clientAddress));
    }

    public static int getThreadsCount(){return threadsCount;}
}
