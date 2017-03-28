package urt.stats.reports.exception;

/**
 * Report exception
 * @author ghost
 *
 */
public class ReportException extends Exception {
    
    /**
     * Constructor with message
     * @param message - error message
     */
    public ReportException(String message) {
        super(message);
    }
    
    /**
     * Constructor with throwable
     * @param e - exception cause
     */
    public ReportException(Throwable e) {
        super(e);
    }
    
    /**
     * Constructor with message and throwable
     * @param message - error message
     * @param e - exception cause
     */
    public ReportException(String message, Throwable e) {
        super(message, e);
    }

}
