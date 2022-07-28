package HistoricalEventsBotApi.bot;

import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.MessageReciever;
import HistoricalEventsBotApi.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class Bot extends TelegramLongPollingBot {

    private final Logger log = Logger.getLogger(Bot.class);

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    public static final Queue<Update> receiveQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    public Bot() {
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
        MessageReciever messageReciever = new MessageReciever(update);
        Thread receiver = new Thread(messageReciever);
        receiver.setDaemon(true);
        receiver.setName("ReceivedThread");
        receiver.setPriority(10);
        receiver.start();
    }

}
