package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StageNameCommand implements Command {

    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final User user;
    private static final String STAGE_NAME_NEW = "Рад познакомиться с тобой <b>%s</b> \uD83D\uDC4B";
    private static final String STAGE_NAME_OLD = "Приветствую тебя <b>%s</b> \uD83D\uDC4B";
    private static final String STAGE_NAME_FAIL = "Такое имя я не выговорю \uD83E\uDD14 " +
            "Давай, что-нибудь попроще, состоящее только из букв или жми /back, для отмены";
    private static final String STAGE_NAME_SLASH = "Я жду твое имя, а не команду \uD83D\uDE01 " +
            "Используй пожалуйста только буквы или жми /back для отмены";
    private static final String STAGE_NAME_BACK = "Хорошо, <b>%s</b> переходим к интересностям \uD83D\uDE43 " +
            "Выбери команду в меню или нажми /help, для отображения моих возможностей";
    private static final String STAGE_NAME = "Переходим к интересностям \uD83D\uDE43 " +
            "Выбери команду в меню или нажми /help, для отображения моих возможностей";

    public StageNameCommand(SendBotMessageService sendBotMessageService, UserService userService, User user) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.user = user;
    }

    public void execute (Update update){
        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            if (text.equals("/back")) {
                user.setStage(Stage.NONE);
                if(user.getName() == null) {
                    user.setName("Скромняшка");
                }
                userService.saveUser(user);
                sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_NAME_BACK, user.getName()));
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_NAME_SLASH);
            }
        } else {
            if(text.matches("[a-zA-Zа-яА-ЯёЁ\\s-_]+")) {
                if(user.getName() == null) {
                    user.setName(text);
                    user.setStage(Stage.NONE);
                    sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_NAME_NEW, user.getName()));
                    sendBotMessageService.sendMessage(user.getChatId(), STAGE_NAME);
                } else {
                    user.setName(text);
                    user.setStage(Stage.NONE);
                    sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_NAME_OLD, user.getName()));
                    sendBotMessageService.sendMessage(user.getChatId(), STAGE_NAME);
                }
                userService.saveUser(user);
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_NAME_FAIL);
            }
        }
    }
}
