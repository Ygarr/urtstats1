package urt.stats.core.exception;

public class JdbcException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public JdbcException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public JdbcException(String message, Exception exception) {
        super(message, exception);
    }
}
