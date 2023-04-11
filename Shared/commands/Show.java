package Shared.commands;

import java.io.Serializable;
import java.util.ArrayList;

import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * ����� Show ��������� ��������� Command
 */
public class Show implements Command, Serializable {
    /**
     * ����������� ������ Show
     */
    public Show() {
    }

    /**
     * ����� ������� ��������� ���������
     * @return ��������� ���������
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new ArrayDTO(true, "", new Object[]{collection.getAll()});
    }
}
