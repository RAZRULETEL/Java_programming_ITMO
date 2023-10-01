package commands;

import command_processing.MyCollection;
import commands.interfaces.Command;
import resources.RouteBuilder;

public class UpdateById implements Command {
    private RouteBuilder route;
    private MyCollection collection;
    private String[] args;

    public UpdateById(RouteBuilder route, MyCollection collection, String[] args) {
        this.route = route;
        this.collection = collection;
        this.args = args;
    }

    @Override
    public String execute() {
        try {
            int key;
            if (args != null && args.length == 1 && !args[0].equals(""))
                key = Integer.parseInt(args[0]);
            else
                return "id не указан";
            for(int k : collection.getMap().keySet())
                if(collection.getMap().get(k).getId() == key) {
                    collection.put(key, route.getRoute());
                    return "Объект "+route.getRoute()+" успешно изменён";
                }
            return "Объекта с данным id не существует";
        }catch (NumberFormatException ex){
            return "Ключ не указан";
        }

    }
}