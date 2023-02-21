package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;

/**
 * Класс для удаления элементов из коллекции по заданному ключу
 */
public class RemoveGreaterKey implements Command {

    /** Коллекция, из которой будут удаляться элементы */
    private MyCollection collection;
    /** Аргументы команды */
    private String[] args;

    /**
     * Конструктор для создания объекта с заданными параметрами
     * @param collection коллекция, из которой будут удаляться элементы
     * @param args аргументы команды
     */
    public RemoveGreaterKey(MyCollection collection, String[] args) {
        this.collection = collection;
        this.args = args;
    }

/**
 * Метод для удаления элементов из коллекции с ключом превышающим заданный ключ
 * @return строка с информацией о количестве удалённых элементов
 */
    @Override
    public String execute() {
        int id = 0;
        try {
            if (args != null && args.length == 1 && !args[0].equals(""))
                id = Integer.parseInt(args[0]);
        }catch(NumberFormatException ex){
            return "id должен быть числом";
        }
        int count = 0;
        for(int key : collection.getMap().keySet())
            if(key > id) {
                collection.remove(key);
                count++;
            }
        return "Удалено "+count+" элементов";
    }
}
