package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� ������� ���������
 */
public class Clear implements Command, Serializable {
    /**
     * ����������� ��� �������� �������
     */
    public Clear() {
    }

    /**
     * ����� ��� ������� ���������
     * @return ������, ���������� �� �������� ���������� ��������
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        collection.clear();
        return new StringDTO(true,  "��������� ������� �������");
    }
}