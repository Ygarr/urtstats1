package urt.stats.core.exception;

/**
 * Empty or invalid log file
 * 
 * @author ghost
 *
 */
public class EmptyLogFileException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public EmptyLogFileException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public EmptyLogFileException(String message, Exception exception) {
        super(message, exception);
    }
}
