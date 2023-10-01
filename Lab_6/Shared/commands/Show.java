package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс Show реализует интерфейс Command
 */
public class Show implements Command, Serializable {
    /**
     * Конструктор класса Show
     */
    public Show() {
    }

    /**
     * Метод выводит коллекцию маршрутов
     * @return коллекция маршрутов
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new ArrayDTO(true, "", new Object[]{collection.getAll()});
    }
}
