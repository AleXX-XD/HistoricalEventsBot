package HistoricalEventsBotApi.util;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.service.EventService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    public static JSONObject getEventMessage(LocalDate localDate) {
        DateTimeFormatter formatterBD = DateTimeFormatter.ofPattern("M/d");
        DateTimeFormatter formatterTG = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"));
        String dateBD = formatterBD.format(localDate);
        String dateTG = formatterTG.format(localDate);
        List<Event> eventsList = eventService.getEvents(dateBD);

        JSONObject contentObject = new JSONObject();
        JSONArray eventsArray = new JSONArray();
        StringBuilder builder = new StringBuilder();
        int i = 1;
        builder.append("Вот что произошло ").append(dateTG).append(" :").append("\n");
        for(Event event: eventsList) {
            String title = removeDate(event.getTitle(),dateTG);
            builder.append("\n").append("<b>").append(i).append("</b>. ")
                    .append("\uD83D\uDD38 ")
                    .append(title).append("\n");
            JSONObject eventObject = new JSONObject();
            eventObject.put("number", i);
            eventObject.put("title", "<b>" + title + "</b>");
            eventObject.put("text", event.getText());
            eventObject.put("image", event.getImg());
            eventObject.put("link", event.getLink());
            eventsArray.add(eventObject);
            i += 1;
        }
        builder.append("\nВведи номер события, чтобы получить подробную информацию или /back для отмены");
        contentObject.put("content", builder.toString());
        contentObject.put("events", eventsArray);
        return contentObject;
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

    public static String getEventForCorrect(String json, int number) throws ParseException {
        JSONArray array = GettingEvents.getArray(json);
        JSONObject event = (JSONObject) array.get(number-1);
        return event.toJSONString();
    }

    public static String getContent (JSONObject object) {
        return object.get("content").toString();
    }

    public static String getEvent (String json, int number) throws ParseException {
        JSONArray array = GettingEvents.getArray(json);
        JSONObject event = (JSONObject) array.get(number-1);
        return event.get("title") +  "\n\n" +  event.get("text") + "\n";
    }

    public static String getEvent (JSONObject json) {
        return json.get("title") +  "\n\n" +  json.get("text") + "\n";
    }

    public static String getImage (String json, int number) throws ParseException {
        JSONArray array = GettingEvents.getArray(json);
        JSONObject event = (JSONObject) array.get(number-1);
        return (String) event.get("image");
    }

    public static String getImage (JSONObject json) {
        return (String) json.get("image");
    }

    public static int countCurrentEvents(String json) throws ParseException {
        return GettingEvents.getArray(json).size();
    }

    private static JSONArray getArray(String text) throws ParseException {
        JSONObject object = stringToJson(text);
        return  (JSONArray) object.get("events");
    }

    public static JSONObject stringToJson(String text) throws ParseException {
        return (JSONObject) new JSONParser().parse(text);
    }
}
