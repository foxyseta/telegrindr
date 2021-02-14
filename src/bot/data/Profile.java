package bot.data;

import java.util.Collections;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.User;

public class Profile implements java.io.Serializable {

    final public static String DEFAULTEMOJI = "😀",
                               EMOJIREGEX = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
    final public static Pattern EMOJIPATTERN = Pattern.compile(EMOJIREGEX);

    public User user;
    public Location location;

    public Profile(User user) {
        this.user = user;
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

    public boolean containsStat(Stat key) {
        return stats.containsKey(key);
    }

    public Integer getStat(Stat key) {
        return stats.get(key);
    }

    public Integer putStat(Stat key, Integer value) {
        if (key.validate(value))
            return stats.put(key, value);
        throw new IllegalArgumentException(
            "Profile.putStat: " + value + " " + key.uom() +
            " is no valid stat");
    }
    
    public Integer removeStat(Stat key) {
        return stats.remove(key);
    }
    
    public boolean containsTag(String tag) {
        return tags.contains(tag);
    }

    public SortedSet<String> unmodifiableTags() {
        return Collections.unmodifiableSortedSet(tags);
    }
    
    public boolean addTag(String tag) {
        return tags.add(tag);
    }
    
    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public String toString() {
        String res = String.format(
            "%s **%s%s** @%s %s%n💬 %s%n",
            emoji, user.getFirstName(),
            user.getLastName() == null ? "" : " " + user.getLastName(),
            user.getUserName(), emoji, user.getLanguageCode());
        if (!stats.isEmpty())
            res += "📋";
        for (Entry<Stat, Integer> entry : stats.entrySet())
            res += " " + entry.getValue() + entry.getKey().uom();
        res += String.format("%n");
        for (String tag : tags)
            res += "#" + tag + " ";
        return res;
    }

    public String toShortString() {
        return String.format("%s @%s", emoji, user.getUserName());
    }

	private static final long serialVersionUID = 4124174882150000215L;
    private String emoji = DEFAULTEMOJI;
    private EnumMap<Stat, Integer> stats = new EnumMap<Stat, Integer>(Stat.class);
    private TreeSet<String> tags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

}