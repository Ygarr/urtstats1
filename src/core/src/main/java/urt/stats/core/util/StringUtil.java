package urt.stats.core.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * String utilities
 * 
 * @author ghost
 *
 */
public class StringUtil {

    /**
     * Default date format pattern
     */
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    
    
    /**
     * Default date formatter
     */
    public static final DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    
    /**
     * Double value format pattern
     */
    public static final String DOUBLE_FORMAT = "###0.00";
    
    /**
     * Double formatter
     */
    public static DecimalFormat doubleFormat = new DecimalFormat(DOUBLE_FORMAT); 
    
    /**
     * Checks given string for not null and not empty (with trim)
     * 
     * @param str {@link String} string
     * 
     * @return boolean <code>true</code> fi string is not null and is not empty or <code>false</code> otherwise
     */
    public static boolean hasText(String str){
        if(str == null){
            return false;
        }
        if("".equals(str.trim())) {
            return false;
        }
        return true;
    }
    
    /**
     * Fill string with the specified symbol till the maxlength from the end
     * 
     * @param str {@link String} source string
     * @param symbol {@link String} symbol
     * @param length int length
     * 
     * @return {@link String} String
     */
    public static String rpadString(String str, String symbol, int length){
        if(!hasText(str)){
            throw new IllegalArgumentException("String required");
        }
        if(symbol == null){
            throw new IllegalArgumentException("Symbol required");
        }
        if(length <= 0){
            throw new IllegalArgumentException("Length should be greater than zero.");
        }

        StringBuilder result = new StringBuilder(str);
        while(result.length() < length){
            result.append(symbol);
        }
        
        return result.toString();
    }
    
    /**
     * Fill string with the specified symbol till the maxlength from the beginning
     * 
     * @param str {@link String} source string
     * @param symbol {@link String} symbol
     * @param length int length
     * 
     * @return {@link String} String
     */
    public static String lpadString(String str, String symbol, int length){
        if(!hasText(str)){
            throw new IllegalArgumentException("String required");
        }
        if(symbol == null){
            throw new IllegalArgumentException("Symbol required");
        }
        if(length <= 0){
            throw new IllegalArgumentException("Length should be greater than zero.");
        }

        StringBuilder result = new StringBuilder(str);
        while(result.length() < length){
            result.insert(0, symbol);
        }
        
        return result.toString();
    }
    
    /**
     * Converts date to string using default date format {@link #DATE_TIME_FORMAT}
     * 
     * @param date {@link Date} date
     * 
     * @return {@link String} date as string
     */
    public static String dateToString(Date date){
        if(date == null){
            throw new IllegalArgumentException("Date required!");
        }
        return dateFormat.format(date);
    }
    
    /**
     * Converts double value to string using default date format {@link #DOUBLE_FORMAT}
     * 
     * @param value double value
     * 
     * @return {@link String} double value as string
     */
    public static String doubleToString(double value){
        return doubleFormat.format(value);
    }
}
