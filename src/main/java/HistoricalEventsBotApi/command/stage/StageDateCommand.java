package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StageDateCommand implements Command {

    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String STAGE_DATE_FAIL_DATE = "<b>%s</b>, такой даты не существует \uD83E\uDD2D " +
            "Ну ничего, все ошибаются. Давай, еще раз.\n" +
            "Введи дату в формате: ДЕНЬ.МЕСЯЦ. Пример: <b>28.07</b> , или жми /back для возврата.";
    public final static String STAGE_DATE_FAIL_FORMAT_DATE = "С форматом даты ошибочка вышла \uD83E\uDD14 Попробуй еще раз.\n" +
            "Введи дату в формате: ДЕНЬ.МЕСЯЦ. Пример: <b>28.07</b> , или жми /back для возврата.";
    private static final String STAGE_DATE_SLASH = "Я жду дату, а не команду \uD83D\uDE01 " +
            "Введи дату в формате: ДЕНЬ.МЕСЯЦ. Пример: <b>28.07</b> , или жми /back для возврата.";
    public final static String STAGE_DATE_BACK = "Хорошо, <b>%s</b> \uD83D\uDC4C Вернул тебя к выбору команд.";

    public StageDateCommand(SendBotMessageService sendBotMessageService, UserService userService, User user) {
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
                sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_DATE_BACK, user.getName()));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_DATE_SLASH);
            }
        } else {
            if (text.matches("\\d{1,2}\\.\\d{1,2}")) {
                LocalDate date = extractionDate(text + ".2020");
                if (date != null) {
                    JSONObject eventsObject = GettingEvents.getEventMessage(date);
                    user.setCurrentEvents(eventsObject.toJSONString());
                    user.setStage(Stage.STAGE_EVENTS);
                    userService.saveUser(user);
                    String content = GettingEvents.getContent(eventsObject);
                    sendBotMessageService.sendMessage(user.getChatId(), content);
                } else {
                    sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_DATE_FAIL_DATE, user.getName()));
                }
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_DATE_FAIL_FORMAT_DATE);
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
