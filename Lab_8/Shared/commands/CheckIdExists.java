package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

public class CheckIdExists implements Command, Serializable {
    private static final long serialVersionUID = 4681326235946677336L;
    private int id;

    public CheckIdExists(){}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length == 1) {
            StringDTO idValidation = validateInt(args[0], "id", Route.MIN_ID, Integer.MAX_VALUE);
            if(idValidation.getSuccess()) {
                this.id = Integer.parseInt(idValidation.getStatus());
                return new ResultDTO(true);
            }else
                return idValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO isValid() {
        if(id >= Route.MIN_ID)
            return new ResultDTO(true);
        else return new StringDTO(false, "Неккоректные данные: id должен быть не меньше "+Route.MIN_ID);
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        for(Route rt : collection.getAll().values())
            if(rt.getId() == id)
                return new ResultDTO( true);
        return new ResultDTO(false);
    }
}
