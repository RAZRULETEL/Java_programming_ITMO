package Shared.command_processing;

import java.util.Arrays;

public class ArrayDTO extends StringDTO {
    private static final long serialVersionUID = 2178755595795012556L;
    private final Object[] data;
    public ArrayDTO(boolean success, String status, Object[] data) {
        super(success, status);
        this.data = data;
    }
    public Object[] getData() {
        return data;
    }

    @Override
    public String getFormattedString(){
        return (getSuccess() ? "" : "Error: ") + getStatus() + ": " + Arrays.toString(data);
    }

    @Override
    public String toString() {
        return "ArrayDTO{" +
                "data=" + Arrays.toString(data) +
                "status='" + getStatus() + '\'' +
                "success=" + getSuccess() +
                "type=" + getType() +
                '}';
    }
}
