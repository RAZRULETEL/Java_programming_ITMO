package Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;

import Server.command_processing.MyCollection;
import Server.command_processing.ServerCommandProcessor;
import Server.command_processing.YamlTools;
import Server.network_modules.ConnectionManager;
import Shared.command_processing.ResultDTO;
import Shared.commands.Save;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static String filename = "data";
    public static void main(String[] args){
        int port = 4145;
        if(System.getenv().containsKey("PORT"))
            port = Integer.parseInt(System.getenv("PORT"));
        else
            System.out.println("Переменная окружения с портом не обнаружена");
        if(System.getenv().containsKey("FILE_NAME"))
            filename = System.getenv("FILE_NAME");
        else
            System.out.println("Переменная окружения не обнаружена");

        ServerCommandProcessor mainProcessor = new ServerCommandProcessor(new MyCollection(YamlTools.load(filename)));

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        ConnectionManager server = new ConnectionManager(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {mainProcessor.printResult(mainProcessor.processCommand(new Save()));}));

        LOGGER.info("Server started listening packets");
        while(true){
            SocketAddress clientAddress = server.receiveNextPacket();
            if (clientAddress != null) {
                ResultDTO result = mainProcessor.processClientBytes(server.getRequestBytes(clientAddress));
                server.answerClient(clientAddress, result);
            }
            try {
            if (consoleReader.ready()){
                mainProcessor.processLine(consoleReader.readLine())
                        .ifPresentOrElse(value -> mainProcessor.printResult(mainProcessor.processCommand(value))
                                , () -> {System.out.println("Похоже, что данной команды не существует, доступны следующие команды: "+mainProcessor.getServerCommands());});
            }
            } catch (IOException ignored) {}
        }
    }
}
