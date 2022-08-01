package HistoricalEventsBotApi.bot;

import HistoricalEventsBotApi.service.MessageReceiverService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Bot extends TelegramLongPollingBot {

    private final Logger log = Logger.getLogger(Bot.class);

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

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
        MessageReceiverService messageReceiver = new MessageReceiverService(update);
        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("ReceivedThread");
        receiver.setPriority(10);
        receiver.start();
    }
}
