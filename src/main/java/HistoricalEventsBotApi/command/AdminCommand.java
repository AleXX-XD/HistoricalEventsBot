package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.IndexingSiteUtil;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminCommand implements Command{

    @Value("${bot.password}")
    private String adminPassword;

    private final UserService userService;
    private final IndexingSiteUtil indexingSiteUtil;
    private final SendBotMessageService sendBotMessageService;

    public final static String ADMIN_MESSAGE = "Смотрите-ка, кто пожаловал! Милости прошу..., вот только после ввода пароля \uD83E\uDD28";

    public AdminCommand(SendBotMessageService sendBotMessageService, UserService userService, IndexingSiteUtil indexingSiteUtil) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.indexingSiteUtil = indexingSiteUtil;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.findByChatId(chatId);
        if(user == null || !user.isActive()) {
            new UnknownCommand(sendBotMessageService).execute(update);
        } else {
            sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE);


            //indexingSiteUtil.startIndexing();
        }
    }
}
