package urt.stats.core.exception;

/**
 * Stats import failed exception
 * 
 * @author ghost
 *
 */
public class ImportFailedException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public ImportFailedException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public ImportFailedException(String message, Exception exception) {
        super(message, exception);
    }
}
