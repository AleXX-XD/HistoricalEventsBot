package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.bot.Bot;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;

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

    public void sendPhoto(String chatId, String path){
        if(path != null) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            InputFile inputFile = new InputFile();
            try {
                inputFile.setMedia(new URL(path).openStream(),"img");
            }
            catch (IOException ex) {
                log.error("Изображение не доступно по ссылке : " + path);
            }
            sendPhoto.setPhoto(inputFile);
            try{
                bot.execute(sendPhoto);
            } catch (TelegramApiException e) {
                log.warn("Ошибка отправки сообщения: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
