import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    
    public static void main(String[] args) // args = [botToken, creatorId]
    {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new Macinino(args[0], Integer.parseInt(args[1])));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Main: needs a bot token and a creator ID"
                               + " (both passed as arguments)");
        } catch (NumberFormatException e) {
            System.out.println("Main: the creator ID should be an integer");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}