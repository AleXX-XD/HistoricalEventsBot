package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.annotation.AdminAnnotation;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;

import HistoricalEventsBotApi.util.IndexingSiteUtil;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

@AdminAnnotation
public class UpdateEventsCommand implements Command {

    private final Logger log = Logger.getLogger(UpdateEventsCommand.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;

    public UpdateEventsCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.userService = userService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        log.info("Запущено обновление базы данных, администратором  '" + user.getName() + "' / chatId = " + user.getChatId() + " ");
        sendBotMessageService.sendMessage(chatId, "Запущено обновление базы данных");
        IndexingSiteUtil.startIndexing();
    }
}
