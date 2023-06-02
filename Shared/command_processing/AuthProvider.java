package Shared.command_processing;

import java.util.ArrayList;
import java.util.Map;

public interface AuthProvider {
    ResultDTO login(String login, String password);
    ResultDTO register(String login, String password);
    Map<String, ArrayList<Integer>> getUsersObjects();
}
