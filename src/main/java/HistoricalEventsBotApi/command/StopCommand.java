package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StopCommand implements Command
{
    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public static final String STOP_MESSAGE = "\uD83D\uDE22 Нам же было так хорошо вместе...\n" +
            "Жаль, что ты покидаешь меня... Эххх....\n" +
            "Возвращайся, я буду ждать тебя \uD83D\uDE09 \uD83D\uDC4C ";
    public static final String STOP_MESSAGE_FAIL = "Эммм... Мы же даже не познакомились \uD83E\uDD28 \n" +
            "Может, для начала введешь /start и мы начнем увлекательное общение?";

    public StopCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        User user = userService.findByChatId(update.getMessage().getChatId().toString());
        if(user == null) {
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), STOP_MESSAGE_FAIL);
        } else {
            sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), STOP_MESSAGE);
            user.setActive(false);
            userService.saveUser(user);
        }
    }
}
