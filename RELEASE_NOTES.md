## 1.1.0

Fixed the stage of selecting events and entering parameters. Now there is no need to specify /back to exit the parameter input mode, you can continue working with the bot by specifying any other command.

## 1.0.0

The main commands available to the user are implemented:
* /start - Starts/resumes working with the bot
* /stop - Suspends work with the bot
* /help - Displays a list of commands
* /name - Allows you to add/change a name
* /today - Displays a list of events on the current day
* /date - Displays a list of events on the specified day
* /subscribe - Enables daily notifications
* /unsubscribe - Disables daily notifications

After the /name, /date commands, as well as after displaying the list of events, the user is given the opportunity to enter parameters without specifying the command. 

After displaying the list of events, the user can specify the event number and get a detailed description.
***
The basic commands available to the administrator are implemented:
* /admin - Starts administrator verification by entering a password
* /ahelp - Displays a list of commands available to the administrator
* /statistical - Displays information about users: total number of users, number of active users, number of active subscriptions
* /id - Displays information about the event by id

* After displaying the list of events, when specifying: '№event correct', the administrator becomes available the correction mode of this record, where it is possible to change the main parameters (date, title, text, image), as well as sting it when using 'delete'