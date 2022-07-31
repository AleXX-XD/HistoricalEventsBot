package HistoricalEventsBotApi.command.admin;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.command.UnknownCommand;
import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminCommand implements Command {

    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;

    public final static String ADMIN_MESSAGE = "Смотрите-ка, кто пожаловал! Милости прошу..., вот только после ввода пароля \uD83E\uDD28\n";

    public AdminCommand(SendBotMessageService sendBotMessageService, UserService userService) {
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
            user.setStage(Stage.STAGE_ADMIN);
            userService.saveUser(user);
            sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE);
        }
    }

}
