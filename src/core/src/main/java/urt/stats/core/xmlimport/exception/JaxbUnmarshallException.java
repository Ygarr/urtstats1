package urt.stats.core.xmlimport.exception;

/**
 * Jaxb unmarshall exception
 * @author ghost
 */
public class JaxbUnmarshallException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public JaxbUnmarshallException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public JaxbUnmarshallException(String message, Exception exception) {
        super(message, exception);
    }
}
