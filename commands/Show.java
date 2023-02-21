package commands;

import java.util.Arrays;
import java.util.HashMap;

import commands.interfaces.Command;
import resources.Route;

/**
 * Класс Show реализует интерфейс Command
 */
public class Show implements Command {
    /**
     * Коллекция маршрутов
     */
    private final Route[] collection;

    /**
     * Конструктор класса Show
     * @param collection коллекция маршрутов
     */
    public Show(HashMap<Integer, Route> collection) {
        this.collection = new Route[collection.size()];
        int i = 0;
        for(int key : collection.keySet())
            this.collection[i++] = collection.get(key);
    }

    /**
     * Метод выводит коллекцию маршрутов
     * @return коллекция маршрутов
     */
    @Override
    public String execute() {
        return Arrays.toString(collection);
    }
}
