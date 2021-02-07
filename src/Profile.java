import java.util.EnumMap;
import java.util.TreeSet;

import org.telegram.telegrambots.meta.api.objects.Location;

public class Profile {

    public static final String DEFAULTEMOJI= "😀"; 

    enum Stat {

        AGE("yo"),
        HEIGHT("cm"),
        WEIGHT("kg");

        Stat(String uom) {
            this.uom = uom;
        }
        
        public String toString() {
            return uom;
        }

        String uom;
    
    }

    public Profile(String id)
    {
       this.id = id; 
    }

    String id,
           emoji = DEFAULTEMOJI;
    EnumMap<Stat, Integer> stats = new EnumMap<Stat, Integer>(Stat.class);
    TreeSet<String> hashtags = new TreeSet<String>();
    Location location;
}
