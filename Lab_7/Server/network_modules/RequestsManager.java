package Server.network_modules;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestsManager {
    private final ConcurrentMap<SocketAddress, ArrayList<byte[]>> requests = new ConcurrentHashMap<>();

    synchronized public void addRequest(SocketAddress address, byte[] request){
        requests.computeIfAbsent(address, k -> new ArrayList<byte[]>()).add(request);
    }

    synchronized public byte[] getUnprocessedRequest(SocketAddress address){
        if(address == null)
            throw new IllegalArgumentException("Address cannot be null");
        ArrayList<byte[]> requestsList = requests.get(address);
        if(requestsList == null || requestsList.isEmpty())
            return null;
        return requestsList.remove(0);
    }
}
