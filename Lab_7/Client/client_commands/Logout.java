package Client.client_commands;

import Client.command_processing.ClientCommandProcessor;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.LocalCommand;
import Shared.resources.AbstractRouteCollection;

public class Logout implements LocalCommand {
    @Override
    public ResultDTO execute(AbstractRouteCollection collection) {
        ClientCommandProcessor.removeCredentials();
        return new StringDTO(true, "Данные для входа успешно очищены");
    }
}
