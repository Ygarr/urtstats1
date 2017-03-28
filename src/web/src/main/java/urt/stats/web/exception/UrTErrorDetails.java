package urt.stats.web.exception;

import urt.stats.web.util.UrTWebUtil;

/**
 * UrT error details holdert
 * @author ghost
 *
 */
public class UrTErrorDetails extends Exception {

    /**
     * Defaule stacktrace depth
     */
    public static final int DEFAULT_STACK_DEPTH = 10;

    /**
     * Localized error description
     */
    private String descr = "";
    
    /**
     * Stack trace as string
     */
    private String stackTraceAsString = "";

    /**
     * Parameterized constructor
     * 
     * @param descr {@link String} localized error description
     * @param exception {@link Exception} exception
     */
    public UrTErrorDetails(String descr, Exception exception) {
        super(exception);
        this.descr = descr;
        if(exception != null){
            stackTraceAsString = UrTWebUtil.getStackTrace(exception, DEFAULT_STACK_DEPTH);
        }
    }

    /**
     * Returns Localized error description
     * 
     * @return {@link String} Localized error description
     */
    public String getDescr() {
        return descr;
    }

    /**
     * Returns stacktrace as formatted HTML text;
     * 
     * @return stacktrace as formatted HTML text;
     */
    public String getStackTraceAsString() {
        return stackTraceAsString;
    }
    
}
