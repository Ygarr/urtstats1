package urt.stats.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utilities for web module
 * 
 * @author ghost
 *
 */
public class UrTWebUtil {

    /**
     * Line break separator for the stacktrace output
     */
    private static final String STACK_TRACE_SEPARATOR = "<br/>";
    
    /**
     * Line ident separator for the stacktrace output
     */
    private static final String STACK_TRACE_IDENT= "&nbsp;&nbsp;&nbsp;&nbsp;";
    
    /**
     * "X-Forwarded-For" HTTP request header name
     */
    public static final String HTTP_HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    
    /**
     * Returns client IP addr
     * 
     * @param request {@link HttpServletRequest} request
     * @return {@link String} client IP
     */
    public static String getClientIp( HttpServletRequest request){
        String result = null;
        result = request.getHeader(HTTP_HEADER_X_FORWARDED_FOR);
        if(result == null){
            result = request.getRemoteAddr();
        }
        if(result == null){
            result = "unknown";
        }
        return result;
    }
    
    /**
     * Returns stack trace
     * В качестве символа новой строки используется "&lt;br/&gt;", так как стектрейс нужно выводить в HTML.
     * Recursevely proceses stacktrace
     * 
     * @param aThrowable - исключение
     * @param depth - глубина рекурсивного вызова.
     * 
     * @return - {@link String} - stacktrace
     */
    public static String getStackTrace(Throwable aThrowable, int depth) {
        final String NEW_LINE = STACK_TRACE_SEPARATOR;
        final String IDENT = STACK_TRACE_IDENT;
        final StringBuilder result = new StringBuilder("");
        if(depth == 0){
            result.append(aThrowable.toString());
            result.append(NEW_LINE);
            result.append(NEW_LINE);
        }
        for (StackTraceElement element : aThrowable.getStackTrace() ){
          result.append(IDENT); 
          result.append(element);
          result.append(NEW_LINE);
        }
        if(depth == 10){
            return result.toString();
        }
        Throwable cause = aThrowable.getCause();
        if(cause != null){
            result.append(NEW_LINE);
            result.append("Caused by: ");
            result.append(cause.toString());
            result.append(NEW_LINE);
            result.append(getStackTrace(cause, depth+1));
        }
        return result.toString();
      }
}
