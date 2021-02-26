package io.github.foxyseta.telegrindr.bot.data;

import static java.lang.Math.*;

import java.util.EnumMap;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedSet;

import org.telegram.telegrambots.meta.api.objects.Location;

/**
 * A <code>Filter</code> can {@link #test} whether a {@link Profile}
 * can be of any interest to the user.
 *
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */ 
public class Filter implements Predicate<Profile> {
    
    /** The regex representing a {@link Stat} and its {@link Range}. */
    final public static String RANGEARGUMENTREGEX = "(\\d*)(,?)(\\d*)(\\w+)"; 
    /** The regex for any tag-related preference. */
    final public static String TAGARGUMENTREGEX = "([+-]?)#([0-9A-Za-z]+).*";
    /**
     * The unit of measurement for distances between one {@link Location} and
     * another.
     */
    final public static String DISTANCEUOM = "km";
    /** The prefix used for excluding a certain tag. */
    final public static String EXCLUDETAGPREFIX = "-"; 
    /**
     * The {@link Pattern} generated from the {@linkplain #RANGEARGUMENTREGEX
     * stat-range pairs' regex}.
     */
    final public static Pattern RANGEARGUMENTPATTERN =
        Pattern.compile(RANGEARGUMENTREGEX); 
    /**
     * The {@link Pattern} generated from the {@linkplain #TAGARGUMENTREGEX
     * tag-related preferences' regex}.
     */
    final public static Pattern TAGARGUMENTPATTERN =
        Pattern.compile(TAGARGUMENTREGEX);
    /** The average earth radius in kilometers. */
    final public static double EARTHRADIUS = 6371.005076123;
    /**
     * The {@link Location} from which all {@linkplain #distance distances}
     * are calculated.
     */
    public Location from;

    /**
     * Instantiates a new {@link Filter}.
     * 
     * @param arguments The users' preferences.
     * @param from See {@link #from}. 
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Filter(String[] arguments, Location from) {
        for (String argument : arguments)
            parse(argument);
        this.from = from;
    }

    /**
     * Checks a given {@link Profile}.
     * 
     * @param profile The {@link Profile} to be tested.
     * @return Whether the {@link Profile} passes the test (<code>true</code>)
     * or gets filtered out (<code>false</code>).
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    @Override
    public boolean test(Profile profile) {
        // stat filters
        for (Stat stat : Stat.values())
            if (statFilters.containsKey(stat) && (!profile.containsStat(stat)
                || !statFilters.get(stat).contains(profile.getStat(stat))))
                return false;
        // distance filter
        if (distanceFilter != null &&
            !distanceFilter.contains(distance(from, profile.location)))
            return false;
        // tags query
        final SortedSet<String> tags = profile.unmodifiableTags();
        if (!tags.containsAll(include))
            return false;
        for (String tag : exclude)
            if (tags.contains(tag))
                return false;
        return true;
    }
    
    /**
     * Checks whether a non-<code>null</code> location is needed for
     * executing {@linkplain #test tests}.
     * 
     * @see #from
     * @return <code>true</code> if the loction is needed, <code>false</code>
     * otherwise.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean isLocationNeeded() {
        return distanceFilter != null;
    }

    /**
     * Contains the {@linkplain #RANGEARGUMENTPATTERN stat-related
     * preferences}.
     */
    private EnumMap<Stat, Range<Integer>> statFilters =
        new EnumMap<Stat, Range<Integer>>(Stat.class);
    /**
     * Contains the {@linkplain #RANGEARGUMENTPATTERN distance-related
     * filters.
     */
    private Range<Double> distanceFilter;
    /** A whitelist for tags. */
    private HashSet<String> include = new HashSet<String>();
    /** A blacklist for tags. */
    private HashSet<String> exclude = new HashSet<String>();
    
    /**
     * Calculates the distance between two instances of {@link Location} using
     * the {@linkplain <a
     * href="https://rosettacode.org/wiki/Haversine_formula"> haversine
     * formula</a>}.
     *
     * @param l1 The first {@link Location}.
     * @param l2 The second {@link Location}.
     * @return The distance between <code>l1</code> and <code>l2</code> in
     * kilometers.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    private Double distance(Location l1, Location l2) {
        final double lat1 = l1.getLatitude(),
                     lon1 = l1.getLongitude(),
                     lat2 = l2.getLatitude(),
                     lon2 = l2.getLongitude(),
                     deltaLat = toRadians(lat2 - lat1),
                     deltaLon = toRadians(lon2 - lon1),
                     a = sin(deltaLat / 2) * sin(deltaLat / 2)
                       + cos(toRadians(lat1)) * cos(toRadians(lat2))
                       * sin(deltaLon / 2) * sin(deltaLon / 2),
                     c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return EARTHRADIUS * c;
    }

    /**
     * A new parser based on {@link java.lang.Integer#parseInt} which can also
     * handle empty strings.
     * @param s The string to be parsed.
     * @return The parsing result, or <code>null</code> if <code>s</code> is
     * empty.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    private Integer parseInt(String s) {
        return s.isEmpty() ? null : Integer.parseInt(s);
    }

    /**
     * Parses a single string representation of one of the user's preferences.
     * 
     * @param arg The string to be parsed.
     * @return <code>true</code> on success, <code>false</code> on failure.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    private boolean parse(String arg) {
        Matcher matcher = RANGEARGUMENTPATTERN.matcher(arg);
        if (matcher.matches()) {
            // min, max
            final Integer min = parseInt(matcher.group(1)), max;
            if (matcher.group(2).isEmpty()) // no delimitator
                max = min; 
            else
                max = parseInt(matcher.group(3));
            if (min == null || max == null || min <= max) {
                // uom
                final String uom = matcher.group(4);
                if (DISTANCEUOM.equals(uom)) {
                    distanceFilter = new Range<Double>(
                        min == null ? null : (double)min,
                        max == null ? null : (double)max);
                    return true;
                }
                for (Stat stat : Stat.values())
                    if (stat.uom().equals(uom)) {
                        statFilters.put(stat, new Range<Integer>(min, max));
                        return true;
                    }
            }
            return false;
        }
        matcher = TAGARGUMENTPATTERN.matcher(arg);
        if (matcher.matches()) {
            String tag = matcher.group(2);
            if (matcher.group(1).equals(EXCLUDETAGPREFIX))
                exclude.add(tag);
            else
                include.add(tag);
        }
        return false;
    }

}