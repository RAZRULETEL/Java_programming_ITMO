package Client.client_commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.CommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.enums.CommandArgument;
import Shared.commands.enums.CommandMessage;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.LocalCommand;
import Shared.commands.interfaces.StreamCommand;
import Shared.resources.AbstractRouteCollection;

public class ExecuteFile implements StreamCommand, LocalCommand {
    public static final String FileNameArg = "file_name";
    private static HashSet<String> files = new HashSet<String>();
    private InputStream inputStream;
    private String fileName;
    private ArrayList<Command> commands = null;
    @Override
    public ResultDTO setStream(InputStream inStream) {
        if(inStream != null){
            this.inputStream = inStream;
            return new ResultDTO(true);
        }else
            return new StringDTO(false, "Поток не может быть null");
    }

    @Override
    public ResultDTO validate(String[] args){
        if(args != null && args.length == 1) {
            StringDTO nameValidation = validateString(args[0], CommandArgument.FILE_NAME.toString());
            if(nameValidation.getSuccess()) {
                this.fileName = nameValidation.getStatus();
                try {
                    inputStream = new FileInputStream(fileName);
                } catch (FileNotFoundException e) {
                    return new StringDTO(false, "Такого файла не существует");
                }
                return new StringDTO(true, "Скрипт успешно выполнен");
            }else
                return nameValidation;
        }else
            return new StringDTO(false, "Вы указали неверное количество аргументов");
    }

    @Override
    public ResultDTO isValid(){
        if(commands != null)
            return new ResultDTO(true);
        return new StringDTO(false, "Файл не был указан, либо его не удалось считать");
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        if(files.contains(fileName))
            return new StringDTO(false, CommandMessage.RecursionRestricted.toString());
        files.add(fileName);
        CommandProcessor processor = new ClientCommandProcessor();
        processor.processStream(inputStream);
        commands = null;
        files = new HashSet<>();
        try {
            inputStream.close();
        } catch (IOException ignored) {}
        inputStream = null;
        return new StringDTO(true, "Выполнеине скрипта успешно завершено");
    }

    @Override
    public CommandArgument[] getArgs(){
        return new CommandArgument[]{CommandArgument.FILE_NAME};
    }
}