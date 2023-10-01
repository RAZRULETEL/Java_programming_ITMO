package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * Класс для печати уникальных расстояний
 */
public class PrintUniqueDistance implements Command, Serializable {
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
        ArrayList<Integer> distances = new ArrayList<>();
        for(int key : collection.getAll().keySet())
            if(distances.stream().noneMatch(e -> Objects.equals(e, collection.getAll().get(key).getDistance())))
                distances.add(collection.getAll().get(key).getDistance());
        return new ArrayDTO(true, "", distances.toArray());
    }
}
