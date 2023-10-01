package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;
import resources.RouteBuilder;

/**
 * Класс для добавления объекта в коллекцию
 */
public class Insert implements Command {
    private RouteBuilder route;
    private MyCollection collection;
    private String[] args;

    /**
     * Конструктор для создания объекта
     * @param route объект для добавления
     * @param collection коллекция для добавления
     * @param args аргументы с ключом для добавления
     */
    public Insert(RouteBuilder route, MyCollection collection, String[] args) {
        this.route = route;
        this.collection = collection;
        this.args = args;
    }

    /**
     * Метод для добавления объекта в коллекцию
     * @return строка с результатом добавления
     */
    @Override
    public String execute() {
        try {
            int key;
            if (args != null && args.length == 1 && !args[0].equals(""))
                key = Integer.parseInt(args[0]);
            else
                return "Ключ не указан";
            collection.put(key, route.getRoute());
            return "Объект "+route.getRoute()+" успешно добавлен";
        }catch (NumberFormatException ex){
            return "Ключ не указан";
        }

    }
}
