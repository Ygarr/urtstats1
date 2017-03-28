package urt.stats.core.vo;

import java.util.Date;

import urt.stats.core.enums.UrTGameType;

/**
 * Game match
 * 
 * @author ghost
 *
 */
public class UrTMatch {

    /**
     * Unique match ID from DB
     */
    private Long id = null;
    
    /**
     * Map
     */
    private UrTMap map = null;
    
    /**
     * Log file name
     */
    private String logFileName = null;
    
    /**
     * Game type
     */
    private UrTGameType gameType = null;
    
    /**
     * Start time
     */
    private Date startTime = null;
    
    /**
     * End time
     */
    private Date endTime = null;
    
    /**
     * Default constructor
     */
    public UrTMatch() {
        
    }
    
    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} Unique match ID from DB
     * @param map {@link UrTMap} Map
     * @param logFileName {@link String} import log file name
     * @param gameType {@link UrTGameType} game type 
     * @param startTime {@link Date} start time
     * @param endTime {@link Date} end time
     */
    public UrTMatch(
            Long id
          , UrTMap map
          , String logFileName
          , UrTGameType gameType
          , Date startTime
          , Date endTime) {
        this.id = id;
        this.map = map;
        this.logFileName = logFileName;
        this.gameType = gameType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns unique match ID from DB
     * @return {@link Long} unique match ID from DB
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique match ID from DB
     * @param id {@link Long} unique match ID from DB
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returs game type
     * 
     * @return {@link UrTGameType} game type
     */
    public UrTGameType getGameType() {
        return gameType;
    }

    /**
     * Sets game type
     * 
     * @param gameType {@link UrTGameType} game type
     */
    public void setGameType(UrTGameType gameType) {
        this.gameType = gameType;
    }

    /**
     * Returns <code>true</code> if match game type is team based or <code>false</code> otherwise
     * 
     * @return boolean <code>true</code> if match game type is team based or <code>false</code> otherwise
     */
    public boolean isTeamBasedMatch(){
        return (getGameType() != null && getGameType().isTeamBased());
    }
    
    /**
     * Returns map
     * 
     * @return {@link UrTMap} map
     */
    public UrTMap getMap() {
        return this.map;
    }

    /**
     * Sets map
     * 
     * @param map {@link UrTMap} map
     */
    public void setMap(UrTMap map) {
        this.map = map;
    }

    /**
     * Returns log file name
     * 
     * @return {@link String} log file name
     */
    public String getLogFileName() {
        return logFileName;
    }

    /**
     * Sets log file name
     * 
     * @param logFileName {@link String} log file name
     */
    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    /**
     * Returns start time
     * 
     * @return {@link Date} start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets start time
     * @param startTime {@link Date} start time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns end time
     * @return {@link Date} end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets end time
     * @param endTime {@link Date} end time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result
                + ((gameType == null) ? 0 : gameType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((logFileName == null) ? 0 : logFileName.hashCode());
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result
                + ((startTime == null) ? 0 : startTime.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTMatch other = (UrTMatch) obj;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (gameType != other.gameType)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (logFileName == null) {
            if (other.logFileName != null)
                return false;
        } else if (!logFileName.equals(other.logFileName))
            return false;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        return true;
    }

    public String toString() {
        return "UrTMatch [id=" + id + ", map=" + map + ", logFileName="
                + logFileName + ", gameType=" + gameType + ", startTime="
                + startTime + ", endTime=" + endTime + "]";
    }
}
