package Server.thread_tasks;

import java.net.SocketAddress;

import Server.network_modules.ConnectionManager;
import Shared.command_processing.ResultDTO;

public class ResponseTask implements Runnable {
    private ResultDTO response;
    private SocketAddress address;
    private ConnectionManager server;

    ResponseTask(ResultDTO response, SocketAddress address, ConnectionManager server) {
        if(server == null || server.isClosed())
            throw new IllegalArgumentException("ConnectionManager cannot be null or closed");
        this.server = server;
        if(response == null)
            throw new IllegalArgumentException("Response cannot be null or closed");
        this.response = response;
        if(address == null)
            throw new IllegalArgumentException("Address cannot be null");
        this.address = address;
    }

    @Override
    public void run() {
        server.answerClient(address, response);
    }
}
