package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DateCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    private final static String DATE_COMMAND = "Укажи дату для поиска событий, в формате: ДЕНЬ.МЕСЯЦ.\n" +
            "Пример: <b>28.07</b>.";
    public final static String DATE_MESSAGE_NO_ACTIVE = "Для начала работы со мной, жми - /start \uD83D\uDE0E";

    public DateCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            sendBotMessageService.sendMessage(chatId, DATE_MESSAGE_NO_ACTIVE);
        } else {
            user.setStage(Stage.STAGE_DATE);
            userService.saveUser(user);
            sendBotMessageService.sendMessage(user.getChatId(), DATE_COMMAND);
        }
    }
}
