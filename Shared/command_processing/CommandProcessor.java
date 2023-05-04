package Shared.command_processing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

import Shared.commands.interfaces.Command;

public abstract class CommandProcessor {
    private InputStream inputStream;

    public CommandProcessor(){};
    {
        inputStream = System.in;
    }
    public ArrayList<ResultDTO> processStream(InputStream inStream){
        if(inStream != null)
            inputStream = inStream;
        else
            return null;
        Scanner linesReader = new Scanner(inputStream);
        ArrayList<ResultDTO> results = new ArrayList<ResultDTO>();
        while (linesReader.hasNextLine())
            processLine(linesReader.nextLine()).ifPresent(command -> {
                results.add(this.processCommand(command));
                this.printResult(results.get(results.size()-1));
            });
        return results;
    }
    public abstract Optional<Command> processLine(String line);
    public abstract ResultDTO processCommand(Command command);
    public void printResult(ResultDTO result){
        if(result instanceof StringDTO)
            System.out.print((result.getSuccess() ? "" : "Error: ") + ((StringDTO) result).getStatus());
        if(result instanceof ArrayDTO)
            System.out.print(Arrays.toString(((ArrayDTO) result).getData()));
        System.out.println();
    }
}
