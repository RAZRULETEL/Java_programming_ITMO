package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� ������ ���������� ����������
 */
public class PrintUniqueDistance implements Command, Serializable {
    /**
     * ����������� ��� �������� �������
     */
    public PrintUniqueDistance() {
    }

    /**
     * ����� ��� ������ ���������� ���������� ���������
     * @return ������ � ����������� ������������
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