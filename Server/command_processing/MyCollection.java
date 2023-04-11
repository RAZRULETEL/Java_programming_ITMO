package Server.command_processing;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс MyCollection используется для хранения и управления коллекцией маршрутов.
 */
public class MyCollection extends AbstractRouteCollection {

    private HashMap<Integer, Route> collection = new HashMap<Integer, Route>();

    public MyCollection(HashMap<Integer, Route> collection) {
        if(collection != null)
            this.collection = collection;
    }

    public MyCollection() {}

    /**
     * Возвращает копию коллекции
     * @return all collection
     */
    @Override
    public HashMap<Integer, Route> getAll() {
        HashMap<Integer, Route> collectionClone = new HashMap<Integer, Route>();
        collectionClone.putAll(collection);
        return collectionClone;
    }

    /**
     * Adds a route to the collection.
     *
     * @param key The key to use for the route.
     * @param route The route to add to the collection.
     */
    @Override
    public void put(int key, Route route) {
        collection.put(key, route);
    }
    /**
     * Removes a route from the collection.
     *
     * @param id The id of the route to remove.
     * @return True if the route was removed, false otherwise.
     */
    @Override
    public boolean remove(int id) {
        return collection.remove(id) != null;
    }
    /**
     * @return размер коллекции
     */
    @Override
    public int size() {
        return collection.size();
    }
    /**
     * Очищает коллекцию
     */
    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public Route get(int key) {
        return collection.get(key);
    }
}
