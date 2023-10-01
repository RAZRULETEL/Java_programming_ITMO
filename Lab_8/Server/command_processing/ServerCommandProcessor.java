package Server.command_processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import Shared.command_processing.CommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.CheckIdExists;
import Shared.commands.CheckKeyExists;
import Shared.commands.Clear;
import Shared.commands.ContainsName;
import Shared.commands.Exit;
import Shared.commands.Help;
import Shared.commands.Info;
import Shared.commands.Insert;
import Shared.commands.PrintUniqueDistance;
import Shared.commands.RemoveByKey;
import Shared.commands.RemoveGreater;
import Shared.commands.RemoveGreaterKey;
import Shared.commands.ReplaceLower;
import Shared.commands.Save;
import Shared.commands.Show;
import Shared.commands.StartsWithName;
import Shared.commands.UpdateById;
import Shared.commands.interfaces.Command;
import Shared.other.Serializer;
import Shared.resources.AbstractRouteCollection;

public class ServerCommandProcessor extends CommandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServerCommandProcessor.class);
    private static final Set<Class> clientAllowedCommands = new HashSet<Class>();
    private final HashMap<String, Command> commandMap = new HashMap<String, Command>();

    {
        commandMap.put("save", new Save());
        commandMap.put("exit", new Exit());
    }
    static {


        clientAllowedCommands.add(Help.class);
        clientAllowedCommands.add(Show.class);
        clientAllowedCommands.add(Info.class);
        clientAllowedCommands.add(Insert.class);
        clientAllowedCommands.add(UpdateById.class);
        clientAllowedCommands.add(RemoveByKey.class);
        clientAllowedCommands.add(Clear.class);
        clientAllowedCommands.add(RemoveGreater.class);
        clientAllowedCommands.add(RemoveGreaterKey.class);
        clientAllowedCommands.add(ContainsName.class);
        clientAllowedCommands.add(StartsWithName.class);
        clientAllowedCommands.add(PrintUniqueDistance.class);
        clientAllowedCommands.add(CheckIdExists.class);
        clientAllowedCommands.add(ReplaceLower.class);
        clientAllowedCommands.add(CheckKeyExists.class);
    }

    private final AbstractRouteCollection collection;
    public ServerCommandProcessor(AbstractRouteCollection collection) {
        if(collection == null) {
            logger.info("CommandProcessor initialized with blank collection");
            this.collection = new MyCollection();
        }else {
            logger.info("CommandProcessor initialized with collection of {} elements", collection.size());
            this.collection = collection;
        }
    }
    @Override
    public ResultDTO processCommand(Command command){
        if(command != null) {
            logger.info("Executing {} command", command.getClass().getSimpleName());
            return command.execute(collection);
        }
        return null;
    }
    public ResultDTO processClientBytes(byte[] serializedCommand){
        Command command = null;
        try {
            command = (Command) Serializer.deserialize(new String(serializedCommand).getBytes());
        } catch (IOException e) {
            logger.error("Object deserialization error");
            return new StringDTO(false,  "Ошибка десериализации команды, вероятно данные были потеряны");
        } catch (ClassNotFoundException e) {
            logger.error("Object class not found: {}", e.getMessage());
            return new StringDTO(false, "Класс данного объекта не найден");
        }
        if(!clientAllowedCommands.contains(command.getClass()))
            return new StringDTO(false, "Выполнение данной команды запрещенно");
        return processCommand(command);
    }

    @Override
    public Optional<Command> processLine(String line) {
        String command = line.split(" ")[0];
        String[] args = null;
        if (line.split(" ").length - 1 >= 0) {
            args = new String[line.split(" ").length-1];
            System.arraycopy(line.split(" "), 1, args, 0, line.split(" ").length - 1);
        }
        if(commandMap.get(command) == null)
            return Optional.empty();
        ResultDTO val = commandMap.get(command).validate(args);
        if(val.getSuccess())
            return Optional.of(commandMap.get(command));
        printResult(val);
        return Optional.empty();
    }

    public Set<String> getServerCommands() {
        return commandMap.keySet();
    }
}
