package bot;

import org.telegram.abilitybots.api.bot.AbilityBot;

public class Macinino extends AbilityBot {

    public Macinino(String botToken, String username, int creatorId) {
        super(botToken, username);
        cId = creatorId;
    }

    @Override
    public int creatorId() {
        return cId;
    }

    private int cId;

}