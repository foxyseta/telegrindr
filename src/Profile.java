import java.util.EnumMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.objects.Location;

enum Stat {

    AGE("yo"), HEIGHT("cm"), WEIGHT("kg");

    Stat(String uom) {
        this.uom = uom;
    }

    public String toString() {
        return uom;
    }

    String uom;

}

public class Profile {

    public static final String DEFAULTEMOJI = "😀",
                               USERIDREGEX = "[a-z0-9]{5,32}",
                               EMOJIREGEX = "([\\u20a0-\\u32ff\\ud83c"
                                          + "\\udc00-\\ud83d\\udeff\\udbb9"
                                          + "\\udce5-\\udbb9\\udcee])";
    public static final Pattern USERIDPATTERN = Pattern.compile(USERIDREGEX),
                                EMOJIPATTERN = Pattern.compile(EMOJIREGEX);

    public Profile(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException(
                "Profile.setId: " + id + " is negative");
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (!USERIDPATTERN.matcher(userId).matches())
            throw new IllegalArgumentException(
                "Profile.setId: Telegram User IDs must follow " + USERIDREGEX);
        this.userId = userId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        if (!EMOJIPATTERN.matcher(emoji).matches())
            throw new IllegalArgumentException(
                "Profile.setEmoji: " + emoji + " is no emoji");
        this.emoji = emoji;
    }

    // TODO stats, hashtags

    public Location location;

    int id;
    String userId, emoji = DEFAULTEMOJI;
    EnumMap<Stat, Integer> stats = new EnumMap<Stat, Integer>(Stat.class);
    TreeSet<String> hashtags = new TreeSet<String>();
}
