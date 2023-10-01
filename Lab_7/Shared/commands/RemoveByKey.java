package Shared.commands;

import java.io.Serializable;

import Server.exceptions.UserUnauthorizedException;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для удаления элемента из коллекции по ключу.
 */
public class RemoveByKey implements Command, Serializable {
    private static final long serialVersionUID = 2658198322515633859L;
    /** Аргументы команды. */
    private int key;

    public RemoveByKey() {}

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

    /**
     * Удаляет элемент из коллекции по ключу.
     * @return сообщение о результате выполнения команды.
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        try {
            if (collection.getAll().get(key) != null)
                if(collection.remove(collection.getAll().get(key).getId()))
                    return new StringDTO(true, "Элемент успешно удалён");
                else
                    return new StringDTO(false, "Ошибка удаления элемента");
            else
                return new StringDTO(false, "Элемента с таким ключом не существует");
        }catch (UserUnauthorizedException e){
            return new StringDTO(false, "Похоже что у вас нет доступа к данному объекту");
        }
    }
}
