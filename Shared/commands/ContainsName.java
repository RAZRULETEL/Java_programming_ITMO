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

public class ContainsName implements Command, Serializable {
    private static final long serialVersionUID = -6420601260222824840L;
    private String name;
    public ContainsName(){}

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

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        ArrayList<Route> routes = new ArrayList<Route>();
        HashMap<Integer, Route> collectionMap = collection.getAll();
        for(int key : collectionMap.keySet())
            if(collectionMap.get(key).getName().contains(name))
                routes.add(collectionMap.get(key));
        return new ArrayDTO(true, "", routes.toArray());
    }
}
