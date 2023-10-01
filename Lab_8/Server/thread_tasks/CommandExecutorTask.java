package Server.thread_tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;

import Server.command_processing.DBCollectionAdapter;
import Server.command_processing.MyDBCollection;
import Server.command_processing.ServerCommandProcessor;
import Server.exceptions.UserUnauthorizedException;
import Server.network_modules.ConnectionManager;
import Shared.command_processing.ResultDTO;
import Shared.command_processing.StringDTO;
import Shared.commands.interfaces.Command;
import Shared.network.AuthPacket;
import Shared.other.Serializer;
import Shared.resources.AbstractRouteCollection;

public class CommandExecutorTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutorTask.class);
    private final ExecutorService responseSender;
    private final MyDBCollection collection;
    private byte[] data = null;
    private SocketAddress address = null;
    private ConnectionManager server = null;

    public CommandExecutorTask(MyDBCollection collection, ExecutorService responseSender) {
        if(collection == null)
            throw new IllegalArgumentException("Collection cannot be null");
        this.collection = collection;
        if(responseSender == null)
            throw new IllegalArgumentException("ResponseSenderPool cannot be null");
        this.responseSender = responseSender;
    }
    public CommandExecutorTask setNetworkManagerAndResponseAddress(ConnectionManager server, SocketAddress address) {
        this.address = address;
        this.server = server;
        return this;
    }
    public CommandExecutorTask setData(byte[] request){
        data = request;

        return this;
    }
    @Override
    public void run() {
        ResultDTO result = null;
        if(data == null)
            throw new IllegalStateException("Data not added, nothing to process");
        try {
            Object packet = Serializer.deserialize(data);
            if(packet instanceof AuthPacket) {
                AbstractRouteCollection adapted = new DBCollectionAdapter(collection, ((AuthPacket) packet).getLogin(), ((AuthPacket) packet).getPassword());
                Command command = ((AuthPacket) packet).getPayload();
                result = new ServerCommandProcessor(adapted).processCommand(command);
            }else
                result = new StringDTO(false, "Прислан объект неправильного типа");
        } catch (IOException e) {
            LOGGER.error("Object deserialization error");
            result = new StringDTO(false,  "Ошибка десериализации команды, вероятно данные были потеряны");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Object class not found: {}", e.getMessage());
            result = new StringDTO(false, "Класс данного объекта не найден");
        }catch (UserUnauthorizedException e){
            LOGGER.error(e.getMessage());
            result = new StringDTO(false, "Ошибка авторизации, ваши данные для входа не верны");
        }catch (ClassCastException e){
            result = new StringDTO(false, "Сервер не поддерживает авторизацию");
            LOGGER.error("User tried authorize, but server collection not support authorization");
        }catch (NoClassDefFoundError e){
            result = new StringDTO(false, "Похоже что сервер не знает такой команды");
            LOGGER.error("Received unknown object or server is delusional, so reboot it");
        }
        if(server != null && address != null)
            responseSender.execute(new ResponseTask(result, address, server));
    }
}
