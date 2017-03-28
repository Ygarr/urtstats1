package urt.stats.web;

import org.springframework.stereotype.Component;

/**
 * UrT Stats configuration
 * @author ghost
 *
 */
@Component("urtStatsConfig")
public class UrTStatsConfig {

    /**
     * Default page size
     */
    private int defaultPageSize = 20;
    
    /**
     * Project version
     */
    private String version = null;

    /**
     * Returns default page size
     * @return int default page size
     */
    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * Sets default page size
     * @param defaultPageSize
     */
    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    /**
     * Returns project version
     * 
     * @return {@link String} project version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * Sets project version
     * 
     * @param version {@link String} project version
     */
    public void setVersion(String version) {
        this.version = version;
    }

}


