package commands;

import commands.interfaces.Command;

/**
 * Класс для выхода из программы
 */
public class Exit implements Command {
    /**
     * Конструктор для выхода из программы
     */
    public Exit() {}

    /**
     * Метод для выполнения команды
     * @return null
     */
    @Override
    public String execute() {
        System.exit(0);
        return null;
    }
}