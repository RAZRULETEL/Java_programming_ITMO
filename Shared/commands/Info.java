package Shared.commands;

import java.io.Serializable;
import java.util.Date;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для получения информации о коллекции
 */
public class Info implements Command, Serializable {
    /**
     * Конструктор для инициализации полей класса
     */
    public Info() {
    }

    /**
     * Метод для получения информации о коллекции
     * @return строка с информацией о коллекции
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new StringDTO(true,  "Коллекция типа " + collection.getAll().getClass().getName() + ", содержит " + collection.getAll().size() + " элементов, инициализирована " + new Date(collection.initTime));
    }
}