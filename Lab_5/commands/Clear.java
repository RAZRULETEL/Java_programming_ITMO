package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;

/**
 * Класс для очистки коллекции
 */
public class Clear implements Command {

    /** Коллекция, которую нужно очистить */
    private MyCollection collection;

    /**
     * Конструктор для создания объекта
     * @param collection коллекция, которую нужно очистить
     */
    public Clear(MyCollection collection) {
        this.collection = collection;
    }

    /**
     * Метод для очистки коллекции
     * @return строка, сообщающая об успешном выполнении операции
     */
    @Override
    public String execute() {
        for(int key : collection.getMap().keySet())
            collection.remove(key);
        return "Коллекция успешно очищена";
    }
}