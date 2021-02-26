# TeleGrindr
![Logo](logo/logo.png)

## Introduction
A [Telegram](https://www.github.com/telegramdesktop) bot to bring a
[Grindr](https://www.grindr.com)-like experience to your groups. Except for
the freemium policy, that is.

## Features
* `/start` asks the bot to introduce itself.
* `/help` gets a description of the usage of TeleGrindr's characterizing
  commands (not the ones related to basic bot administration). 
* `/iam {arguments}` helps a group member to set up their profile, and prints
  the updated data. If you send a location to other users, TeleGrindr will set
  that as your current position.
* `/howis @username` can get the profile of any user in the group.
* `/whois {arguments}` returns a list of all the users. You can use many
  filters based on your needs.
The bot also inherits the [standard commands](https://github.com/rubenlagus/TelegramBots/blob/b03fe98798192840402168e6e422d1b4cee48279/TelegramBots.wiki/abilities/Simple-Example.md#testing-your-bot) defined by the
[Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots)'s
`AbilityBot`. These features are also useful for basic bot administration.

## User setup

### Talking to BotFather
After asking BotFather to create a `/newbot`, you should:
1. `/setname` (you may use "TeleGrindr")
1. `/setdescription` (you may use [this](#Introduction))
1. `/setabouttext` (you may use "The crossover nobody was waiting for.")
1. `/setuserpic` (you may use [this](./logo/logo.png))
1. `/setcommands`
> start - short introduction<br />
> help - prints a short guide<br />
> iam - edits your profile<br />
> howis - displays a profile<br />
> whois - searches for profiles<br />
> commands - lists every command<br />
> ban - bans a user from using this bot (admin)<br />
> unban - lifts the ban from the user (admin)<br />
> promote - promotes user to bot admin (admin)<br />
> demote - demotes bot admin to user (admin)<br />
> stats - shows bot stats (admin)<br />
> claim - claims this bot (creator)<br />
> backup - backups the bot's database (creator)<br />
> recover - recovers the bot's database (creator)<br />
> report - lists only TeleGrindr-specific commands (creator)<br />

### Prerequisites
* any Java Development Kit

### Dependencies
* [Telegram Ability Bot](https://mvnrepository.com/artifact/org.telegram/telegrambots-abilities)
* [SLF4J NOP Binding](https://mvnrepository.com/artifact/org.slf4j/slf4j-nop)

### Usage

#### Without Docker
After building the project using [`Main`](./src/Main.java#L13) as your main
class, you can launch it. Your environment needs to provide three arguments:
1. the bot's token
1. the bot's username
1. the bot's creator's identifier<br />
For example, you got a single executable JAR file named `telegrindr.jar` in
your current directory, you can run it from Bash like this:
```bash
$ java -jar telegrindr.jar 123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11 telegrindrBot 1234567890
```
#### With Docker
After building the project using [`Main`](./src/Main.java#L13) as your main
class, you should have a single executable JAR file named `telegrindr.jar` in
the parent directory. You can build your image like this:
```bash
$ docker build docker
    -e BOT_TOKEN=$BOT_TOKEN
    -e BOT_USERNAME=$BOT_USERNAME
    -e BOT_CREATOR_ID=$BOT_CREATOR_ID
```