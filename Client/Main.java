package Client;

import java.util.Optional;
import java.util.Scanner;

import Client.command_processing.ClientCommandProcessor;
import Shared.commands.interfaces.Command;

public class Main {
    private static int port = 4145;

    public static void main(String[] args) {
        if(System.getenv().containsKey("PORT"))
            port = Integer.parseInt(System.getenv("PORT"));
        else
            System.out.println("Переменная окружения PORT не обнаружена");
        Scanner consoleReader = new Scanner(System.in);
        ClientCommandProcessor mainProcessor = new ClientCommandProcessor();

        try {
            NetworkTools.setPort(port);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        System.out.println("Вводите команды, либо help для списка");
        System.out.print("-->");
        if(args.length > 0)
        for(;;)
            if (consoleReader.hasNextLine()) {
                String consoleString = consoleReader.nextLine();
                Optional<Command> command = mainProcessor.processLine(consoleString);
                command.ifPresent(value -> mainProcessor.printResult(mainProcessor.processCommand(value)));
                System.out.print("-->");
            }

    }

}
