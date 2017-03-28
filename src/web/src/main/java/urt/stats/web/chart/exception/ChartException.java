package urt.stats.web.chart.exception;

/**
 * Chart exception
 * 
 * @author ghost
 *
 */
public class ChartException extends Exception {

    /**
     * Constructor with message
     * @param message {@link String} error message
     */
    public ChartException(String message) {
        super(message);
    }
    
    /**
     * Constructor with cause
     * @param e {@link Throwable} cause
     */
    public ChartException(Throwable e) {
        super(e);
    }

    /**
     * Constructor with message and cause
     * @param message {@link String} error message
     * @param e {@link Throwable} cause
     */
    public ChartException(String message, Throwable e) {
        super(message, e);
    }

}
