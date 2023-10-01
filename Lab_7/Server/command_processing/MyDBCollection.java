package Server.command_processing;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Server.exceptions.UserUnauthorizedException;
import Server.network_modules.DBHandler;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.resources.Route;

public class MyDBCollection {
    private DBHandler db;
    private final Map<Integer, Route> collection;
    private final Map<String, ArrayList<Integer>> userToObject;

    public MyDBCollection(String url, String DBuser, String DBpassword) throws SQLException, NoSuchFieldException {
        db = new DBHandler(url, DBuser, DBpassword);
        collection = Collections.synchronizedMap(db.loadCollection());
        userToObject = Collections.synchronizedMap(
                Objects.requireNonNullElse(db.getUsersObjects(), new HashMap<String, ArrayList<Integer>>()));
    }

    public MyDBCollection(DBHandler dbHandler) throws SQLException, NoSuchFieldException {
        db = dbHandler;
        collection = Collections.synchronizedMap(db.loadCollection());
        userToObject = Collections.synchronizedMap(
                Objects.requireNonNullElse(db.getUsersObjects(), new HashMap<String, ArrayList<Integer>>()));
    }

    /**
     * Возвращает копию коллекции
     * @return all collection
     */
    public HashMap<Integer, Route> getAll() {
        synchronized(collection) {
            HashMap<Integer, Route> collectionClone = new HashMap<Integer, Route>();
            collectionClone.putAll(collection);
            return collectionClone;
        }
    }

    /**
     * Adds a route to the collection.
     *
     * @param key The key to use for the route.
     * @param route The route to add to the collection.
     */
    public boolean put(int key, Route route, String userLogin, String password) {
        synchronized(collection) {
            if(!userToObject.containsKey(userLogin) || (collection.containsKey(key) && !userToObject.get(userLogin).contains(collection.get(key).getId())))
                throw new UserUnauthorizedException(userLogin);
            int insrtId = collection.containsKey(key) ? db.updateRoute(key, route) : db.addRoute(key, route, userLogin);
            if(insrtId > -1) {
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
            if(!db.checkUserAuth(login, password))
                throw new UserUnauthorizedException(login);
        synchronized(collection) {
            if(db.deleteRoute(id) > -1) {
                getAll().forEach((key, route) -> {if(route.getId() == id)collection.remove(key);});
                userToObject.get(login).remove((Integer) id);
                return true;
            }
            else return false;
        }
    }
    /**
     * @return размер коллекции
     */

    public int size() {
        synchronized(collection) {
            return collection.size();
        }
    }
    /**
     * Очищает все объекты пользователя
     */
    public void clear(String login, String password) {
        synchronized(collection) {
            if(!db.checkUserAuth(login, password))
                throw new UserUnauthorizedException(login);
            int[] keys = db.clearByLogin(login);
            if(keys != null && keys.length > 0) {
                for (int key : keys)
                    collection.remove(key);
                userToObject.put(login, new ArrayList<>());
            }
        }
    }

    public Route get(int key) {
        return collection.get(key);
    }

    public ResultDTO registerUser(String login, String password){
        synchronized(userToObject) {
            System.out.println(userToObject);
            if(userToObject.containsKey(login))
                return new StringDTO(false, "Пользователь с таким логином уже есть");
            userToObject.put(login, new ArrayList<>());
            return new ResultDTO(db.registerUser(login, password));
        }
    }
    public ResultDTO checkUserAuth(String login, String password){
        if(db.checkUserAuth(login, password))
            return new ResultDTO(true);
        if(!db.checkUserExists(login))
            return new StringDTO(false, "Данного пользователя не существует");
        return new StringDTO(false, "Введён неправильный пароль");
    }

    @Override
    public String toString() {
        return "MyDBCollection{" +
                "db=" + db +
                ", collection=" + collection +
                '}';
    }
}

