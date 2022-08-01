package HistoricalEventsBotApi.command.admin;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static HistoricalEventsBotApi.command.CommandName.*;

@AdminAnnotation
public class AdminHelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨ <b>Возможности администратора</b> ✨\n\n"
                    + "%s - статистика\n"
                    + "%s - получить запись по id\n"
                    + "%s - обновление всей БД\n"
                    + "№записи correct - изменение отдельной записи из списка\n"
                    + "%s - меню администратора\n",
            STATISTIC.getCommandName(),ID.getCommandName(), UPDATE.getCommandName(), AHELP.getCommandName());

    public AdminHelpCommand (SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
