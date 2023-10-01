package Shared.command_processing;

import java.io.Serializable;

public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 4566891352948048701L;
    private final boolean success;
    public ResultDTO(boolean success) {
        this.success = success;
    }
    public boolean getSuccess(){
        return success;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "success=" + success +
                '}';
    }
}
