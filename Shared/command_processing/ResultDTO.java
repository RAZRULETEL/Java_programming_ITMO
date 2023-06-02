package Shared.command_processing;

import java.io.Serializable;

import Shared.network.PacketType;

public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 4566891352948048701L;
    private final boolean success;
    private PacketType type = PacketType.ExecutionResult;
    public ResultDTO(boolean success) {
        this.success = success;
    }
    public boolean getSuccess(){
        return success;
    }
    public String getFormattedString(){
        return success ? "Success" : "Error";
    }
    public ResultDTO setType(PacketType type){
        this.type = type;
        return this;
    }

    public PacketType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                ", success=" + success +
                '}';
    }
}
