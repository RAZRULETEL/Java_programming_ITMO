package Shared.commands;

import Server.command_processing.YamlTools;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;


/**
 * ����� Save ������������ ��� ���������� ��������� � ����.
 */
public class Save implements Command {
    /** ��������� ������� */
    private String fileName = "data.yaml";

    public Save() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length < 2){
            if(args.length == 1){
                StringDTO nameValidation = validateString(args[0], "���������");
                if(nameValidation.getSuccess()) {
                    this.fileName = nameValidation.getStatus();
                    return new ResultDTO(true);
                }else
                    return nameValidation;
            }
            fileName = "data.yaml";
            return new ResultDTO(true);
        }else{
            return new StringDTO(false, "��������� �� ����� 1 ���������");
        }
    }

    @Override
    public ResultDTO isValid(){
        if(fileName != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "�������� ����� �� �������");
    }

    /**
     * ����� ���������� �������
     * @return ��������� � ���������� ����������
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        String fileName = this.fileName;
        this.fileName = null;
        if(YamlTools.save(collection.getAll(), fileName))
            return new StringDTO(true, "��������� ������� ���������");
        else
            return new StringDTO(false, "������ ���������� ���������");

    }
}
