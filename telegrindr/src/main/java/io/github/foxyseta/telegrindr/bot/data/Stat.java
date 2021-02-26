package io.github.foxyseta.telegrindr.bot.data;

import java.util.regex.Pattern;

/**
 * <code>StatRegex</code> defines useful regex-related constants for the
 * {@link Stat} package.
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */
class StatRegex {

    /** A regex for units of measurement. */
    final public static String UOMREGEX = "\\w+";
    /**
     * The {@link Pattern} generated from the {@linkplain #UOMREGEX u.o.m.
     * regex}.
     */
    final public static Pattern UOMPATTERN = Pattern.compile(UOMREGEX);

}

/**
 * A <code>Stat</code> defines a new kind of numeric parameter.
 * 
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Stat {

    /** An age is measured in years. */
    AGE("yo", new Range<Integer>(14, 150)),
    /** An height is measured in centimeters. */
    HEIGHT("cm", new Range<Integer>(50, 250)),
    /** A weight is measured in kilograms. */
    WEIGHT("kg", new Range<Integer>(25, 750));
 
    /**
     * Instantiates a new {@link Stat}.
     * 
     * @param uom An unit of measurement.
     * @param interval The range of valid valued for this {@link Stat}.
     * @throws IllegalArgumentException The u.o.m. does not match the
     * {@linkplain StatRegex#UOMREGEX u.o.m.'s regex}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    Stat(String uom, Range<Integer> interval) {
        if (!StatRegex.UOMPATTERN.matcher(uom).matches())
            throw new IllegalArgumentException(
                "Stat.Stat: " + uom + " does not match " + StatRegex.UOMREGEX
            );
        this.uom = uom;
        this.interval = interval;
    }

    /**
     * Gets the {@link #uom}.
     * 
     * @return The unit of measurement of this {@link Stat}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public String uom() {
        return uom;
    }

    /**
     * Gets the {@link #min}.
     * 
     * @return The minimum value for this {@link Stat}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public int min() {
        return interval.getMin();
    }
    
    /**
     * Gets the {@link #max}.
     * 
     * @return The maximum value for this {@link Stat}.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public int max() {
        return interval.getMax();
    }

    /**
     * Checks whether a given value is contained in the suggested {@link
     * #interval}.
     * 
     * @param x The value to be checked.
     * @return <code>true</code> is the given value is valid, <code>false
     * </code> otherwise.
     * @author FoxySeta
     * @version 1.0.0
     * @since 1.0.0
     */
    public boolean validate(int x) {
        return interval.contains(x);
    }

    /** The unit of measurement for this {@link Stat}. */
    private String uom;
    /** A {@Range} representing the accepted values for this {@link Stat}. */
    private Range<Integer> interval;

}