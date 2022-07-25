package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.bot.Bot;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class SendBotMessageService {

    private final Logger log = Logger.getLogger(SendBotMessageService.class);
    private final Bot bot;

    public SendBotMessageService(Bot bot) {
        this.bot = bot;
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Ошибка отправки сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
