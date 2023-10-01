package Shared.commands;

import java.io.Serializable;

import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandArgument;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;
/**
 * Класс для добавления объекта в коллекцию
 */
public class Insert implements ObjectCommand, Serializable {
    private static final long serialVersionUID = 3700070382996829632L;
    private Route route;
    private int key;

    public Insert() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && (args.length == 1)) {
            StringDTO keyValidation = validateInt(args[0], CommandArgument.KEY.toString());
            if(keyValidation.getSuccess()){
                this.key = Integer.parseInt(keyValidation.getStatus());
                Command keyValidator = new CheckKeyExists();
                keyValidator.validate(new String[]{args[0]});
                ResultDTO keyValidationRes = new ClientCommandProcessor().processCommand(keyValidator);
                if(keyValidationRes == null)
                    return new StringDTO(false, "не удалось проверить существование ключа");
                if(keyValidationRes instanceof StringDTO)
                    return keyValidationRes;
                if(keyValidationRes.getSuccess())
                    return keyValidationRes instanceof StringDTO ? keyValidationRes : new StringDTO(false, "Похоже что данный ключ уже существует");
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
        boolean sucess = collection.put(key, route);
        route = null;
        return sucess ? new StringDTO( true, "Объект успешно добавлен") : new StringDTO( false, "Ошибка добавления объекта");
    }

    @Override
    public CommandArgument[] getArgs(){
        return new CommandArgument[]{CommandArgument.KEY};
    }

    @Override
    public ResultDTO setObject(Object obj) {
        if(obj instanceof Route){
            route = ((Route)obj);
            return new ResultDTO( true);
        }else
            return new StringDTO( false, "Требуется объект типа Route");
    }
}
