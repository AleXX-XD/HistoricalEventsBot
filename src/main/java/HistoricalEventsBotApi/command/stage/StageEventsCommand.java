package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import HistoricalEventsBotApi.util.SendingEvents;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StageEventsCommand implements Command {

    private final Logger log = Logger.getLogger(StageEventsCommand.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String STAGE_EVENTS = "Чтобы получить информацию о другом событии " +
            "выбери номер от 1 до %s, или жми /back для возврата.";
    public final static String STAGE_EVENTS_FAIL_FORMAT = "Эммм...и что это? \uD83E\uDD14\n" +
            "Введи номер события, чтобы отобразить подробную информацию, или жми /back для возврата. ";
    private static final String STAGE_EVENTS_SLASH = "Я жду номер события, а не команду \uD83D\uDE01";
    public final static String STAGE_EVENTS_OVERFLOW = "Нет такого номера! Введи номер из диапазона: %s, или жми /back для возврата.";
    public final static String STAGE_EVENTS_BACK = "Хорошо, <b>%s</b> \uD83D\uDC4C Вернул тебя к выбору команд.";

    public StageEventsCommand(SendBotMessageService sendBotMessageService, UserService userService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.user = user;
    }

    @Override
    public void execute(Update update) {
        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            if (text.equals("/back")) {
                user.setStage(Stage.NONE);
                userService.saveUser(user);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_EVENTS_BACK, user.getName()));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_EVENTS_SLASH);
            }
        } else {
            if (text.matches("\\d{1,2}")) {
                try {
                    int number = Integer.parseInt(text);
                    int count = GettingEvents.countCurrentEvents(user.getCurrentEvents());
                    if(number <= count) {
                        String message = GettingEvents.getEvent(user.getCurrentEvents(), number);
                        String image = GettingEvents.getImage(user.getCurrentEvents(), number);
                        sendBotMessageService.sendMessage(user.getChatId(), message);
                        sendBotMessageService.sendPhoto(user.getChatId(), image);
                        sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_EVENTS, count));
                    } else {
                        sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_EVENTS_OVERFLOW, "1-" + count));
                    }
                } catch (ParseException ex) {
                    log.warn("Ошибка обработки Json из БД : " + ex.getMessage());
                    ex.printStackTrace();
                }


            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_EVENTS_FAIL_FORMAT);
            }
        }


    }

    private LocalDate extractionDate(String text) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
