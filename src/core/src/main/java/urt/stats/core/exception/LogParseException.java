package urt.stats.core.exception;

/**
 * Error during log parsing
 * @author ghost
 *
 */
public class LogParseException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public LogParseException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public LogParseException(String message, Exception exception) {
        super(message, exception);
    }
}
