package bot.data;

import java.util.Collections;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.objects.Location;

public class Profile implements java.io.Serializable {

    final public static String DEFAULTEMOJI = "😀",
                               EMOJIREGEX = "([\\u20a0-\\u32ff\\ud83c"
                                          + "\\udc00-\\ud83d\\udeff\\udbb9"
                                          + "\\udce5-\\udbb9\\udcee])";
    final public static Pattern EMOJIPATTERN = Pattern.compile(EMOJIREGEX);

    public Location location, lastSentLocation;

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
    
	private static final long serialVersionUID = 4124174882150000215L;
    private int id;
    private String emoji = DEFAULTEMOJI;
    private EnumMap<Stat, Integer> stats = new EnumMap<Stat, Integer>(Stat.class);
    private TreeSet<String> tags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

}