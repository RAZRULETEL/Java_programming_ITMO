package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для выхода из программы
 */
public class Exit implements Command, Serializable {
    private static final long serialVersionUID = -2873634715092908566L;

    /**
     * Конструктор для выхода из программы
     */
    public Exit() {}

    @Override
    public ResultDTO validate(String[] args) {
        System.exit(0);
        return null;
    }

    /**
     * Метод для выполнения команды
     * @return null
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        System.exit(0);
        return null;
    }
}