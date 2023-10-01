package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static final long serialVersionUID = 2323737444506192991L;
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
        HashMap<Integer, Route> collectionMap = collection.getAll();
        for(int key : collectionMap.keySet())
            if(collectionMap.get(key).getName().startsWith(name))
                routes.add(collectionMap.get(key));
        return new ArrayDTO(true, "", routes.toArray());
    }
}
