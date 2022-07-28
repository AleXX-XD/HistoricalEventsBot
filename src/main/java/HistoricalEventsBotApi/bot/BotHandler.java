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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalTime;

import static HistoricalEventsBotApi.command.CommandName.NO;

@Component
public class BotHandler {

    private static final Logger log = Logger.getLogger(BotHandler.class);
    private static final String COMMAND_PREFIX = "/";
    private static CommandContainer commandContainer;
    private static UserService userService;
    private static StageDefinition stageDefinition;


    @Autowired
    public BotHandler(Bot bot, Config config, UserService userService, EventService eventService) {
        BotHandler.commandContainer = new CommandContainer(new SendBotMessageService(bot),
                userService, eventService, config);
        BotHandler.stageDefinition = new StageDefinition(new SendBotMessageService(bot), userService);
        BotHandler.userService = userService;
    }

    public static void distribution(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            User user = userService.getUser(update.getMessage().getChatId().toString());
            if (user != null && user.getStage() != Stage.NONE) {
                if(user.getName() == null) {
                    user.setName("Стесняшка");
                }
                if(LocalTime.now().isAfter(user.getStageTime().plusMinutes(2))) {
                    user.setStage(Stage.NONE);
                    user.setStageTime(LocalTime.now());
                    userService.saveUser(user);
                    distribution(update);
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
