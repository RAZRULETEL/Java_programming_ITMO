package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

/**
 * ����� ��� �������� ��������� �� ��������� �� ��������� �����
 */
public class RemoveGreaterKey implements Command, Serializable {
    /** ���� */
    private int key;

    public RemoveGreaterKey() {

    }

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
 * ����� ��� �������� ��������� �� ��������� � ������ ����������� �������� ����
 * @return ������ � ����������� � ���������� �������� ���������
 */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        int count = 0;
        for(int key : collection.getAll().keySet())
            if(key > this.key) {
                collection.remove(key);
                count++;
            }
        return new StringDTO(true, "������� "+count+" ���������");
    }
}
