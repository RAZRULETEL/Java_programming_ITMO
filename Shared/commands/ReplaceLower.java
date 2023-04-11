package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;
/**
 * ����� ��� ������ �������� � ��������� �� �����, ���� ����� ������� ������ �������
 */
public class ReplaceLower implements ObjectCommand, Serializable {
    /**
     * ����� �������
     */
    private Route route;

    /**
     * ��������� �������
     */
    private int key;

    public ReplaceLower() {
    }

    @Override
    public ResultDTO validate(String[] args) {
        if (args != null && args.length == 1) {
            StringDTO keyValidation = validateInt(args[0], "����");
            if (keyValidation.getSuccess()) {
                this.key = Integer.parseInt(keyValidation.getStatus());
                return new ResultDTO(true);
            } else
                return keyValidation;
        } else
            return new StringDTO(false, "�� ������� �������� ���������� ����������");
    }

    @Override
    public ResultDTO isValid() {
        if(route !=null)
            return new ResultDTO(true);
        else
            return new StringDTO(false,"Route �� ����� ���� null");
    }
/**
 * �������� ������� � ��������� �� �����, ���� ����� ������� ������ ���������
 * @return ��������� � ���������� ���������� �������
 */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        if(collection.getAll().get(key) == null)
            return new StringDTO(false,  "�������� � ����� ������ �� ����������");
        if (collection.getAll().get(key).compareTo(route) < 0) {
            collection.put(key, route);
            return new StringDTO(true, "������� ������� ������");
        }else
            return new StringDTO(false, "������� �������� ������");
    }

    @Override
    public ResultDTO setObject(Object obj) {
        if(obj instanceof Route){
            route = ((Route)obj);
            return new ResultDTO(true);
        }else
            return new StringDTO(false, "��������� ������ ���� Route");
    }
}