package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс для удаления из коллекции всех элементов, превышающих заданный
 */
public class RemoveGreater implements ObjectCommand, Serializable {
    private Route route;

    /**
     * Конструктор для создания объекта с заданными параметрами
     *
     */
    public RemoveGreater() {

    }

    @Override
    public ResultDTO isValid(){
        if(route != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "Route не может быть null");
    }
    /**
     * Метод для удаления из коллекции всех элементов, превышающих заданный
     *
     * @return строка с количеством удаленных элементов
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        int count = 0;
        for(int key : collection.getAll().keySet())
            if(collection.getAll().get(key).compareTo(route) > 0) {
                collection.remove(key);
                count++;
            }
        return new StringDTO(true, "Удалено "+count+" элементов");
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
