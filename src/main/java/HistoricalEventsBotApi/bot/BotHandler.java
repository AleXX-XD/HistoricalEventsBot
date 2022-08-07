package HistoricalEventsBotApi.bot;

import HistoricalEventsBotApi.command.CommandContainer;
import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.command.stage.StageDefinition;
import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

import static HistoricalEventsBotApi.command.CommandName.NO;

@Component
public class BotHandler {

    private static final String COMMAND_PREFIX = "/";
    private static CommandContainer commandContainer;
    private static UserService userService;
    private static StageDefinition stageDefinition;


    @Autowired
    public BotHandler(Bot bot, Config config, UserService userService, EventService eventService) {
        BotHandler.commandContainer = new CommandContainer(new SendBotMessageService(bot),
                userService, eventService);
        BotHandler.stageDefinition = new StageDefinition(new SendBotMessageService(bot), userService, eventService, config);
        BotHandler.userService = userService;
    }

    public static void distribution(Update update) {
       if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            User user = userService.getUser(update.getMessage().getChatId().toString());
           if (message.startsWith(COMMAND_PREFIX)) {
               if(user != null)  {
                   user.setStage(Stage.NONE);
                   userService.saveUser(user);
               }
               commandContainer.retrieveCommand(message, user).execute(update);
           } else {
               if (user != null && user.getStage() != Stage.NONE) {
                   if(user.getName() == null && user.getStage() != Stage.STAGE_NAME) {
                       user.setName("Человек без имени");
                   }
                   if(LocalDateTime.now().isAfter(user.getStageTime().plusMinutes(5))) {
                       user.setStage(Stage.NONE);
                       userService.saveUser(user);
                       distribution(update);
                   } else {
                       userService.saveUser(user);
                       stageDefinition.definition(user,update);
                   }
               } else {
                   commandContainer.retrieveCommand(NO.getCommandName(), user).execute(update);
               }
           }
        }
    }
}
