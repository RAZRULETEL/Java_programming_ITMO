package commands;

import java.util.ArrayList;
import java.util.HashMap;

import commands.interfaces.Command;
import resources.Route;

/**
 * Класс FilterByName реализует интерфейс Command.
 * Он используется для фильтрации коллекции по имени.
 * @author Имя автора
 * @version 1.0
 */
public class FilterByName implements Command {
    private HashMap<Integer, Route> collection;
    private final String[] args;
    private int pos;

    /**
     * Конструктор для создания объекта FilterByName.
     * @param collection коллекция для фильтрации
     * @param args аргументы для фильтрации
     * @param sub_pos позиция для фильтрации
     */
    public FilterByName(HashMap<Integer, Route> collection, String[] args, int sub_pos) {
        this.collection = collection;
        this.args = args;
    }

    /**
     * Выполняет фильтрацию по подстроке в имени.
     * @return отсортированная по имени коллекция.
     */
    @Override
    public String execute() {
        String subString;
        if (args != null && args.length == 1 && !args[0].equals(""))
            subString = (args[0]);
        else
            return "Подстрока указана неверно";
        ArrayList<Route> routes = new ArrayList<Route>();
        for(int key : collection.keySet())
            switch (pos){
                case -1:
                    if(collection.get(key).getName().startsWith(subString))
                        routes.add(collection.get(key));
                    break;
                default: case 0:
                    if(collection.get(key).getName().contains(subString))
                        routes.add(collection.get(key));
                    break;
                case 1:
                    if(collection.get(key).getName().endsWith(subString))
                        routes.add(collection.get(key));
                    break;

            }
        return String.valueOf(routes);
    }
}
