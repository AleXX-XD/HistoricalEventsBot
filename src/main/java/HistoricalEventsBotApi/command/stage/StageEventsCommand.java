package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StageEventsCommand implements Command {

    private final Logger log = Logger.getLogger(StageEventsCommand.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String STAGE_EVENTS = "Чтобы получить информацию о другом событии " +
            "выбери номер от 1 до %s, или жми /back для возврата.";
    public final static String STAGE_EVENTS_FAIL_FORMAT = "Эммм...и что это? \uD83E\uDD14\n" +
            "Введи номер события, чтобы отобразить подробную информацию, или жми /back для возврата. ";
    private static final String STAGE_EVENTS_SLASH = "Я жду номер, а не команду \uD83D\uDE01\n" +
            "Введи номер события или жми /back для возврата. ";
    public final static String STAGE_EVENTS_OVERFLOW = "Нет такого номера! Введи номер из диапазона: %s, или жми /back для возврата.";
    public final static String STAGE_EVENTS_BACK = "Хорошо, <b>%s</b> \uD83D\uDC4C Вернул тебя к выбору команд.";
    public final static String STAGE_EVENTS_CORRECT = "Укажи праметр (date,title,text,image) и значение для изменения через !-! или delete для удаления записи : \n" +
            "Формат: параметр!-!значение\n" +
            "Пример: date!-!4/25 , image!-!https://url , text!-!Текст\n" +
            "Для завершения изменений, жми /back";

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
                    if (number <= count) {
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
            } else if (text.matches("\\d{1,2}\\scorrect") && user.isAdmin()) {
                String[] textSplit = text.split("\\s");
                try {
                    String event = GettingEvents.getEventForCorrect(user.getCurrentEvents(), Integer.parseInt(textSplit[0]));
                    user.setStage(Stage.STAGE_CORRECT);
                    user.setCurrentEvents(event);
                    userService.saveUser(user);
                    sendBotMessageService.sendMessage(user.getChatId(), STAGE_EVENTS_CORRECT);
                } catch (ParseException ex) {
                    log.warn("Ошибка обработки Json из БД : " + ex.getMessage());
                }
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_EVENTS_FAIL_FORMAT);
            }
        }
    }
}
