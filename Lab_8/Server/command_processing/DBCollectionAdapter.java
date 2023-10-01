package Server.command_processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Shared.command_processing.AuthProvider;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.SynchronizationProvider;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

public class DBCollectionAdapter extends AbstractRouteCollection implements AuthProvider, SynchronizationProvider {
    private MyDBCollection collection;
    private String login, password;

    public DBCollectionAdapter(MyDBCollection collection, String login, String password){
        this.collection = collection;
        this.login = login;
        this.password = password;
    }

    @Override
    public HashMap<Integer, Route> getAll() {
        return collection.getAll();
    }

    @Override
    public boolean put(int key, Route route) {
        return collection.put(key, route, login, password);
    }

    @Override
    public boolean remove(int id) {
        return collection.remove(id, login, password);
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear(login, password);
    }

    @Override
    public Route get(int key) {
        return collection.get(key);
    }

    @Override
    public ResultDTO login(String login, String password) {
        return collection.checkUserAuth(login, password);
    }

    @Override
    public ResultDTO register(String login, String password) {
        return collection.registerUser(login, password);
    }

    @Override
    public Map<String, ArrayList<Integer>> getUsersObjects() {
        return collection.getUsersObjects();
    }

    @Override
    public void synchronizeRoute(int id, double x, double y, int mass, int radius) {
        collection.syncRoute(id, x, y, mass, radius);
    }
}
