package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NoCommand implements Command
{
    private final SendBotMessageService sendBotMessageService;

    public static final String NO_MESSAGE = "Упс \uD83D\uDE15 не понятно... \n" +
            "Я поддерживаю команды, начинающиеся со слеша (/).\n"
            + "Чтобы посмотреть список команд введи /help";

    public NoCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), NO_MESSAGE);
    }
}
