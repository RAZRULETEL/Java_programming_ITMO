package Shared.commands;

import java.io.InputStream;
import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;
/**
 * Класс для добавления объекта в коллекцию
 */
public class Insert implements ObjectCommand, Serializable {
    private Route route;
    private int key;

    public Insert() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], "ключ");
            if(keyValidation.getSuccess()){
                this.key = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            }else
                return keyValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO isValid() {
        if(route != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "Route не может быть null");
    }
    /**
     * Метод для добавления объекта в коллекцию
     * @return строка с результатом добавления
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        collection.put(key, route);
        route = null;
        return new StringDTO(true, "Объект " + collection.get(key) + " успешно добавлен");
    }

    @Override
    public ResultDTO setObject(Object obj) {
        if(obj instanceof Route){
            route = ((Route)obj);
            return new ResultDTO(true);
        }else
            return new StringDTO(false, "Требуется объект типа Route");
    }
}
