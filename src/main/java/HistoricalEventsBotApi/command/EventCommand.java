package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class EventCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final EventService eventService;

    public final static String EVENT_MESSAGE_FAIL = "Хочешь прокачать свои знания? Я рад \uD83D\uDE01 Жми - /start \uD83D\uDE0E";

    public EventCommand(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.findByChatId(chatId);
        if(user == null || !user.isActive()){
            sendBotMessageService.sendMessage(chatId, EVENT_MESSAGE_FAIL);
        } else {
            String message = getEventMessage(LocalDate.now());
            sendBotMessageService.sendMessage(chatId, message);
        }
    }

    private String getEventMessage(LocalDate localDate) {
        DateTimeFormatter formatterBD = DateTimeFormatter.ofPattern("M/d");
        DateTimeFormatter formatterTG = DateTimeFormatter.ofPattern("dd MMMM", Locale.forLanguageTag("ru"));
        String dateBD = formatterBD.format(localDate);
        String dateTG = formatterTG.format(localDate);
        List<Event> eventsList = eventService.getEvents(dateBD);

        StringBuilder builder = new StringBuilder();
        builder.append("Вот что произошло ").append(dateTG).append(" :").append("\n");
        for(Event event: eventsList) {
            builder.append("\n");
            builder.append("✅ ").append(removeDate(event.getTitle(),dateTG)).append("\n");
        }
        return builder.toString();
    }

    private String removeDate(String text, String date) {
        String finalText = text;
        if(text.startsWith(date)) {
            text = text.replaceFirst(date, "").trim();
            String firstLetter = text.substring(0,1).toUpperCase();
            finalText = firstLetter + text.substring(1);
        }
        return finalText;
    }
}
