package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UserService userService;

    public final static String START_MESSAGE_NEW_USER = "Привет! Я бот, который интересуется историей.\n" +
            "Если хочешь получать информацию об исторических событиях, то оставайся со мной, мы подружимся \uD83D\uDE09" +
            "\nЧтобы ознакомиться с моими возможностями, жми /help";
    public final static String START_MESSAGE_OLD_USER = "Привет! Тебе было без меня скучно? " +
            "Я рад, что ты вернулся \uD83D\uDE00" +
            "\nНапомнить, что я умею? Жми /help";
    public final static String START_MESSAGE_FAIL = "Ты наверное ошибся командой? Ты же уже со мной \uD83D\uDE42 " +
            "Попробуй еще раз или воспользуйся /help";

    public StartCommand(SendBotMessageService sendBotMessageService, UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.findByChatId(chatId);
        if(user == null) {
            user = new User(chatId,true);
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
