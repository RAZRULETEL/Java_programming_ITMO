package Shared.commands;

import java.io.Serializable;
import java.util.Date;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� ��������� ���������� � ���������
 */
public class Info implements Command, Serializable {
    /**
     * ����������� ��� ������������� ����� ������
     */
    public Info() {
    }

    /**
     * ����� ��� ��������� ���������� � ���������
     * @return ������ � ����������� � ���������
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new StringDTO(true,  "��������� ���� " + collection.getAll().getClass().getName() + ", �������� " + collection.getAll().size() + " ���������, ���������������� " + new Date(collection.initTime));
    }
}