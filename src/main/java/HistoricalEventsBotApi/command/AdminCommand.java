package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.annotation.AdminAnnotation;
import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminCommand implements Command{

    private final Logger log = Logger.getLogger(AdminCommand.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final Config config;

    public final static String ADMIN_MESSAGE = "Смотрите-ка, кто пожаловал! Милости прошу..., вот только после ввода пароля \uD83E\uDD28\n" +
            "Пиши: /simsaladmin PASSWORD";
    public final static String ADMIN_MESSAGE_TRUE = "Приветсвую тебя, Админ-Господин! \uD83D\uDE0E\n" +
            "Теперь тебе доступно меню администратора. Жми /help, чтобы ознакомиться с ним.";
    public final static String ADMIN_MESSAGE_FALSE = "Не верный пароль! \uD83E\uDD28 А ты точно тот, за кого себя выдаешь?";
    public final static String ADMIN_MESSAGE_FAIL = "Упс... Не верная команда \uD83D\uDE15 Попробуй еще раз.\n" +
            "Шаблон: /simsaladmin PASSWORD";

    public AdminCommand(SendBotMessageService sendBotMessageService, UserService userService, Config config) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.config = config;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.getUser(chatId);
        if(user == null || !user.isActive()) {
            new UnknownCommand(sendBotMessageService).execute(update);
        } else {
            String[] textList = update.getMessage().getText().trim().split("\\s+");
            switch (textList.length) {
                case 1: {
                    sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE);
                    break;
                }
                case 2: {
                    if(textList[1].equals(config.getAdminPassword())) {
                        user.setAdmin(true);
                        userService.saveUser(user);
                        sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE_TRUE);
                    } else {
                        sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE_FALSE);
                    }
                    break;
                }
                default: {
                    sendBotMessageService.sendMessage(chatId, ADMIN_MESSAGE_FAIL);
                    break;
                }
            }
        }
    }

}
