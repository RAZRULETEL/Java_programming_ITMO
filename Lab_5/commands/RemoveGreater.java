package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;
import resources.RouteBuilder;

/**
 * Класс для удаления из коллекции всех элементов, превышающих заданный
 */
public class RemoveGreater implements Command {
    private MyCollection collection;
    private RouteBuilder route;

    /**
     * Конструктор для создания объекта с заданными параметрами
     *
     * @param route элемент для сравнения
     * @param collection коллекция
     */
    public RemoveGreater(RouteBuilder route, MyCollection collection) {
        this.collection = collection;
        this.route = route;
    }

    /**
     * Метод для удаления из коллекции всех элементов, превышающих заданный
     *
     * @return строка с количеством удаленных элементов
     */
    @Override
    public String execute() {
        int count = 0;
        for(int key : collection.getMap().keySet())
            if(collection.getMap().get(key).compareTo(route.getRoute()) > 0) {
                collection.remove(key);
                count++;
            }
        return "Удалено "+count+" элементов";
    }
}
