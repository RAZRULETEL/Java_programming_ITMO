package Shared.commands;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * Класс для печати уникальных расстояний
 */
public class PrintUniqueDistance implements Command, Serializable {
    private static final long serialVersionUID = -3572837396320917130L;

    /**
     * Конструктор для создания объекта
     */
    public PrintUniqueDistance() {
    }

    /**
     * Метод для печати уникальных расстояний маршрутов
     * @return строка с уникальными расстояниями
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        HashSet<Integer> distances = new HashSet<>();
        HashMap<Integer, Route> collectionCopy = collection.getAll();
        for(int key : collectionCopy.keySet())
            distances.add(collectionCopy.get(key).getDistance());
        return new ArrayDTO(true, "", distances.toArray());
    }
}