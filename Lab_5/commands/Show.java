package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;

/**
 * Класс Show реализует интерфейс Command
 */
public class Show implements Command {
    /**
     * Коллекция маршрутов
     */
    private final MyCollection collection;

    /**
     * Конструктор класса Show
     * @param collection коллекция маршрутов
     */
    public Show(MyCollection collection) {
        this.collection = collection;
    }

    /**
     * Метод выводит коллекцию маршрутов
     * @return коллекция маршрутов
     */
    @Override
    public String execute() {
        int i = 0;
        StringBuilder out = new StringBuilder("[");
        for(int key : collection.getMap().keySet())
            out.append(key).append(": ").append(collection.getMap().get(key));
        out.append("]");
        return out.toString();
    }
}
