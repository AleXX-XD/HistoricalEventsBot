package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static HistoricalEventsBotApi.command.CommandName.*;
import static HistoricalEventsBotApi.command.CommandName.HELP;

public class AdminHelpCommand implements Command{

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("✨ <b>Дотупные команды</b> ✨\n\n"
                    + "<b>Начать\\закончить работу с ботом</b>\n"
                    + "▶ %s - начать работу со мной\n"
                    + "⏹ %s - приостановить работу со мной\n\n"
                    + "<b>Получение исторических событий</b>\n"
                    + "\uD83D\uDCC5 %s - получить список событий на сегодняшний день\n"
                    + "\uD83D\uDCC6 %s - получить список событий в указанную дату\n"
                    + "\uD83D\uDD14 %s - подписаться на ежедневную рассылку\n"
                    + "\uD83D\uDD15 %s - отписаться от рассылки \n\n"
                    + "\uD83D\uDC64 %s - добавить/изменить имя\n"
                    + "\uD83D\uDE91 %s - помощь в работе со мной\n",
            START.getCommandName(), STOP.getCommandName(), TODAY.getCommandName(),
            DATE.getCommandName(), SUBSCRIBE.getCommandName(), UNSUBSCRIBE.getCommandName(),
            NAME.getCommandName(), HELP.getCommandName());

    public AdminHelpCommand (SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
