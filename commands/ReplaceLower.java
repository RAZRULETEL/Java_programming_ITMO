package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;
import resources.RouteBuilder;

/**
 * Класс для замены элемента в коллекции по ключу, если новый элемент меньше старого
 */
public class ReplaceLower implements Command {

    /** Коллекция, в которой производится замена */
    private MyCollection collection;

    /** Новый элемент */
    private RouteBuilder route;

    /** Аргументы команды */
    private String[] args;

    /**
     * Конструктор для создания объекта
     * @param route новый элемент
     * @param collection коллекция, в которой производится замена
     * @param args аргументы команды
     */
    public ReplaceLower(RouteBuilder route, MyCollection collection, String[] args) {
        this.collection = collection;
        this.route = route;
        this.args = args;
    }

/**
 * Заменяет элемент в коллекции по ключу, если новый элемент меньше заданного
 * @return сообщение о результате выполнения команды
 */
    @Override
    public String execute() {
        int key = -1;
        try {
            if (args != null && args.length == 1 && !args[0].equals(""))
                key = Integer.parseInt(args[0]);
            if (collection.getMap().get(key).compareTo(route.getRoute()) < 0) {
                collection.put(key, route.getRoute());
                return "Элемент успешно заменён";
            }else
                return "Элемент не был заменён";
        }catch(NumberFormatException ex){
            return "Ключ должен быть числом";
        }
    }
}