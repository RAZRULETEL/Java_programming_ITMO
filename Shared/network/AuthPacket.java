package Shared.network;

import java.io.Serializable;

import Shared.commands.interfaces.Command;

public class AuthPacket implements Serializable {
    private static final long serialVersionUID = -6654424402681097143L;
    private final String login;
    private final String password;
    private final Command payload;
    public AuthPacket(String login, String password, Command payload){
        this.login = login;
        this.password = password;
        this.payload = payload;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Command getPayload() {
        return payload;
    }
}
