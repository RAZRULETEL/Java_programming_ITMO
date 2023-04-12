package Client.command_processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

import Client.NetworkTools;
import Client.client_commands.ExecuteFile;
import Shared.command_processing.CommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.CheckIdExists;
import Shared.commands.Clear;
import Shared.commands.ContainsName;
import Shared.commands.Exit;
import Shared.commands.StartsWithName;
import Shared.commands.Help;
import Shared.commands.Info;
import Shared.commands.Insert;
import Shared.commands.PrintUniqueDistance;
import Shared.commands.RemoveByKey;
import Shared.commands.RemoveGreater;
import Shared.commands.RemoveGreaterKey;
import Shared.commands.ReplaceLower;
import Shared.commands.Show;
import Shared.commands.UpdateById;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.LocalCommand;
import Shared.commands.interfaces.ObjectCommand;
import Shared.commands.interfaces.StreamCommand;
import Shared.resources.AbstractRouteCollection;
import Shared.resources.RouteBuilder;

public class ClientCommandProcessor extends CommandProcessor {
    private final HashMap<String, Command> commandMap = new HashMap<>();
    private final InputStream inputStream;

    {
        commandMap.put("exit", new Exit());
        commandMap.put("help", new Help());
        commandMap.put("show", new Show());
        commandMap.put("info", new Info());
        commandMap.put("insert", new Insert());
        commandMap.put("update", new UpdateById());
        commandMap.put("remove_key", new RemoveByKey());
        commandMap.put("clear", new Clear());
        commandMap.put("execute_script", new ExecuteFile());
        commandMap.put("remove_greater", new RemoveGreater());
        commandMap.put("remove_greater_key", new RemoveGreaterKey());
        commandMap.put("filter_contains_name", new ContainsName());
        commandMap.put("filter_starts_with_name", new StartsWithName());
        commandMap.put("print_unique_distance", new PrintUniqueDistance());
        commandMap.put("replace_if_lower", new ReplaceLower());
    }
    public ClientCommandProcessor(){
        inputStream = System.in;
    }

    @Override
    public Optional<Command> processLine(String line){
        String command = line.split(" ")[0];
        String[] args = null;
        if (line.split(" ").length - 1 >= 0) {
            args = new String[line.split(" ").length-1];
            System.arraycopy(line.split(" "), 1, args, 0, line.split(" ").length - 1);
        }
        Command result = null;
        try {
             result = commandMap.get(command);
             if(result == null){
                 printResult(new StringDTO(false, "Данной команды не существует"));
                 return Optional.empty();
             }
             ResultDTO validationResult = result.validate(args);
             if(!validationResult.getSuccess()) {
                 printResult(validationResult);
                 return Optional.empty();
             }
             if(result instanceof ObjectCommand){
                 ResultDTO objValid = ((ObjectCommand)result).setObject(new RouteBuilder(inputStream).build());
                 if(!objValid.getSuccess()) {
                     printResult(objValid);
                     return Optional.empty();
                 }
             }
        }catch (NoSuchElementException | IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return Optional.of(result);
    }

    @Override
    public ResultDTO processCommand(Command command) {
        if(command == null) {
            return null;
        }
        if(command instanceof LocalCommand){
            return command.execute(null);
        }else
            if(NetworkTools.sendCommand(command))
                return NetworkTools.receiveAnswer();
            else
                return new StringDTO(false, "отправка команды прервана");
    }
}
