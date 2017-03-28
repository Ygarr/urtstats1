package urt.stats.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Perfomance timing analyzer
 * 
 * @author ghost
 *
 */
public class TimingAnalyzer {

    /**
     * Logger
     */
    private Logger logger;
    
    /**
     * Timer start timestamp
     */
    private long startTime = -1;

    /**
     * Time format
     */
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss SS");
    
    /**
     * Contructor with logger
     * 
     * @param logger {@link WmLogger} logger
     */
    public TimingAnalyzer(Logger logger){
        this.logger = logger;
        init();
    }
    
    /**
     * Constructor with class for logger creation
     * 
     * @param clazz {@link Class} class for logger creation
     */
    public TimingAnalyzer(Class clazz){
        this.logger = Logger.getLogger(clazz);
        init();
    }
    
    /**
     * Initialization
     */
    private void init(){
        this.timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }
    
    /**
     * Starts timer
     */
    public void start(){
        this.startTime = System.currentTimeMillis();
    } 
    
    /**
     * Stops timer and logs given message with timing stats
     * 
     * @param message {@link StringBuffer} message to log
     * @param loggerPriority {@link Priority} logger priority
     */
    public void stop(StringBuffer message, Priority loggerPriority){
        if(startTime == -1){
            throw new IllegalAccessError("Timer is not started! Cannot stop.");
        }
        long endTime = System.currentTimeMillis();
        long dt = endTime - startTime;
        Date time = new Date(dt);
        message.append("Execution time: ");
        message.append(timeFormat.format(time));
        this.logger.log(loggerPriority, message.toString());
    }
}
