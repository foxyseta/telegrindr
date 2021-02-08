import org.telegram.abilitybots.api.bot.AbilityBot;

public class Macinino extends AbilityBot {

    public static String USERNAME = "MacininoBot";

    protected Macinino(String botToken, int creatorId) {
        super(botToken, USERNAME);
        cId = creatorId;
    }

    @Override
    public int creatorId() {
        return cId;
    }

    protected int cId;

}