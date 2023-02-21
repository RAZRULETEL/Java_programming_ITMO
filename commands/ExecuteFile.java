package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import command_processing.CommandProcessor;
import command_processing.MyCollection;
import commands.interfaces.Command;

/**
 * Класс для выполнения команд из файла
 *
 */
public class ExecuteFile implements Command {
    private MyCollection collection;
    private String[] args;

    /**
     * Конструктор для создания объекта класса ExecuteFile
     *
     * @param collection коллекция, над которой будут выполняться команды
     * @param args аргументы команды
     */
    public ExecuteFile(MyCollection collection, String[] args) {
        this.collection = collection;
        this.args = args;
    }

    /**
     * Метод для выполнения команд из файла
     *
     * @return строка с результатом выполнения команды
     */
    public String execute() {
        String fileName;
        if (args != null && args.length == 1 && !args[0].equals(""))
            fileName = (args[0]);
        else
            return "Название файла не указано";
        CommandProcessor cmd = new CommandProcessor(collection);
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Такого файла не существует");
            return null;
        }
        while(scanner.hasNextLine())
            cmd.process(scanner.nextLine());
        return "Выполнение скрипта из файла завершено";
    }
}