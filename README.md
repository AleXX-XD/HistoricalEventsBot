## Overview
A telegram bot that displays information about events that occurred on a specified day in history. It is possible to connect daily notifications.

The main commands available to the user:
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
The basic commands available to the administrator:
* /admin - Starts administrator verification by entering a password
* /ahelp - Displays a list of commands available to the administrator
* /statistical - Displays information about users: total number of users, number of active users, number of active subscriptions
* /id - Displays information about the event by id

* After displaying the list of events, when specifying: '№event correct', the administrator becomes available the correction mode of this record, where it is possible to change the main parameters (date, title, text, image), as well as sting it when using 'delete'

###Technology Stack:
* Java
* Spring Boot
* Spring Data Jpa
* PostgreSQL
* Jsoup
* Json
* Log4j
* Docker

## Building
...

## Troubleshooting
...

## Release Notes
Can be found in [RELEASE_NOTES](RELEASE_NOTES.md).

## Authors
* Aleksey Dyakov - [AleXX-XD](https://github.com/AleXX-XD)

## Acknowledgments
...

## Contributing
Please, follow [Contributing](CONTRIBUTING.md) page.

## Code of Conduct
Please, follow [Code of Conduct](CODE_OF_CONDUCT.md) page.

## License
This project is Apache License 2.0 - see the [LICENSE](LICENSE) file for details
