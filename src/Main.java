import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    
    // args = [botToken, botUsername, creatorId]
    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new bot.TeleGrindr(args[0], args[1],
                                             Integer.parseInt(args[2])));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Main: needs a bot token, a bot username,"
                               + " and a creator ID (both passed as arguments)");
        } catch (NumberFormatException e) {
            System.out.println("Main: the creator ID should be an integer");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}