package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public final static String START_MESSAGE_NEW_USER = "Привет! Давай знакомиться. Меня зовут БОТер Ⅰ Великий.\n" +
            "Я очень люблю историю и буду рад делиться своими знаниями с тобой \uD83D\uDE09\n" +
            "Как я могу обращаться к тебе?";
    public final static String START_MESSAGE_OLD_USER = "Привет! Тебе было без меня скучно? " +
            "Я рад, твоему возвращению \uD83D\uDE00" +
            "\nНапомнить, что я умею? Жми /help или меню слева";
    public final static String START_MESSAGE_FAIL = "Ты же уже со мной \uD83D\uDE42 " +
            "Введи другую команду или воспользуйся /help";

    public StartCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null) {
            user = new User(chatId);
            userService.saveUser(user);
            sendBotMessageService.sendMessage(chatId, START_MESSAGE_NEW_USER);
        } else {
            if(user.isActive()) {
                sendBotMessageService.sendMessage(chatId, START_MESSAGE_FAIL);
            } else {
                user.setActive(true);
                sendBotMessageService.sendMessage(chatId, START_MESSAGE_OLD_USER);
            }
        }
        userService.saveUser(user);
    }
}
