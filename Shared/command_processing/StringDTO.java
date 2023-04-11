package Shared.command_processing;

public class StringDTO extends ResultDTO{
    private final String status;

    public StringDTO(boolean success, String status) {
        super(success);
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StringDTO{" +
                "status='" + status + '\'' +
                "success=" + getSuccess() +
                '}';
    }
}
