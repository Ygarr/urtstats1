package urt.stats.core.exception;

/**
 * There is no such Object excepion
 *  
 * @author ghost
 *
 */
public class NoSuchObjectException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public NoSuchObjectException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public NoSuchObjectException(String message, Exception exception) {
        super(message, exception);
    }
}
