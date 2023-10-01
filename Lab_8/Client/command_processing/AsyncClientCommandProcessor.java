package Client.command_processing;

import java.util.ListResourceBundle;
import java.util.Optional;

import Client.network.NetworkTools;
import Client.ui.components.CollectionObjectPopup;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;
import Shared.commands.interfaces.LocalCommand;
import Shared.commands.interfaces.ObjectCommand;
import Shared.network.AuthPacket;

public class AsyncClientCommandProcessor extends ClientCommandProcessor{
    private ListResourceBundle currentLocale;

    public AsyncClientCommandProcessor(ListResourceBundle locale){
        super();
        currentLocale = locale;
    }

    public Optional<ResultDTO> sendCommand(Command command){
        if(command == null) return Optional.empty();
        if(command instanceof ObjectCommand){
            ResultDTO objValid = ((ObjectCommand)command).setObject(new CollectionObjectPopup(null, currentLocale).getRoute());
            if(!objValid.getSuccess()) {
                printResult(objValid);
                return Optional.empty();
            }
        }
        if(command instanceof LocalCommand){
            throw new IllegalArgumentException("Cannot send local command");
        }else {
            if(!getAuthStatus().getSuccess()) return Optional.of(getAuthStatus());
            return Optional.of(new ResultDTO(NetworkTools.getInstance().sendObject(new AuthPacket(login, pass, command))));
        }
    }

    public Optional<ResultDTO> sendCommand(Command command, String[] args){
        return sendCommand(command,null,args);
    }

    public Optional<ResultDTO> sendCommand(Command command, Object obj, String[] args){
        ResultDTO validationResult = command.validate(args);
        if(!validationResult.getSuccess()) {
            printResult(validationResult);
            return Optional.empty();
        }
        if(command instanceof ObjectCommand){
            ResultDTO objValid = ((ObjectCommand)command).setObject(obj == null ? new CollectionObjectPopup(null, currentLocale).getRoute() : obj);
            if(!objValid.getSuccess()) {
                printResult(objValid);
                return Optional.empty();
            }
        }
        if(command instanceof LocalCommand){
            throw new IllegalArgumentException();
        }else {
            if(!getAuthStatus().getSuccess()) return Optional.of(getAuthStatus());
            return Optional.of(new ResultDTO(NetworkTools.getInstance().sendObject(new AuthPacket(login, pass, command))));
        }
    }

    public void setLocale(ListResourceBundle newLocale){
        currentLocale = newLocale;
    }
}
