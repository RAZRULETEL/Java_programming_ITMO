package Shared.commands;

import java.io.Serializable;
import java.util.HashMap;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandArgument;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс для удаления элементов из коллекции по заданному ключу
 */
public class RemoveGreaterKey implements Command, Serializable {
    private static final long serialVersionUID = -1691068577780092206L;
    /**
     * Ключ
     */
    private int key;

    public RemoveGreaterKey() {

    }

    @Override
    public ResultDTO validate(String[] args) {
        if (args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], CommandArgument.KEY.toString());
            if (keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            } else
                return keyValidation;
        } else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    /**
     * Метод для удаления элементов из коллекции с ключом превышающим заданный ключ
     *
     * @return строка с информацией о количестве удалённых элементов
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        int count = 0;
        HashMap<Integer, Route> collMap = collection.getAll();
        for (int key : collMap.keySet())
            if (key > this.key) {
                if (collection.remove(collMap.get(key).getId()))
                    count++;
            }
        System.out.println(count);
        return new StringDTO(true, "Удалено " + count + " элементов");
    }

    @Override
    public CommandArgument[] getArgs() {
        return new CommandArgument[]{CommandArgument.KEY};
    }
}
