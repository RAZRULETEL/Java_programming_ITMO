package Shared.command_processing;

public interface AuthProvider {
    ResultDTO login(String login, String password);
    ResultDTO register(String login, String password);
}
