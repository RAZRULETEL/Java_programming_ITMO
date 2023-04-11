package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для очистки коллекции
 */
public class Clear implements Command, Serializable {
    /**
     * Конструктор для создания объекта
     */
    public Clear() {
    }

    /**
     * Метод для очистки коллекции
     * @return строка, сообщающая об успешном выполнении операции
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        collection.clear();
        return new StringDTO(true,  "Коллекция успешно очищена");
    }
}