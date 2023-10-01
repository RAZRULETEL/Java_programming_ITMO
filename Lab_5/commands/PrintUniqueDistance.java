package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import commands.interfaces.Command;
import resources.Route;

/**
 * Класс для печати уникальных расстояний
 */
public class PrintUniqueDistance implements Command {
    private HashMap<Integer, Route> collection;

    /**
     * Конструктор для создания объекта
     * @param collection коллекция
     */
    public PrintUniqueDistance(HashMap<Integer, Route> collection) {
        this.collection = collection;
    }

    /**
     * Метод для печати уникальных расстояний маршрутов
     * @return строка с уникальными расстояниями
     */
    @Override
    public String execute() {
        ArrayList<Integer> distances = new ArrayList<>();
        for(int key : collection.keySet())
            if(distances.stream().noneMatch(e -> Objects.equals(e, collection.get(key).getDistance())))
                distances.add(collection.get(key).getDistance());
        return String.valueOf(distances);
    }
}