package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� �������� �������� �� ��������� �� �����.
 */
public class RemoveByKey implements Command, Serializable {
    /** ��������� �������. */
    private int key;

    public RemoveByKey() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], "����");
            if(keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            }else
                return keyValidation;
        }else
            return new StringDTO(false, "�� ������� �������� ���������� ����������");
    }

    /**
     * ������� ������� �� ��������� �� �����.
     * @return ��������� � ���������� ���������� �������.
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        if (collection.remove(key))
            return new StringDTO(true, "������� ������� �����");
        else
            return new StringDTO(false, "�������� � ����� ������ �� ����������");
    }
}
