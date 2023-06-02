package Server.network_modules;

import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestsManager {
    private final ConcurrentMap<SocketAddress, ArrayDeque<byte[]>> requests = new ConcurrentHashMap<>();

    synchronized public void addRequest(SocketAddress address, byte[] request) {
        requests.computeIfAbsent(address, k -> new ArrayDeque<>()).add(request);
    }

    synchronized public byte[] getUnprocessedRequest(SocketAddress address) {
        if (address == null)
            throw new IllegalArgumentException("Address cannot be null");
        ArrayDeque<byte[]> requestsList = requests.get(address);
        if (requestsList == null || requestsList.isEmpty())
            return null;
        return requestsList.poll();

    }
}