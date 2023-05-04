package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

public class CheckKeyExists implements Command, Serializable {
    private static final long serialVersionUID = -830279007845064392L;
    private int key;

    public CheckKeyExists(){}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], "ключ");
            if(keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            }else
                return keyValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        for(int k : collection.getAll().keySet())
            if(k == key)
                return new ResultDTO(true);
        return new ResultDTO(false);
    }
}
