package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

public class UpdateById implements ObjectCommand, Serializable {
    private Route route;
    private int id;

    public UpdateById() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], "id");
            if(keyValidation.getSuccess()){
                this.id = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            }else
                return keyValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        for(int k : collection.getAll().keySet())
            if(collection.getAll().get(k).getId() == id) {
                collection.put(id, route);
                return new StringDTO(true, "Объект "+route+" успешно изменён");
            }
            return new StringDTO(false, "Объекта с данным id не существует");

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