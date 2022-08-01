package HistoricalEventsBotApi.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO("nocommand"),
    ADMIN("/admin"),
    TODAY("/today"),
    DATE("/date"),
    SUBSCRIBE("/subscribe"),
    UNSUBSCRIBE("/unsubscribe"),
    NAME("/name"),
    UPDATE("/update"),
    ID("/id"),
    STATISTIC("/statistic"),
    AHELP("/ahelp");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
