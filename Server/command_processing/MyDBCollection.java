package Server.command_processing;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import Server.exceptions.UserUnauthorizedException;
import Server.network_modules.ConnectionManager;
import Server.network_modules.DBHandler;
import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandMessage;
import Shared.network.PacketType;
import Shared.resources.Route;

public class MyDBCollection {
    private final DBHandler db;
    private final Map<Integer, Route> collection;
    private final Map<String, ArrayList<Integer>> userToObject;
    private final ConnectionManager network;

    public MyDBCollection(String url, String DBuser, String DBpassword, ConnectionManager network) throws SQLException, NoSuchFieldException {
        db = new DBHandler(url, DBuser, DBpassword);
        collection = Collections.synchronizedMap(db.loadCollection());
        userToObject = Collections.synchronizedMap(
                Objects.requireNonNullElse(db.getUsersObjects(), new HashMap<String, ArrayList<Integer>>()));
        this.network = network;
    }


    /**
     * Возвращает копию коллекции
     *
     * @return all collection
     */
    public HashMap<Integer, Route> getAll() {
        synchronized (collection) {
            HashMap<Integer, Route> collectionClone = new HashMap<Integer, Route>();
            collectionClone.putAll(collection);
            return collectionClone;
        }
    }

    /**
     * Adds a route to the collection.
     *
     * @param key   The key to use for the route.
     * @param route The route to add to the collection.
     */
    public boolean put(int key, Route route, String userLogin, String password) {
        synchronized (collection) {
            if (!userToObject.containsKey(userLogin) || (collection.containsKey(key) && !userToObject.get(userLogin).contains(collection.get(key).getId())))
                throw new UserUnauthorizedException(userLogin);
            int insrtId = collection.containsKey(key) ? db.updateRoute(key, route) : db.addRoute(key, route, userLogin);
            if (insrtId > -1) {
                Class<Route> routeClass = Route.class;
                try {
                    Field idField = routeClass.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(route, insrtId);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    return false;
                }
                collection.put(key, route);
                userToObject.get(userLogin).add(insrtId);
                network.notifyAllClients(new ArrayDTO( true, "", new Object[]{key, route, userLogin}).setType(PacketType.InsertCollectionObject));
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a route from the collection.
     *
     * @param id The id of the route to remove.
     * @return True if the route was removed, false otherwise.
     */
    public boolean remove(int id, String login, String password) {
        if (!db.checkUserAuth(login, password))
            throw new UserUnauthorizedException(login);
        synchronized (collection) {
            if (userToObject.get(login).contains(id) && db.deleteRoute(id) > -1) {
                getAll().forEach((key, route) -> {
                    if (route.getId() == id) collection.remove(route.getId());
                });
                userToObject.get(login).remove((Integer) id);
                network.notifyAllClients(new ArrayDTO(true, "", new Object[]{id}).setType(PacketType.ClearCollection));
                return true;
            } else return false;
        }
    }

    /**
     * @return размер коллекции
     */

    public int size() {
        synchronized (collection) {
            return collection.size();
        }
    }

    /**
     * Очищает все объекты пользователя
     */
    public void clear(String login, String password) {
        synchronized (collection) {
            if (!db.checkUserAuth(login, password))
                throw new UserUnauthorizedException(login);
            int[] keys = db.clearByLogin(login);

            if (keys != null && keys.length > 0) {
                Integer[] ids = new Integer[keys.length];
                for (int i = 0; i < keys.length; i++) {
                    ids[i] = collection.get(keys[i]).getId();
                    collection.remove(keys[i]);
                }
                userToObject.put(login, new ArrayList<>());
                network.notifyAllClients(new ArrayDTO(true, "", ids).setType(PacketType.ClearCollection));
            }
        }
    }

    public Route get(int key) {
        return collection.get(key);
    }

    public ResultDTO registerUser(String login, String password) {
        synchronized (userToObject) {
            if (userToObject.containsKey(login))
                return new StringDTO(false, CommandMessage.UserAlreadyExists.getMessage());
            userToObject.put(login, new ArrayList<>());
            return new ResultDTO(db.registerUser(login, password));
        }
    }

    public ResultDTO checkUserAuth(String login, String password) {
        if (db.checkUserAuth(login, password))
            return new ResultDTO(true);
        if (!db.checkUserExists(login))
            return new StringDTO(false, CommandMessage.UserNotExists.getMessage());
        return new StringDTO(false, CommandMessage.IncorrectPassword.toString());
    }

    public Map<String, ArrayList<Integer>> getUsersObjects() {
        return new HashMap<>(userToObject);
    }

    public void syncRoute(int id, double x, double y, int mass, int radius) {
        HashMap<Integer, Route> collMap = getAll();
        for (int key : collMap.keySet()) {
            Route route = collMap.get(key);
            if (route.getId() == id) {
                route.getFrom().setX(x);
                route.getFrom().setZ(y);
                if (mass >= 0)
                    route.setDistance(mass == 0 ? null : mass);
                route.getCoordinates().setY(radius);
                int insertId = db.updateRoute(key, route);
                if (insertId > -1) {
                    String user = "";
                    Iterator<String> users = userToObject.keySet().iterator();
                    while (users.hasNext() && userToObject.get(user) != null && userToObject.get(user).contains(route.getId()))
                        user = users.next();
                    collection.put(key, route);
                    network.notifyAllClients(new ArrayDTO( true, "", new Object[]{key, route, user}).setType(PacketType.InsertCollectionObject));
                }
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "MyDBCollection{" +
                "db=" + db +
                ", collection=" + collection +
                '}';
    }
}

