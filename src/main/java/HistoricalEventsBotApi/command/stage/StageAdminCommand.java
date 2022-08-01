package HistoricalEventsBotApi.command.stage;

import HistoricalEventsBotApi.command.Command;
import HistoricalEventsBotApi.config.Config;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import org.jboss.logging.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class StageAdminCommand implements Command {

    private final Logger log = Logger.getLogger(StageAdminCommand.class);
    private final UserService userService;
    private final SendBotMessageService sendBotMessageService;
    private final Config config;
    private final User user;

    public final static String STAGE_ADMIN_TRUE = "Приветсвую тебя, Админ-Господин! \uD83D\uDE0E\n" +
            "Теперь тебе доступно меню администратора. Жми /ahelp, чтобы ознакомиться с ним.";
    public final static String STAGE_ADMIN_FALSE = "Не верный пароль! \uD83E\uDD28 А ты точно тот, за кого себя выдаешь?";
    public final static String STAGE_ADMIN_BACK = "Хорошо, <b>%s</b> \uD83D\uDC4C Вернул тебя к выбору команд.";
    private static final String STAGE_ADMIN_SLASH = "Я жду пароль, а не команду \uD83E\uDD28\n" +
            "Или вводи пароль или жми /back для возврата.";
    public final static String STAGE_ADMIN_MAILING = "Пользователю '%s / %s' предоставлен доступ администратора";

    public StageAdminCommand(SendBotMessageService sendBotMessageService, UserService userService, User user, Config config) {
        this.sendBotMessageService = sendBotMessageService;
        this.userService = userService;
        this.user = user;
        this.config = config;
    }

    @Override
    public void execute(Update update) {
        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            if (text.equals("/back")) {
                user.setStage(Stage.NONE);
                userService.saveUser(user);
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ADMIN_BACK);
            } else {
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ADMIN_SLASH);
            }
        } else {
            if (text.equals(config.getAdminPassword())) {
                log.info("Пользователю '" + user.getName() + " / " + user.getChatId() + "' предоставлен доступ администратора");
                user.setStage(Stage.NONE);
                user.setAdmin(true);
                userService.saveUser(user);
                List<User> userList = userService.getAdminUsers();
                for (User u : userList) {
                    sendBotMessageService.sendMessage(user.getChatId(), String.format(STAGE_ADMIN_MAILING, u.getName(), u.getChatId()));
                }
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ADMIN_TRUE);
            } else {
                log.info("Попытка входа пользователя '" + user.getName() + " / " + user.getChatId() + "' с паролем: " + text);
                sendBotMessageService.sendMessage(user.getChatId(), STAGE_ADMIN_FALSE);
            }
        }
    }
}
