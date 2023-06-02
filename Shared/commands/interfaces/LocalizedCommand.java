package Shared.commands.interfaces;

import Shared.commands.enums.CommandMessage;
import Shared.resources.AbstractRouteCollection;

public interface LocalizedCommand extends Command{
    CommandMessage validateLocaled(String[] args);
    CommandMessage executeLocaled(AbstractRouteCollection collection);
}
