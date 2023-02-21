package command_processing;

import java.util.HashMap;

import resources.Route;

/**
 * Класс MyCollection используется для хранения и управления коллекцией маршрутов.
 */
public class MyCollection {
    /**
     * The collection of routes.
     */
    private HashMap<Integer, Route> collection = new HashMap<Integer, Route>();
    /**
     * The time when the collection was initialized.
     */
    public final Long initTime;

    {
        initTime = System.currentTimeMillis();
    }
    /**
     * Constructor for the MyCollection class.
     * Initializes the collection with the given HashMap.
     *
     * @param collection The HashMap to initialize the collection with.
     */
    public MyCollection(HashMap<Integer, Route> collection) {
        this.collection = collection;
    }
    /**
     * Constructor for the MyCollection class.
     * Initializes the collection with the given file.
     *
     * @param file The file to initialize the collection with.
     */
    public MyCollection(String file) {
        Route[] collectionArray = YamlTools.load(file);
        if(collectionArray != null)
            for(Route route : collectionArray)
                collection.put(route.getId(), route);
    }

    /**
     * Constructor for the MyCollection class.
     * Initializes the collection with an empty HashMap.
     */
    public MyCollection() {}

    /**
     * Returns the collection as a HashMap.
     *
     * @return The collection as a HashMap.
     */
    public HashMap<Integer, Route> getMap() {
        return collection;
    }


    /**
     * Adds a route to the collection.
     *
     * @param key The key to use for the route.
     * @param route The route to add to the collection.
     */
    public void put(int key, Route route) {
        collection.put(key, route);
    }
    /**
     * Removes a route from the collection.
     *
     * @param id The id of the route to remove.
     * @return True if the route was removed, false otherwise.
     */
    public boolean remove(int id) {
        if(!collection.containsKey(id))
            return false;
        collection.remove(id);
        return true;
    }
}
