package bot.data;

import java.util.Collections;
import java.util.EnumMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * A <code>Profile</code> defines all of the pieces of information concerning
 * a single {@link User}.
 * 
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */
public class Profile implements java.io.Serializable {

    /** The emoji used by default in a new {@link Profile}. */
    final public static String DEFAULTEMOJI = "😀";
    /** The regex representing an emoji (or another symbol). */
    final public static String EMOJIREGEX = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
    /**
     * The {@link Pattern} generated from the {@linkplain #EMOJIREGEX emoji
     * regex}.
     */
    final public static Pattern EMOJIPATTERN = Pattern.compile(EMOJIREGEX);

    /** The {@link User} who set up this {@link Profile}. */
    public User user;
    /** The {@link Location} of the user. */
    public Location location;

    /**
     * Instantiates a new {@link Profile}.
     * 
     * @param user The user represented by this {@link Profile}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Profile(User user) {
        this.user = user;
    }

    /**
     * Gets the current emoji.
     *
     * @see #emoji
     * @return The emoji used by this {@link Profile}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Sets a new emoji.
     * 
     * @see #emoji
     * @param emoji The new emoji.
     * @throws IllegalArgumentException The argument is not an emoji.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public void setEmoji(String emoji) {
        if (!EMOJIPATTERN.matcher(emoji).matches())
            throw new IllegalArgumentException(
                "Profile.setEmoji: " + emoji + " is no emoji");
        this.emoji = emoji;
    }

    /**
     * Checks if the {@link Profile} has already a value associated to a
     * certain {@link Stat}.
     * @param key The {@link Stat} to search for.
     * @return <code>true</code> if a value has been set, <code>false</code>
     * otherwise.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean containsStat(Stat key) {
        return stats.containsKey(key);
    }

    /**
     * Gets the value associated to a certain {@link Stat} if present.
     * 
     * @param key The {@link Stat} to search for.
     * @return The value associated to <code>key</code>, or <code>null</code>
     * if not present.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Integer getStat(Stat key) {
        return stats.get(key);
    }

    /**
     * Sets a new value for a certain {@link Stat}, but only if such a value is
     * adeguate.
     * 
     * @param key The {@link Stat} to which the new value will be associated.
     * @param value The new value to set.
     * @return The old value, or <code>null</code> if not present.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Integer putStat(Stat key, Integer value) {
        if (key.validate(value))
            return stats.put(key, value);
        return stats.get(key);
    }

    /**
     * Removes the value associated to a certain {@link Stat} if present.
     * 
     * @param key The {@link Stat} whose value is to be deleted.
     * @return The old value, or <code>null</code> if not present. 
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Integer removeStat(Stat key) {
        return stats.remove(key);
    }
    
    /**
     * Checks whether a certain tag was added to the {@link Profile}.
     * 
     * @param tag The tag to search for.
     * @return <code>true</code> if the tag if found, <code>false</code>
     * otherwise.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean containsTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Gets an unmodifiable clone of the collection containing the {@link
     * #tags}.
     *
     * @return The unmodifiable clone.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public SortedSet<String> unmodifiableTags() {
        return Collections.unmodifiableSortedSet(tags);
    }
    
    /**
     * Adds a new tag to the {@link Profile} if not already present.
     * 
     * @param tag The tag to be added.
     * @return <code>true</code> if the tag was added, <code>false</code> if
     * the tag was already present.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean addTag(String tag) {
        return tags.add(tag);
    }
    
    /**
     * Removes a tag from the {@link Profile} if present.
     * 
     * @param tag The tag to be removed.
     * @return <code>true</code> if the tag was removed, <code>false</code> if
     * the tag was not present to begin with.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    /**
     * A string representation of the {@link Profile}.
     * 
     * @return The string representation.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public String toString() {
        String res = String.format(
            "%s *%s%s* @%s %s%n💬 %s%n",
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

    /**
     * A shorter string representation of the {@link Profile}.
     * 
     * @return The short string representation.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public String toShortString() {
        return String.format("%s @%s", emoji, user.getUserName());
    }

    /** This {@link java.io.Serializable}'s version number. */
	private static final long serialVersionUID = 4124174882150000215L;
    /** The emoji used to decorate this {@link Profile}. */
    private String emoji = DEFAULTEMOJI;
    /** The numeric parameters set by the user. */
    private EnumMap<Stat, Integer> stats = new EnumMap<Stat, Integer>(Stat.class);
    /** The tags set by the user. */
    private TreeSet<String> tags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

}