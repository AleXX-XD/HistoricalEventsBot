package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static HistoricalEventsBotApi.command.stage.Stage.*;

@Component
public class StageDefinition {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;
    private final EventService eventService;
    private final Config config;

    public StageDefinition(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService, Config config) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
        this.config = config;
    }

    public void definition(User user, Update update) {
        switch (user.getStage()) {
            case STAGE_NAME: {
                new StageNameCommand(sendBotMessageService, userService, user).execute(update);
                break;
            }
            case STAGE_DATE: {
                new StageDateCommand(sendBotMessageService, userService, user).execute(update);
                break;
            }
            case STAGE_EVENTS: {
                new StageEventsCommand(sendBotMessageService, userService, user).execute(update);
                break;
            }
            case STAGE_ADMIN: {
                new StageAdminCommand(sendBotMessageService, userService, user, config).execute(update);
                break;
            }
            case STAGE_CORRECT: {
                if(user.isActive() && user.isAdmin()) {
                    new StageCorrectCommand(sendBotMessageService, userService, eventService, user).execute(update);
                } else {
                    user.setStage(NONE);
                    userService.saveUser(user);
                    sendBotMessageService.sendMessage(user.getChatId(), "Команда доступна только администратору!");
                }
                break;
            }
            case STAGE_ID: {
                if(user.isActive() && user.isAdmin()) {
                    new StageIdCommand(sendBotMessageService, eventService, user).execute(update);
                } else {
                    user.setStage(NONE);
                    userService.saveUser(user);
                    sendBotMessageService.sendMessage(user.getChatId(), "Команда доступна только администратору!");
                }
                break;
            }
        }
    }
}
