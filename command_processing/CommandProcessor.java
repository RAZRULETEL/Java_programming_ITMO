package command_processing;

import commands.Clear;
import commands.ExecuteFile;
import commands.Exit;
import commands.FilterByName;
import commands.Help;
import commands.Info;
import commands.Insert;
import commands.PrintUniqueDistance;
import commands.RemoveByKey;
import commands.RemoveGreater;
import commands.RemoveGreaterKey;
import commands.ReplaceLower;
import commands.Save;
import commands.Show;
import commands.UpdateById;
import commands.interfaces.Command;
import resources.Route;
import resources.RouteBuilder;

public class CommandProcessor {
    private MyCollection collection;
    private RouteBuilder builder = null;
    private Command delayedCommand = null;
    public CommandProcessor(MyCollection collection) {
        this.collection = collection;
    }
    public CommandProcessor(Route[] collectionArray){
        collection = new MyCollection();
        if(collectionArray != null)
        for(Route route : collectionArray)
            collection.put(route.getId(), route);
    }
    public void process(String line){
        if(line == null || line.equals(""))
            return;
        String command = line.split(" ")[0];
        String[] args = null;
        if (line.split(" ").length - 1 >= 0) {
            args = new String[line.split(" ").length-1];
            System.arraycopy(line.split(" "), 1, args, 0, line.split(" ").length - 1);
        }

            if((builder == null || !builder.isBuilding())) {
                if ((command.equals("replace_if_lower") || command.equals("insert") || command.equals("remove_greater") || command.equals("update"))) {
                    builder = new RouteBuilder();
                    System.out.println(builder.getDescription());
                }
            }else{
                builder.build(command);
                if(builder.isBuilding())
                    System.out.println(builder.getDescription());
                else
                    System.out.println(delayedCommand.execute());

            }
        Command result = null;
        if(delayedCommand == null)
            result =
                switch (command){
                    case "help" -> new Help();
                    case "show" -> new Show(collection.getMap());
                    case "info" -> new Info(collection);
                    case "clear" -> new Clear(collection);
                    case "save" -> new Save(collection.getMap(), args);
                    case "remove_greater_key" -> new RemoveGreaterKey(collection, args);
                    case "remove_key" -> new RemoveByKey(collection, args);
                    case "execute_script" -> new ExecuteFile(collection, args);
                    case "insert" -> new Insert(builder, collection, args);
                    case "print_unique_distance" -> new PrintUniqueDistance(collection.getMap());
                    case "remove_greater" -> new RemoveGreater(builder, collection);
                    case "update" -> new UpdateById(builder, collection, args);
                    case "replace_if_lower" -> new ReplaceLower(builder, collection, args);
                    case "filter_contains_name" -> new FilterByName(collection.getMap(), args, 0);
                    case "filter_starts_with_name" -> new FilterByName(collection.getMap(), args, -1);
                    case "exit" -> new Exit();
                    default -> () -> "Такой команды не существует, напишите help для списка команд";
                };
        if(result != null)
            if(builder == null || !builder.isBuilding())
                System.out.println(result.execute());
            else
                delayedCommand = result;
        if(delayedCommand != null && (builder == null || !builder.isBuilding()))
            delayedCommand = null;
    }

}
