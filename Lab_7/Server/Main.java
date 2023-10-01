package Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import Server.command_processing.DBCollectionAdapter;
import Server.command_processing.MyDBCollection;
import Server.command_processing.ServerCommandProcessor;
import Server.network_modules.ConnectionManager;
import Server.thread_tasks.CommandExecutorTask;
import Server.thread_tasks.ReceiverTask;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static String filename = "data";

    private static final int CACHED_THREADS_COUNT = 10;
    private static final int SENDER_THREADS_COUNT = 10;

    private static final ExecutorService clientListenerPool = Executors.newCachedThreadPool(), clientAnswerSenderPool = Executors.newFixedThreadPool(SENDER_THREADS_COUNT);
    private static final ForkJoinPool commandExecutorPool = new ForkJoinPool();

    private static final String databaseHostPort = "jdbc:postgresql://pg:5432" ;
    private static final String databaseName = "studs";

//    private static final String databaseHostPort = "jdbc:postgresql://localhost:5433" ;
//    private static final String databaseName = "postgres";

    private static final String serverCMPLogin = "admin", serverCMPPasswrod = "admin";

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

        if(args == null || args.length != 2){
            System.out.println("Недостаточно аргументов, требуется логин и пароль от БД");
            System.exit(1);
        }
        String dbLogin = args[0];
        String dbPassword = args[1];

        MyDBCollection dbCollection = null;
        try {
            dbCollection = new MyDBCollection(databaseHostPort+"/"+databaseName, dbLogin, dbPassword);
        } catch (SQLException | NoSuchFieldException e) {
            LOGGER.error("Cannot connect to DB, can't continue working without it, stopping app");
            System.exit(1);
        }
        ServerCommandProcessor mainProcessor = new ServerCommandProcessor(new DBCollectionAdapter(dbCollection, serverCMPLogin, serverCMPPasswrod));

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        ConnectionManager server = new ConnectionManager(port);

        LOGGER.info("Server started listening packets");
        while(true){
            if(ReceiverTask.getThreadsCount() < CACHED_THREADS_COUNT)
                clientListenerPool.execute(new ReceiverTask(server, commandExecutorPool, new CommandExecutorTask(dbCollection, clientAnswerSenderPool)));
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
