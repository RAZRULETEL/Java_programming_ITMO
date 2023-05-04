package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс Show реализует интерфейс Command
 */
public class Show implements Command, Serializable {
    private static final long serialVersionUID = 4279609460359602111L;

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
