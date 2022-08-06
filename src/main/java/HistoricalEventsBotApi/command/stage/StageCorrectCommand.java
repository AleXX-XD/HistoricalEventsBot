package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.command.admin.AdminAnnotation;
import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.EventService;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import HistoricalEventsBotApi.util.GettingEvents;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.meta.api.objects.Update;

@AdminAnnotation
public class StageCorrectCommand implements Command {

    private final Logger log = Logger.getLogger(StageCorrectCommand.class);
    private final UserService userService;
    private final EventService eventService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;

    public final static String CORRECT_FAIL = "Не верно указана команда!!! Параметры : date, title, text, image или delete для удаления записи\n" +
            "Формат: параметр!-!значение\n" +
            "Пример: date!-!4/25 , image!-!https://url , text!-!Текст";

    public StageCorrectCommand(SendBotMessageService sendBotMessageService, UserService userService, EventService eventService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.eventService = eventService;
        this.user = user;
    }

    @Override
    public void execute(Update update) {
        String text = update.getMessage().getText();
        String[] splitText = text.split("!-!");
        Event event = getEvent(user.getCurrentEvents());
        if (event != null) {
            switch (splitText[0]) {
                case "date": {
                    if (splitText[1].matches("\\d{1,2}/\\d{1,2}")) {
                        event.setDate(splitText[1]);
                        eventService.saveEvent(event);
                        sendBotMessageService.sendMessage(user.getChatId(), "Дата обновлена");
                        log.info("Запись с id = '" + event.getLink() + "' - изменена дата администратором '" + user.getName() + " / " + user.getChatId() + "'");
                    } else {
                        sendBotMessageService.sendMessage(user.getChatId(), "Дата указана не верно");
                    }
                    break;
                }
                case "title": {
                    event.setTitle(splitText[1]);
                    eventService.saveEvent(event);
                    sendBotMessageService.sendMessage(user.getChatId(), "Заголовок обновлен");
                    log.info("Запись с id '" + event.getLink() + "' - изменен заголовок администратором '" + user.getName() + " / " + user.getChatId() + "'");
                    break;
                }
                case "text": {
                    event.setText(splitText[1]);
                    eventService.saveEvent(event);
                    sendBotMessageService.sendMessage(user.getChatId(), "Текст обновлен");
                    log.info("Запись с id = '" + event.getLink() + "' - изменен текст администратором '" + user.getName() + " / " + user.getChatId() + "'");
                    break;
                }
                case "image": {
                    if (splitText[1].matches("https?://.+")) {
                        event.setImg(splitText[1]);
                        eventService.saveEvent(event);
                        sendBotMessageService.sendMessage(user.getChatId(), "Ссылка на картинку обновлена");
                        log.info("Запись с id = '" + event.getLink() + "' - изменена ссылка на картинку администратором '" + user.getName() + " / " + user.getChatId() + "'");
                    } else {
                        sendBotMessageService.sendMessage(user.getChatId(), "Ссылка указана не верно");
                    }
                    break;
                }
                case "delete": {
                    log.info("Запись с id = '" + event.getLink() + "' удалена администратором '" + user.getName() + " / " + user.getChatId() + "'");
                    eventService.deleteEvent(event);
                    sendBotMessageService.sendMessage(user.getChatId(), "Запись удалена");
                    user.setStage(Stage.NONE);
                    userService.saveUser(user);
                    break;
                }
                default:
                    sendBotMessageService.sendMessage(user.getChatId(), CORRECT_FAIL);
            }
        } else {
            sendBotMessageService.sendMessage(user.getChatId(), "Запись не найдена");
            user.setStage(Stage.NONE);
            userService.saveUser(user);
        }
    }

    private Event getEvent(String json) {
        try {
            JSONObject object = GettingEvents.stringToJson(json);
            return eventService.getEvent((String) object.get("link"));
        } catch (ParseException ex) {
            log.warn("Ошибка обработки Json из БД : " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
