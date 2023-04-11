package Shared.commands.interfaces;

import java.io.InputStream;

import Shared.command_processing.ResultDTO;

public interface ObjectCommand extends Command {
    public ResultDTO setObject(Object obj);
}
