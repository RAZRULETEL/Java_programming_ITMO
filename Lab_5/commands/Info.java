package commands;

import java.util.Date;
import java.util.HashMap;

import command_processing.MyCollection;
import commands.interfaces.Command;
import resources.Route;

/**
 * Класс для получения информации о коллекции
 */
public class Info implements Command {
    /**
     * Временная метка инициализации коллекции
     */
    private final Long timestamp;
    /**
     * Коллекция
     */
    private HashMap<Integer, Route> collection;

    /**
     * Конструктор для инициализации полей класса
     * @param collection коллекция
     */
    public Info(MyCollection collection) {
        this.timestamp = collection.initTime;
        this.collection = collection.getMap();
    }

    /**
     * Метод для получения информации о коллекции
     * @return строка с информацией о коллекции
     */
    @Override
    public String execute() {
        return "Коллекция типа " + collection.getClass().getName() + ", содержит " + collection.size() + ", инициализирована " + new Date(timestamp);
    }
}