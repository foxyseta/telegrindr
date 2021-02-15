package bot.data;

import static java.lang.Math.*;

import java.util.EnumMap;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedSet;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Filter implements Predicate<Profile> {
    
    final public static String RANGEARGUMENTREGEX = "(\\d*)(,?)(\\d*)(\\w+)",
                               TAGARGUMENTREGEX = "([+-]?)#([0-9A-Za-z]+).*",
                               DISTANCEUOM = "km",
                               EXCLUDETAGPREFIX = "-"; 
    final public static Pattern RANGEARGUMENTPATTERN =
        Pattern.compile(RANGEARGUMENTREGEX),
                                TAGARGUMENTPATTERN =
        Pattern.compile(TAGARGUMENTREGEX);
    final public static double EARTHRADIUS = 6371005.076123; // m (average)
    public Location from;

    public Filter(String[] arguments, Location from) {
        for (String argument : arguments)
            parse(argument);
        this.from = from;
    }

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
    
    public boolean isLocationNeeded() {
        return distanceFilter != null;
    }

    private EnumMap<Stat, Range<Integer>> statFilters =
        new EnumMap<Stat, Range<Integer>>(Stat.class);
    private Range<Double> distanceFilter;
    private HashSet<String> include = new HashSet<String>(),
                            exclude = new HashSet<String>();
    
    // Haversine method
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

    private Integer parseInt(String s) {
        return s.isEmpty() ? null : Integer.parseInt(s);
    }

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