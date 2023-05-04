package Shared.resources;

import java.util.HashMap;

public abstract class AbstractRouteCollection {
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
    public AbstractRouteCollection(HashMap<Integer, Route> collection) {
        if(collection != null)
            this.collection = collection;
    }
    /**
     * Constructor for the MyCollection class.
     * Initializes the collection with an empty HashMap.
     */
    public AbstractRouteCollection() {}

    /**
     * Returns the collection as a HashMap.
     *
     * @return The collection as a HashMap.
     */
    public abstract HashMap<Integer, Route> getAll();


    /**
     * Adds a route to the collection.
     *  @param key The key to use for the route.
     * @param route The route to add to the collection.
     * @return
     */
    public abstract boolean put(int key, Route route);
    /**
     * Removes a route from the collection.
     *
     * @param id The id of the route to remove.
     * @return True if the route was removed, false otherwise.
     */
    public abstract boolean remove(int id);
    /**
     * @return size of collection
     */
    public abstract int size();

    /**
     * Clears collection
     */
    public abstract void clear();
    /**
     * @param key of object to return
     * @return object by key
     */
    public abstract Route get(int key);
}
