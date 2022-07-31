package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class TodayCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public final static String EVENT_MESSAGE_NO_ACTIVE = "Хочешь прокачать свои знания? Я рад \uD83D\uDE01 Но для начала, жми - /start \uD83D\uDE0E";

    public TodayCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()){
            sendBotMessageService.sendMessage(chatId, EVENT_MESSAGE_NO_ACTIVE);
        } else {
            JSONObject eventsObject = GettingEvents.getEventMessage(LocalDate.now());
            user.setCurrentEvents(eventsObject.toJSONString());
            user.setStage(Stage.STAGE_EVENTS);
            userService.saveUser(user);
            String content = GettingEvents.getContent(eventsObject);
            sendBotMessageService.sendMessage(chatId, content);
            System.out.println(eventsObject.toJSONString());
        }
    }
}
