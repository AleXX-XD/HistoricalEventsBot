package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StageDateCommand implements Command {

    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String DATE_MESSAGE_FAIL_DATE = "<b>%s</b>, такой даты не существует \uD83E\uDD2D " +
            "Ну ничего, все ошибаются. Давай, еще раз.\n" +
            "Введи дату в формате: ДЕНЬ.МЕСЯЦ. Пример: <b>28.07</b> , или жми /back для возврата.";
    public final static String DATE_MESSAGE_FAIL_FORMAT_DATE = "С форматом даты ошибочка вышла \uD83E\uDD14 Попробуй еще раз.\n" +
            "Введи дату в формате: ДЕНЬ.МЕСЯЦ. Пример: <b>28.07</b> , или жми /back для возврата.";

    public StageDateCommand(SendBotMessageService sendBotMessageService, UserService userService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.user = user;
    }

    @Override
    public void execute(Update update) {
        String[] textList = update.getMessage().getText().trim().split("\\s+");
        if(textList.length == 1) {
            if(textList[0].matches("\\d{1,2}\\.\\d{1,2}")) {
                LocalDate date = extractionDate(textList[0] + ".2020");
                if(date != null) {
                    String message = GettingEvents.getEventMessage(date);
                    sendBotMessageService.sendMessage(user.getChatId(), message);
                    user.setStage(Stage.STAGE_EVENTS);
                    user.setStageTime(LocalTime.now());
                    userService.saveUser(user);
                } else {
                    sendBotMessageService.sendMessage(user.getChatId(), String.format(DATE_MESSAGE_FAIL_DATE, user.getName()));
                }
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), DATE_MESSAGE_FAIL_FORMAT_DATE);
            }
        }
    }

    private LocalDate extractionDate(String text) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
            return LocalDate.parse(text, formatter);
        }
        catch (DateTimeParseException ex) {
            return null;
        }
    }
}
