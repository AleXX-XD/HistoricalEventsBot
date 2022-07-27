package HistoricalEventsBotApi.util;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.service.EventService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class GettingEvents {

    private static EventService eventService;

    public GettingEvents(EventService eventService) {
        GettingEvents.eventService = eventService;
    }

    public static String getEventMessage(LocalDate localDate) {
        DateTimeFormatter formatterBD = DateTimeFormatter.ofPattern("M/d");
        DateTimeFormatter formatterTG = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"));
        String dateBD = formatterBD.format(localDate);
        String dateTG = formatterTG.format(localDate);
        List<Event> eventsList = eventService.getEvents(dateBD);

        StringBuilder builder = new StringBuilder();
        builder.append("Вот что произошло ").append(dateTG).append(" :").append("\n");
        for(Event event: eventsList) {
            builder.append("\n");
            builder.append("\uD83D\uDD38 ").append(removeDate(event.getTitle(),dateTG)).append("\n");
        }
        return builder.toString();
    }

    private static String removeDate(String text, String date) {
        String finalText = text;
        if(text.startsWith(date)){
            text = text.replaceFirst(date, "").trim();
            String firstLetter = text.substring(0,1).toUpperCase();
            finalText = firstLetter + text.substring(1);
        }
        return finalText;
    }
}
