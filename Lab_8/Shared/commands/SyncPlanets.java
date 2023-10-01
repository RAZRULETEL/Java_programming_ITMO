package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.command_processing.SynchronizationProvider;
import Shared.commands.interfaces.Command;
import Shared.network.PacketType;
import Shared.resources.AbstractRouteCollection;

public class SyncPlanets implements Command, Serializable {
    private double x, y;
    private int mass = -1, id, radius;
    @Override
    public ResultDTO validate(String[] args) {
        if(args.length >= 3 && args.length <= 5){
            try{
                id = Integer.parseInt(args[0]);
                x = Double.parseDouble(args[1]);
                y = Double.parseDouble(args[2]);
                if(args.length >= 4)
                    mass = Integer.parseInt(args[3]);
                else
                    mass = -1;
                if(args.length == 5)
                    radius = Integer.parseInt(args[4]);
                return new ResultDTO(true);
            }catch (NumberFormatException e){
                return new StringDTO(false, "Все аргументы должны быть числами");
            }
        }else
            return new StringDTO(false, "Неккоректное кол-во аргументов");
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        if(collection instanceof SynchronizationProvider) {
            ((SynchronizationProvider)collection).synchronizeRoute(id, x, y, mass, radius);
            return new ResultDTO(true).setType(PacketType.SynchronizationResult);
        }else
            return new ResultDTO(false).setType(PacketType.SynchronizationResult);
    }
}
