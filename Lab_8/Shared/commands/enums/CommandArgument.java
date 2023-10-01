package Shared.commands.enums;

public enum CommandArgument {
    KEY, ID, LOGIN, PASSWORD, NAME, FILE_NAME;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
