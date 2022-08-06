package HistoricalEventsBotApi.command.admin;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.command.UnknownCommand;
import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

@AdminAnnotation
public class IdEventCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public final static String ID_MESSAGE = "Укажи id записи.";

    public IdEventCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            new UnknownCommand(sendBotMessageService).execute(update);
        } else {
            user.setStage(Stage.STAGE_ID);
            userService.saveUser(user);
            sendBotMessageService.sendMessage(chatId, ID_MESSAGE);
        }
    }
}
