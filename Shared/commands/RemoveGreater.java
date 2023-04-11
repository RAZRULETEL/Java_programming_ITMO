package Shared.commands;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.ObjectCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.Route;

/**
 * ����� ��� �������� �� ��������� ���� ���������, ����������� ��������
 */
public class RemoveGreater implements ObjectCommand {
    private Route route;

    /**
     * ����������� ��� �������� ������� � ��������� �����������
     *
     */
    public RemoveGreater() {

    }

    @Override
    public ResultDTO isValid(){
        if(route != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "Route �� ����� ���� null");
    }
    /**
     * ����� ��� �������� �� ��������� ���� ���������, ����������� ��������
     *
     * @return ������ � ����������� ��������� ���������
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        int count = 0;
        for(int key : collection.getAll().keySet())
            if(collection.getAll().get(key).compareTo(route) > 0) {
                collection.remove(key);
                count++;
            }
        return new StringDTO(true, "������� "+count+" ���������");
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
