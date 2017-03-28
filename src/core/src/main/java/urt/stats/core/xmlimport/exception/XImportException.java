package urt.stats.core.xmlimport.exception;

/**
 * XML import exception
 * @author ghost
 *
 */
public class XImportException extends Exception {

    /**
     * Constructor with exception
     * 
     * @param exception {@link Exception} - exception
     */
    public XImportException(Exception exception) {
        super(exception);
    }
    
    /**
     * Constructor with exception and message
     * 
     * @param message {@link String} - message
     * @param exception {@link Exception} - exception
     */
    public XImportException(String message, Exception exception) {
        super(message, exception);
    }
}