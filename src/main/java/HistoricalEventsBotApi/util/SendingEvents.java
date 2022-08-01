package HistoricalEventsBotApi.util;

import HistoricalEventsBotApi.command.stage.Stage;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SendingEvents {

    private final Logger log = Logger.getLogger(SendingEvents.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;

    public SendingEvents(UserService userService, SendBotMessageService sendBotMessageService){
        this.userService = userService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Scheduled(cron = "${bot.interval-in-cron}")
    public void sendEvents() {
        List<User> userList = userService.getSubUsers();
        JSONObject eventsObject = GettingEvents.getEventMessage(LocalDate.now());
        String message = GettingEvents.getContent(eventsObject);
        for(User user : userList) {
            user.setStage(Stage.STAGE_EVENTS);
            userService.saveUser(user);
            String SENDING_MESSAGE = "Привет, %s! Я к тебе с ежедневной рассылкой \uD83D\uDE09";
            sendBotMessageService.sendMessage(user.getChatId(), String.format(SENDING_MESSAGE, user.getName()));
            sendBotMessageService.sendMessage(user.getChatId(), message);
        }
        log.info("Рассылка событий на сегодняшнюю дату прошла успешно!");
    }
}
