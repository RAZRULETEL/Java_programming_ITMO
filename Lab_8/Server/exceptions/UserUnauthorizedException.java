package Server.exceptions;

public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(String login){
        super("Invalid user credentials, for login:"+login);
    }
}
