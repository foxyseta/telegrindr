import org.telegram.abilitybots.api.bot.AbilityBot;

public class Macinino extends AbilityBot {

    public static String USERNAME = "MacininoBot";

    public Macinino(String botToken, int creatorId) {
        super(botToken, USERNAME);
        cId = creatorId;
    }

    @Override
    public int creatorId() {
        return cId;
    }

    private int cId;

}