package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

public class Help implements Command, Serializable {
    /**
     * Constructor for Help class.
     */
    public Help() {}

    /**
    * ���������� �������.
    * @return ������ ���������� ���������� � ��������.
    */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        return new StringDTO(true, """
                ������ ������:\s
                help : ������� ������� �� ��������� ��������
                info : ������� � ����������� ����� ������ ���������� � ��������� (���, ���� �������������, ���������� ��������� � �.�.)
                show : ������� � ����������� ����� ������ ��� �������� ��������� � ��������� �������������
                insert null {element} : �������� ����� ������� � �������� ������
                update id {element} : �������� �������� �������� ���������, id �������� ����� ���������
                remove_key null : ������� ������� �� ��������� �� ��� �����
                clear : �������� ���������
                save : ��������� ��������� � ����
                execute_script file_name : ������� � ��������� ������ �� ���������� �����. � ������� ���������� ������� � ����� �� ����, � ������� �� ������ ������������ � ������������� ������.
                exit : ��������� ��������� (��� ���������� � ����)
                remove_greater {element} : ������� �� ��������� ��� ��������, ����������� ��������
                replace_if_lower null {element} : �������� �������� �� �����, ���� ����� �������� ������ �������
                remove_greater_key null : ������� �� ��������� ��� ��������, ���� ������� ��������� ��������
                filter_contains_name name : ������� ��������, �������� ���� name ������� �������� �������� ���������
                filter_starts_with_name name : ������� ��������, �������� ���� name ������� ���������� � �������� ���������
                print_unique_distance : ������� ���������� �������� ���� distance ���� ��������� � ���������""");
    }
}
