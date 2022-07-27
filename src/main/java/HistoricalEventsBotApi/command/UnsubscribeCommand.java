package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnsubscribeCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final EventService eventService;

    public final static String EVENTUNSUB_MESSAGE_NO_ACTIVE = "Для начала работы со мной, жми - /start \uD83D\uDE0E";
    public final static String EVENTUNSUB_MESSAGE = "Твоя подписка приостановлена \uD83D\uDC4C Если передумаешь, буду рад \uD83D\uDE09";
    public final static String EVENTUNSUB_MESSAGE_FAIL = "У тебя нет действующей подписки. Можешь подписаться, нажав /eventsub ";

    public UnsubscribeCommand(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            sendBotMessageService.sendMessage(chatId, EVENTUNSUB_MESSAGE_NO_ACTIVE);
        } else {
            if(user.isSubscription()) {
                user.setSubscription(false);
                userService.saveUser(user);

                sendBotMessageService.sendMessage(chatId, EVENTUNSUB_MESSAGE);
            } else {
                sendBotMessageService.sendMessage(chatId, EVENTUNSUB_MESSAGE_FAIL);
            }

        }

    }
}
