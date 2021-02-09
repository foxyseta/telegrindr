package bot.data;

public class Range<T extends Comparable<? super T>> {
    
    public Range(T min, T max) {
        setMinMax(min, max);
    }
    
    public T getMin() {
        return min;
    }
    
    public void setMin(T min) {
        this.min = min;
        validateRange();
    }
    
    public T getMax() {
        return max;
    }
    
    public void setMax(T max) {
        this.max = max;
        validateRange();
    }
    
    public void setMinMax(T min, T max) {
        this.min = min;
        this.max = max;
        validateRange();
    }

    public boolean contains(T x) {
        return min.compareTo(x) <= 0 && x.compareTo(max) <= 0;
    }

    private T min, max;
    
    private void validateRange() {
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException(
                "Range.validateRange: min (" + min + ") > max (" + max + ")"
            );
    }
}
