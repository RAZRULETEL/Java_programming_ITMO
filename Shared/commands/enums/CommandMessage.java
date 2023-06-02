package Shared.commands.enums;

import java.util.ListResourceBundle;

public enum CommandMessage {
    InvalidArgumentCount("invalid argument count"), Success("sucess", true), ArgumentNotPresented("argument %s not presented"),
    InvalidArgument("invalid %s argument"), AuthorizationSuccess("authorization success", true),
    UnexpectedError("unexpected error"), RegistrationSuccess("registration success", true), UserAlreadyExists("user already exists"),
    UserNotExists("user not exists"), UserNotAuthorized("user not authorized"),
    IncorrectPassword("invalid password"), ServerNotResponded("server not responded"), RecursionRestricted("recursion restricted");
    private final String message;
    private final boolean positive;
    private CommandArgument arg;

    CommandMessage(String message) {
        this.message = message;
        positive = false;
    }

    CommandMessage(String message, boolean isPositive) {
        this.message = message;
        positive = isPositive;
    }

    public CommandMessage setArgument(CommandArgument arg){
        if(arg == null) throw new IllegalArgumentException("Argument cannot be null");
        this.arg = arg;
        return this;
    }

    public boolean isPositive() {return positive;}

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage() {
        return arg == null ? message : String.format(message, arg.toString());
    }

    public String getFormattedMessage(ListResourceBundle locale) {
        return arg == null ? locale.getString(message) : String.format(locale.getString(message), locale.getString(arg.toString()));
    }

    public CommandArgument getArgument() {return arg;}

    public static CommandMessage getFromMessage(String message){
        for(CommandMessage msg : values())
            if(msg.message.equals(message))
                return msg;
        return null;
    }

    @Override
    public String toString() {
        return message;
    }
}
