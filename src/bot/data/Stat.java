package bot.data;

public enum Stat {

    AGE("yo", new Range<Integer>(14, 150)),
    HEIGHT("cm", new Range<Integer>(50, 250)),
    WEIGHT("kg", new Range<Integer>(25, 750));

    Stat(String uom, Range<Integer> interval) {
        this.uom = uom;
        this.interval = interval;
    }

    public String uom() {
        return uom;
    }

    public int min() {
        return interval.getMin();
    }
    
    public int max() {
        return interval.getMax();
    }

    public boolean validate(int x) {
        return interval.contains(x);
    }

    private String uom;
    private Range<Integer> interval;
}