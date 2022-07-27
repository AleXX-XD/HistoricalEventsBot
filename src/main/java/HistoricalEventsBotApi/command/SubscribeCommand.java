package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SubscribeCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final EventService eventService;

    public final static String EVENTSUB_MESSAGE_NO_ACTIVE = "Хочешь прокачать свои знания? Я рад \uD83D\uDE01 Но для начала, жми - /start \uD83D\uDE0E";
    public final static String EVENTSUB_MESSAGE = "Твоя подписка успешно оформлена \uD83D\uDE09\uD83D\uDC4C";
    public final static String EVENTSUB_MESSAGE_FAIL = "Ты уже подписан рассылку \uD83D\uDE01";

    public SubscribeCommand(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            sendBotMessageService.sendMessage(chatId, EVENTSUB_MESSAGE_NO_ACTIVE);
        } else {
            if(user.isSubscription()) {
                sendBotMessageService.sendMessage(chatId, EVENTSUB_MESSAGE_FAIL);
            } else {
                user.setSubscription(true);
                userService.saveUser(user);
                sendBotMessageService.sendMessage(chatId, EVENTSUB_MESSAGE);
            }
        }

    }
}
