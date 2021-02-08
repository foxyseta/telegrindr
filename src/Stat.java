public enum Stat {

    AGE("yo", 14, 150), HEIGHT("cm", 50, 250), WEIGHT("kg", 25, 750);

    Stat(String uom, int min, int max) {
        if (min > max)
            throw new IllegalArgumentException(
                "Stat.Stat: min (" + min + ") > max (" + max + ")");
        this.uom = uom;
        this.min = min;
        this.max = max;
    }

    public String uom() {
        return uom;
    }

    public int min() {
        return min;
    }
    
    public int max() {
        return max;
    }

    public boolean validate(int value) {
        return min <= value && value <= max;
    }

    String uom;
    int min, max;
}