package HistoricalEventsBotApi.command.admin;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.command.UnknownCommand;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

@AdminAnnotation
public class StaticCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public final static String STATIC_MESSAGE = "Статистика:\n" +
            "Всего зарегистрировано = %s\n" +
            "Всего активных = %s\n" +
            "Всего подписок = %s\n";

    public StaticCommand(SendBotMessageService sendBotMessageService, UserService userService) {
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
            long all = userService.countAll();
            long active = userService.countActiveUsers();
            long subscription = userService.countSubUsers();
            sendBotMessageService.sendMessage(chatId, String.format(STATIC_MESSAGE, all, active, subscription));
        }
    }
}
