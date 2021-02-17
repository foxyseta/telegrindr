package bot.data;

/**
 * A <code>Range</code> defines an interval of values. If bot extremes are
 * not-<code>null</code>, the first one must be less or equal than the
 * second.
 *
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */
public class Range<T extends Comparable<? super T>> {
    
    /**
     * Instantiates a new {@link Range}.
     * 
     * @param min The first extreme.
     * @param max The second extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public Range(T min, T max) {
        setMinMax(min, max);
    }
    
    /**
     * Gets the first extreme.
     *
     * @see #min 
     * @return The first extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public T getMin() {
        return min;
    }
    
    /**
     * Sets the first extreme.
     * 
     * @see #min
     * @param min The new value for the first extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public void setMin(T min) {
        this.min = min;
        validateRange();
    }
    
    /**
     * Gets the second extreme.
     * 
     * @see #max
     * @return The second extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public T getMax() {
        return max;
    }
    
    /**
     * Sets the second extreme.
     * 
     * @see #max
     * @param max The new value for the second extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public void setMax(T max) {
        this.max = max;
        validateRange();
    }
    
    /**
     * Sets both extremes at the same time.
     * 
     * @see #min
     * @see #max
     * @param min The new value for the first extreme.
     * @param max The new value for the second extreme.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public void setMinMax(T min, T max) {
        this.min = min;
        this.max = max;
        validateRange();
    }

    /**
     * Checks whether the {@link Range} contains the given value.
     * @param x Value to be checked.
     * @return <code>true</code> if the {@link Range} contains the value,
     * <code>false</code> otherwise.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean contains(T x) {
        return (min == null || min.compareTo(x) <= 0) &&
               (max == null || x.compareTo(max) <= 0);
    }

    /** The first extreme. */
    private T min;
    /** The second extreme. */
    private T max;
    
    /**
     * Checks for the validity of the {@link Range} extremes.
     * 
     * @throws IllegalArgumentException The first extreme is greater than the
     * second.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    private void validateRange() {
        if (min != null && max != null && min.compareTo(max) > 0)
            throw new IllegalArgumentException(
                "Range.validateRange: min (" + min + ") > max (" + max + ")"
            );
    }

}
