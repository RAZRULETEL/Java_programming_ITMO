package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� ������ �� ���������
 */
public class Exit implements Command, Serializable {
    /**
     * ����������� ��� ������ �� ���������
     */
    public Exit() {}

    @Override
    public ResultDTO validate(String[] args) {
        System.exit(0);
        return null;
    }

    /**
     * ����� ��� ���������� �������
     * @return null
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        System.exit(0);
        return null;
    }
}