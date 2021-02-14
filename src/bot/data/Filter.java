package bot.data;

import java.util.EnumMap;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.SortedSet;
import static java.lang.Math.*;
import org.telegram.telegrambots.meta.api.objects.Location;

public class Filter implements Predicate<Profile> {
    
    final public static String RANGEARGUMENTREGEX = "(\\d*),?(\\d*)(.+)";
    final public static double EARTHRADIUS = 6371005.076123; // m (average)
    public Location from;

    public Filter(String[] arguments, Location from) {
        this.from = from;
    }

    public interface Query {

        public boolean validate(SortedSet<String> tags);

    }

    public class AtomicQuery implements Query {

        public AtomicQuery(String tag) {
            this.tag = tag;
        }

        public String tag;

        public boolean validate(SortedSet<String> tags) {
           return tags.contains(this.tag);
        }
        
    }
    
    public class ExcludingQuery implements Query {

        public ExcludingQuery(Query query) {
            this.query = query;
        }
        
        public Query query;

        public boolean validate(SortedSet<String> tags) {
            return !query.validate(tags);
        }

    }
    
    public class ConjoiningQuery implements Query {

        public ConjoiningQuery(Query queryA, Query queryB) {
            this.queryA = queryA;
            this.queryB = queryB;
        }
        
        public Query queryA, queryB;

        public boolean validate(SortedSet<String> tags) {
            return queryA.validate(tags) && queryB.validate(tags);
        }
        
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
        if (queries == null)
            return true;
        for (Query query : queries)
            if (!query.validate(profile.unmodifiableTags())) 
                return false;
        return true;
    }
    
    private EnumMap<Stat, Range<Integer>> statFilters =
        new EnumMap<Stat, Range<Integer>>(Stat.class);
    private Range<Double> distanceFilter;
    private HashSet<Query> queries = new HashSet<Query>();

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

}