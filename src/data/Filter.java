package data;

import java.util.EnumMap;

public class Filter {
    
    public boolean validate(Profile profile) {
        // TODO validate
        return false;
    }
    
    public EnumMap<Stat, Range<Integer>> stats =
        new EnumMap<Stat, Range<Integer>>(Stat.class);
    public Range<Double> distance;

    public interface Query {

        public boolean validate(String tag);

    }

    public class AtomicQuery implements Query {

        public AtomicQuery(String tag) {
            this.tag = tag;
        }

        public String tag;

        public boolean validate(String tag) {
           return this.tag.equalsIgnoreCase(tag);
        }
        
    }
    
    public class ExcludingQuery implements Query {

        public ExcludingQuery(Query query) {
            this.query = query;
        }
        
        public Query query;

        public boolean validate(String tag) {
            return !query.validate(tag);
        }

    }
    
    public class ConjoiningQuery implements Query {

        public ConjoiningQuery(Query queryA, Query queryB) {
            this.queryA = queryA;
            this.queryB = queryB;
        }
        
        public Query queryA, queryB;

        public boolean validate(String tag) {
            return queryA.validate(tag) && queryB.validate(tag);
        }
        
    }

    public Query query;

}
