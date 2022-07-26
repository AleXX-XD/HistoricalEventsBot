package HistoricalEventsBotApi.command;

import HistoricalEventsBotApi.command.admin.*;
import HistoricalEventsBotApi.model.User;
import HistoricalEventsBotApi.service.SendBotMessageService;
import HistoricalEventsBotApi.service.UserService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static HistoricalEventsBotApi.command.CommandName.*;
import static java.util.Objects.nonNull;

@Component
public class CommandContainer {

    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;

    @Autowired
    public CommandContainer(SendBotMessageService sendBotMessageService, UserService userService) {

        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, userService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, userService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(ADMIN.getCommandName(), new AdminCommand(sendBotMessageService, userService))
                .put(TODAY.getCommandName(), new TodayCommand(sendBotMessageService, userService))
                .put(DATE.getCommandName(), new DateCommand(sendBotMessageService, userService))
                .put(SUBSCRIBE.getCommandName(), new SubscribeCommand(sendBotMessageService, userService))
                .put(UNSUBSCRIBE.getCommandName(), new UnsubscribeCommand(sendBotMessageService, userService))
                .put(NAME.getCommandName(), new NameCommand(sendBotMessageService, userService))
                .put(UPDATE.getCommandName(), new UpdateEventsCommand(sendBotMessageService, userService))
                .put(STATISTIC.getCommandName(), new StatisticCommand(sendBotMessageService, userService))
                .put(ALL_USERS.getCommandName(), new AllUsersCommand(sendBotMessageService, userService))
                .put(AHELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier, User user) {
        Command orDefault = commandMap.getOrDefault(commandIdentifier, unknownCommand);
        if(isAdminCommand(orDefault)) {
            if(user != null && user.isAdmin()) {
                return orDefault;
            } else {
                return unknownCommand;
            }
        }
        return orDefault;
    }

    private boolean isAdminCommand(Command command) {
        return nonNull(command.getClass().getAnnotation(AdminAnnotation.class));
    }
}
