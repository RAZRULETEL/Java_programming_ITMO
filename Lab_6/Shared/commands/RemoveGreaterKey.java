package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для удаления элементов из коллекции по заданному ключу
 */
public class RemoveGreaterKey implements Command, Serializable {
    /** Ключ */
    private int key;

    public RemoveGreaterKey() {

    }

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
 * Метод для удаления элементов из коллекции с ключом превышающим заданный ключ
 * @return строка с информацией о количестве удалённых элементов
 */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        int count = 0;
        for(int key : collection.getAll().keySet())
            if(key > this.key) {
                collection.remove(key);
                count++;
            }
        return new StringDTO(true, "Удалено "+count+" элементов");
    }
}
