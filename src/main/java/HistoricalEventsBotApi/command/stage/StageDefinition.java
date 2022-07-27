package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.*;
import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.IndexingSiteUtil;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static HistoricalEventsBotApi.command.stage.Stage.*;

@Component
public class StageDefinition {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public StageDefinition(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
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
        }
    }
}
