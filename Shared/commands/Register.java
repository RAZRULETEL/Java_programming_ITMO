package Shared.commands;

import java.io.Serializable;

import Shared.command_processing.AuthProvider;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.resources.AbstractRouteCollection;

public class Register implements Command, Serializable {
    private static final long serialVersionUID = -3089373308513387661L;
    private String login;
    private String password;

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
        return ((AuthProvider) collection).register(login, password);
    }
}
