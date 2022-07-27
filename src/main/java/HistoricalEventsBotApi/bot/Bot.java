package HistoricalEventsBotApi.bot;

import HistoricalEventsBotApi.command.CommandContainer;
import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.command.stage.StageDefinition;
import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.IndexingSiteUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;

import static HistoricalEventsBotApi.command.CommandName.*;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private static final String COMMAND_PREFIX = "/";
    private final CommandContainer commandContainer;
    private final UserService userService;
    private final StageDefinition stageDefinition;


    public Bot(UserService userService, EventService eventService, IndexingSiteUtil indexingSiteUtil,
               Config config) {
        this.commandContainer = new CommandContainer(new SendBotMessageService(this),
                userService, eventService, indexingSiteUtil, config);
        this.stageDefinition = new StageDefinition(new SendBotMessageService(this), userService);
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            User user = userService.getUser(update.getMessage().getChatId().toString());
            if (user != null && user.getStage() != Stage.NONE) {
                if(user.getName() == null) {
                    user.setName("Стесняшка");
                }
                if(LocalTime.now().isAfter(user.getStageTime().plusMinutes(5))) {
                    user.setStage(Stage.NONE);
                    user.setStageTime(LocalTime.now());
                    userService.saveUser(user);
                    onUpdateReceived(update);
                } else {
                    userService.saveUser(user);
                    stageDefinition.definition(user,update);
                }
            } else {
                if (message.startsWith(COMMAND_PREFIX)) {
                    String commandIdentifier = message.split("\\s+")[0].toLowerCase();
                    commandContainer.retrieveCommand(commandIdentifier, user).execute(update);
                } else {
                    commandContainer.retrieveCommand(NO.getCommandName(), user).execute(update);
                }
            }
        }
    }

}
