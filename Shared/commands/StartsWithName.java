package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс FilterByName реализует интерфейс Command.
 * Он используется для фильтрации коллекции по имени.
 */
public class StartsWithName implements Command, Serializable {
    private String name;

    public StartsWithName() {}

    @Override
    public ResultDTO validate(String[] args){
        if(args != null && args.length == 1) {
            StringDTO nameValidation = validateString(args[0], "подстрока");
            if(nameValidation.getSuccess()) {
                this.name = nameValidation.getStatus();
                return new ResultDTO(true);
            }else
                return nameValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO isValid(){
        if(name != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "Подстрока не может быть пустой");
    }

    /**
     * Выполняет фильтрацию по подстроке в имени.
     * @return отсортированная по имени коллекция.
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        ArrayList<Route> routes = new ArrayList<Route>();
        for(int key : collection.getAll().keySet())
            if(collection.getAll().get(key).getName().startsWith(name))
                routes.add(collection.getAll().get(key));
        return new ArrayDTO(true, "", routes.toArray());
    }
}
