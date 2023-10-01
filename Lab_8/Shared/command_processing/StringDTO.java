package Shared.command_processing;

public class StringDTO extends ResultDTO{
    private static final long serialVersionUID = 6044115694733081209L;
    private final String status;

    public StringDTO(boolean success, String status) {
        super(success);
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String getFormattedString(){
        return (getSuccess() ? "" : "Error: ") + status;
    }

    @Override
    public String toString() {
        return "StringDTO{" +
                "status='" + status + '\'' +
                "success=" + getSuccess() +
                '}';
    }
}
