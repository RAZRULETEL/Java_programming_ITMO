package Shared.commands;

import java.io.Serializable;

import Client.NetworkTools;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;
/**
 * Класс для замены элемента в коллекции по ключу, если новый элемент меньше старого
 */
public class ReplaceLower implements ObjectCommand, Serializable {
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
            StringDTO keyValidation = validateInt(args[0], "ключ");
            if (keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());

                Command keyValidator = new CheckKeyExists();
                keyValidator.validate(args);
                NetworkTools.sendCommand(keyValidator);
                ResultDTO keyValidationRes = NetworkTools.receiveAnswer();
                if(keyValidationRes == null || !keyValidationRes.getSuccess())
                    return keyValidationRes instanceof StringDTO ? keyValidationRes : new StringDTO(false, "Похоже что данного ключа не существует");
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
        if(collection.getAll().get(key) == null)
            return new StringDTO(false,  "Элемента с таким ключом не существует");
        if (collection.getAll().get(key).compareTo(route) < 0) {
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
}
