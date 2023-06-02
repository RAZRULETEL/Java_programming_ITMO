package Shared.commands;

import java.io.Serializable;
import java.util.HashMap;

import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandArgument;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;
/**
 * Класс для замены элемента в коллекции по ключу, если новый элемент меньше старого
 */
public class ReplaceLower implements ObjectCommand, Serializable {
    private static final long serialVersionUID = 377066655521419087L;
    /**
     * Новый элемент
     */
    private Route route;

    /**
     * Аргументы команды
     */
    private int key;

    public ReplaceLower() {
    }

    @Override
    public ResultDTO validate(String[] args) {
        if (args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], CommandArgument.KEY.toString());
            if (keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());
                Command keyValidator = new CheckKeyExists();
                keyValidator.validate(new String[]{args[0]});
                ResultDTO keyValidationRes = new ClientCommandProcessor().processCommand(keyValidator);
                if(keyValidationRes == null)
                    return new StringDTO(false, "не удалось проверить существование ключа");
                if(keyValidationRes instanceof StringDTO)
                    return keyValidationRes;
                if(!keyValidationRes.getSuccess())
                    return new StringDTO(false, "Похоже что данного ключа не существует");
                return new ResultDTO(true);
            } else
                return keyValidation;
        } else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO isValid() {
        if(route !=null)
            return new ResultDTO(true);
        else
            return new StringDTO(false,"Route не может быть null");
    }
/**
 * Заменяет элемент в коллекции по ключу, если новый элемент меньше заданного
 * @return сообщение о результате выполнения команды
 */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        HashMap<Integer, Route> collectionMap = collection.getAll();
        if(collectionMap.get(key) == null)
            return new StringDTO(false,  "Элемента с таким ключом не существует");
        if (collectionMap.get(key).compareTo(route) < 0) {
            collection.put(key, route);
            return new StringDTO(true, "Элемент успешно заменён");
        }else
            return new StringDTO(false, "Элемент оказался больше");
    }

    @Override
    public ResultDTO setObject(Object obj) {
        if(obj instanceof Route){
            route = ((Route)obj);
            return new ResultDTO(true);
        }else
            return new StringDTO(false, "Требуется объект типа Route");
    }

    @Override
    public CommandArgument[] getArgs(){
        return new CommandArgument[]{CommandArgument.KEY};
    }
}