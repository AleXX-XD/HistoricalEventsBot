package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.bot.Bot;
import HistoricalEventsBotApi.bot.BotHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageReciever implements Runnable {

    private final Update update;

    public MessageReciever(Update update) {
        this.update = update;
    }

    @Override
    public void run() {
        BotHandler.distribution(update);
    }
}
