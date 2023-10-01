package Client.client_commands;

import java.util.Optional;

import Client.command_processing.ClientCommandProcessor;
import Client.network.NetworkTools;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.Login;
import Shared.commands.enums.CommandArgument;
import Shared.commands.enums.CommandMessage;
import Shared.commands.interfaces.LocalCommand;
import Shared.commands.interfaces.LocalizedCommand;
import Shared.network.AuthPacket;
import Shared.resources.AbstractRouteCollection;

public class LoginSender implements LocalCommand, LocalizedCommand {
    private String login, password;
    @Override
    public ResultDTO validate(String[] args) {
        if(args == null || args.length != 2)
            return new StringDTO(false, "Неверное количество аргументов");

        StringDTO logVal = validateString(args[0], CommandArgument.LOGIN.toString());
        if(!logVal.getSuccess())
            return logVal;

        StringDTO passVal = validateString(args[1], CommandArgument.PASSWORD.toString());
        if(!passVal.getSuccess())
            return passVal;

        login = logVal.getStatus();
        password = passVal.getStatus();

        return new ResultDTO(true);
    }

    @Override
    public CommandMessage validateLocaled(String[] args) {
        if(args == null || args.length != 2)
            return CommandMessage.InvalidArgumentCount;

        StringDTO logVal = validateString(args[0], CommandArgument.LOGIN.toString());
        if(!logVal.getSuccess())
            return CommandMessage.ArgumentNotPresented.setArgument(CommandArgument.LOGIN);

        StringDTO passVal = validateString(args[1], CommandArgument.PASSWORD.toString());
        if(!passVal.getSuccess())
            return CommandMessage.ArgumentNotPresented.setArgument(CommandArgument.PASSWORD);

        login = logVal.getStatus();
        password = passVal.getStatus();

        return CommandMessage.Success;
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        Login logPack = new Login();
        if(!logPack.validate(new String[]{login, password}).getSuccess())
            return logPack.validate(new String[]{login, password});
        AuthPacket pack = new AuthPacket(null, null, logPack);
        NetworkTools network = NetworkTools.getInstance();
        network.sendObject(pack);
        Optional<ResultDTO> answer;
        answer = network.receiveNextResultPacket();
        if(answer != null && answer.isPresent() && answer.get().getSuccess()) {
            ClientCommandProcessor.setCredentials(login, password);
            return new StringDTO(true, "Авторизация успешна");
        }
        return answer == null || answer.isEmpty() || !(answer.get() instanceof StringDTO) ? new ResultDTO(false) : answer.get();
    }

    @Override
    public CommandMessage executeLocaled(AbstractRouteCollection collection) {
        ResultDTO res = execute(collection);
        if(res.getSuccess())
            return CommandMessage.AuthorizationSuccess;
        if(res instanceof StringDTO){
            CommandMessage msg = CommandMessage.getFromMessage(((StringDTO) res).getStatus());
            return msg == null ? CommandMessage.UnexpectedError : msg;
        }
        return CommandMessage.UnexpectedError;
    }
}
