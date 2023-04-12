package Client.client_commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import Client.NetworkTools;
import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.CommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.LocalCommand;
import Shared.commands.interfaces.StreamCommand;
import Shared.resources.AbstractRouteCollection;

public class ExecuteFile implements StreamCommand, LocalCommand {
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
            StringDTO nameValidation = validateString(args[0], "подстрока");
            if(nameValidation.getSuccess()) {
                this.fileName = nameValidation.getStatus();
                InputStream script;
                try {
                    script = new FileInputStream(fileName);
                } catch (FileNotFoundException e) {
                    return new StringDTO(false, "Такого файла не существует");
                }
                if(files.contains(fileName))
                    return new StringDTO(false, "Данный скрипт уже выполнялся, рекурсия запрещена");
                files.add(fileName);
                CommandProcessor processor = new ClientCommandProcessor();
                commands = new ArrayList<>();
                commands = processor.processStream(script);

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
        ClientCommandProcessor processor = new ClientCommandProcessor();
        for(Command command: commands){
            processor.printResult(processor.processCommand(command));
        }
        commands = null;
        files = new HashSet<>();
        return new StringDTO(true, "Выполнеине скрипта успешно завершено");
    }
}
