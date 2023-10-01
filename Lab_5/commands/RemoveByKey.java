package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;

/**
 * Класс для удаления элемента из коллекции по ключу.
 */
public class RemoveByKey implements Command {

    /** Коллекция, из которой будет удаляться элемент. */
    private MyCollection collection;

    /** Аргументы команды. */
    private final String[] args;

    /**
     * Конструктор для создания объекта класса RemoveByKey.
     * @param collection коллекция, из которой будет удаляться элемент.
     * @param args аргументы команды.
     */
    public RemoveByKey(MyCollection collection, String[] args) {
        this.collection = collection;
        this.args = args;
    }

    /**
     * Удаляет элемент из коллекции по ключу.
     * @return сообщение о результате выполнения команды.
     */
    @Override
    public String execute() {
        int key = -1;
        try {
            if (args != null && args.length == 1 && !args[0].equals(""))
                key = Integer.parseInt(args[0]);
            if (collection.remove(key))
                return "Элемент успешно удалён";
            else
                return "Элемента с таким ключом не существует";
        }catch(NumberFormatException ex){
            return "Ключ должен быть числом";
        }
    }
}
