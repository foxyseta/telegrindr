package io.github.foxyseta.telegrindr;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This class features the application's entry point.
 * 
 * @see #main
 * @author FoxySeta
 * @version 1.0
 * @since 1.0
 */
public class App {
    
    /**
     * The application's entry point. It instantiates a single <code>TeleGrindr
     * </code>.
     * 
     * @see io.github.foxyseta.telegrindr.bot
     * @param args Contains the bot's token, username and creator's ID.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new io.github.foxyseta.telegrindr.bot.TeleGrindr(
                            args[0], args[1],
                            Integer.parseInt(args[2])));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Main: needs a bot token, a bot username,"
                               + " and a creator ID (all passed as arguments)");
        } catch (NumberFormatException e) {
            System.out.println("Main: the creator ID should be an integer");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}