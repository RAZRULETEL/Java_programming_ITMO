package Shared.commands.interfaces;

import java.io.InputStream;

import Shared.command_processing.ResultDTO;

public interface StreamCommand extends Command{
    ResultDTO setStream(InputStream inStream);
}
