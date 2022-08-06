package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command
{
    private static final String UNKNOWN_MESSAGE = "Моя Твоя не понимать \uD83D\uDE1F\nЖми /help чтобы отобразить список доступных команд.";

    private final SendBotMessageService sendBotMessageService;

    public UnknownCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {

        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
    }
}
