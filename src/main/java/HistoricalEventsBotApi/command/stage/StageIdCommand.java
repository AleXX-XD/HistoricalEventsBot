package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StageIdCommand implements Command {

    private final UserService userService;
    private final EventService eventService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String STAGE_ID_FAIL = "По указанному id ничего не нашлось!";
    private static final String STAGE_ID_SLASH = "Я жду id, а не команду \uD83D\uDE01 " +
            "Введи id в формате url, или жми /back для возврата.";
    public final static String STAGE_ID_BACK = "Хорошо, <b>%s</b> \uD83D\uDC4C Вернул тебя к выбору команд.";

    public StageIdCommand(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
        this.user = user;
    }

    @Override
    public void execute(Update update) {
        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            if (text.equals("/back")) {
                user.setStage(Stage.NONE);
                userService.saveUser(user);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_ID_BACK, user.getName()));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ID_SLASH);
            }
        } else {
            Event event = eventService.getEvent(text);
            if(event != null) {
                String eventText = "<b>" + event.getTitle() + "</b>" + "\n\n" + event.getText();
                sendBotMessageService.sendMessage(user.getChatId(), eventText);
                sendBotMessageService.sendPhoto(user.getChatId(), event.getImg());
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ID_FAIL);
            }
        }
    }
}
