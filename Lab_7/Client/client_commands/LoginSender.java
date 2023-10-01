package Client.client_commands;

import java.util.Optional;

import Client.NetworkTools;
import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.Login;
import Shared.commands.interfaces.LocalCommand;
import Shared.network.AuthPacket;
import Shared.resources.AbstractRouteCollection;

public class LoginSender implements LocalCommand {
    private String login, password;
    @Override
    public ResultDTO validate(String[] args) {
        if(args == null || args.length != 2)
            return new StringDTO(false, "Вы указали неверное количество аргументов, требуется логин и пароль");

        StringDTO logVal = validateString(args[0], "логин");
        if(!logVal.getSuccess())
            return logVal;

        StringDTO passVal = validateString(args[1], "пароль");
        if(!passVal.getSuccess())
            return passVal;

        login = logVal.getStatus();
        password = passVal.getStatus();

        return new ResultDTO(true);
    }

    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        Login logPack = new Login();
        if(!logPack.validate(new String[]{login, password}).getSuccess())
            return logPack.validate(new String[]{login, password});
        AuthPacket pack = new AuthPacket(null, null, logPack);
        NetworkTools.sendOject(pack);
        Optional<ResultDTO> answer = NetworkTools.receiveAnswer();
        if(answer != null && answer.isPresent() && answer.get().getSuccess()) {
            ClientCommandProcessor.setCredentials(login, password);
            return new StringDTO(true, "Авторизация успешна");
        }
        return answer == null || answer.isEmpty() || !(answer.get() instanceof StringDTO) ? new StringDTO(false, "Ошибка авторизации, вероятно вы вели неправильные данные") : answer.get();
    }
}
