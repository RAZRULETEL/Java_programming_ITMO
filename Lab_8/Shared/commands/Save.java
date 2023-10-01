package Shared.commands;

import Server.command_processing.YamlTools;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;


/**
 * Класс Save используется для сохранения коллекции в файл.
 */
public class Save implements Command {
    /** Аргументы команды */
    private String fileName = "data.yaml";

    public Save() {}

    @Override
    public ResultDTO validate(String[] args) {
        if(args != null && args.length < 2){
            if(args.length == 1){
                StringDTO nameValidation = validateString(args[0], "подстрока");
                if(nameValidation.getSuccess()) {
                    this.fileName = nameValidation.getStatus();
                    return new ResultDTO(true);
                }else
                    return nameValidation;
            }
            fileName = "data.yaml";
            return new ResultDTO(true);
        }else{
            return new StringDTO(false, "Требуется не более 1 аргумента");
        }
    }

    @Override
    public ResultDTO isValid(){
        if(fileName != null)
            return new ResultDTO(true);
        else
            return new StringDTO(false, "Название файла не указано");
    }

    /**
     * Метод выполнения команды
     * @return сообщение о результате выполнения
     */
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        String fileName = this.fileName;
        this.fileName = "data.yaml";
        if(YamlTools.save(collection.getAll(), fileName))
            return new StringDTO(true, "Коллекция успешно сохранена");
        else
            return new StringDTO(false, "Ошибка сохранения коллекции");

    }
}
