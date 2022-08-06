package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StageIdCommand implements Command {

    private final EventService eventService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String STAGE_ID_FAIL = "По указанному id ничего не нашлось!";

    public StageIdCommand(SendBotMessageService sendBotMessageService, EventService eventService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.eventService = eventService;
        this.user = user;
    }

    @Override
    public void execute(Update update) {
        String text = update.getMessage().getText();
        Event event = eventService.getEvent(text);
        if (event != null) {
            String eventText = "<b>" + event.getTitle() + "</b>" + "\n\n" + event.getText();
            sendBotMessageService.sendMessage(user.getChatId(), eventText);
            sendBotMessageService.sendPhoto(user.getChatId(), event.getImg());
        } else {
            sendBotMessageService.sendMessage(user.getChatId(), STAGE_ID_FAIL);
        }
    }
}
