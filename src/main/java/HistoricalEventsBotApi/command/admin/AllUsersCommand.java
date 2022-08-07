package HistoricalEventsBotApi.command.admin;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Update;

@AdminAnnotation
public class AllUsersCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public AllUsersCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        List<User> userList = this.userService.getAllUser();
        StringBuilder builder = new StringBuilder();
        builder.append("Список всех пользователей:\n\n");
        int i = 1;
        for (User user : userList) {
            builder.append(i).append(". ").append(user.getChatId()).append(" / ").append(user.getName())
                    .append(" / Active: ").append(user.isActive())
                    .append(" / Subscription: ")
                    .append(user.isSubscription()).append("\n");
            i += 1;
        }

        this.sendBotMessageService.sendMessage(chatId, builder.toString());
    }
}
