package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class NameCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final static String NAME_COMMAND = "Напиши, как мне к тебе обращаться. " +
            "Чтобы я не сломал язык, используй пожалуйста только буквы \uD83D\uDE0A";
    public final static String NAME_MESSAGE_NO_ACTIVE = "Для начала работы со мной, жми - /start \uD83D\uDE0E";

    public NameCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            sendBotMessageService.sendMessage(chatId, NAME_MESSAGE_NO_ACTIVE);
        } else {
            user.setStage(Stage.STAGE_NAME);
            userService.saveUser(user);
            sendBotMessageService.sendMessage(user.getChatId(), NAME_COMMAND);
        }
    }
}
