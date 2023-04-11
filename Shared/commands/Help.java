package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

public class Help implements Command, Serializable {
    /**
     * Constructor for Help class.
     */
    public Help() {}

    /**
    * Выполнение команды.
    * @return строка содержащая информацию о командах.
    */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new StringDTO(true, """
                Список команд:\s
                help : вывести справку по доступным командам
                info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                insert null {element} : добавить новый элемент с заданным ключом
                update id {element} : обновить значение элемента коллекции, id которого равен заданному
                remove_key null : удалить элемент из коллекции по его ключу
                clear : очистить коллекцию
                save : сохранить коллекцию в файл
                execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                exit : завершить программу (без сохранения в файл)
                remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
                replace_if_lower null {element} : заменить значение по ключу, если новое значение меньше старого
                remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный
                filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку
                filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки
                print_unique_distance : вывести уникальные значения поля distance всех элементов в коллекции""");
    }
}
