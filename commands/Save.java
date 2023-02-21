package commands;

import java.util.HashMap;

import command_processing.YamlTools;
import commands.interfaces.Command;
import resources.Route;

/**
 * Класс Save реализует интерфейс Command.
 * Он используется для сохранения коллекции в файл.
 * @author Имя автора
 * @version 1.0
 */
public class Save implements Command {
    /** Коллекция, которую нужно сохранить */
    private HashMap<Integer, Route> collection;
    /** Аргументы команды */
    private String[] args = null;

    /**
     * Конструктор для создания объекта с заданной коллекцией.
     * @param collection Коллекция, которую нужно сохранить.
     */
    public Save(HashMap<Integer, Route> collection) {
        this.collection = collection;
    }

/**
 * Конструктор для создания объекта с заданной коллекцией и аргументами.
 * @param collection Коллекция, которую нужно сохранить.
 * @param args Аргументы команды
 */
    public Save(HashMap<Integer, Route> collection, String[] args) {
        this.collection = collection;
        this.args = args;
    }

    /**
     * Метод выполнения команды
     * @return сообщение о результате выполнения
     */
    @Override
    public String execute() {
        String filename = "data";
        if(args != null && args.length == 1 && !args[0].equals(""))
            filename = args[0];
        YamlTools.save(collection, filename);
        return "Коллекция успешно сохранена";
    }
}
