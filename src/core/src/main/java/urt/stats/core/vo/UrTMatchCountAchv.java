package urt.stats.core.vo;

import java.util.ArrayList;
import java.util.List;

import urt.stats.core.enums.UrTCountAchvType;

/**
 * Best Match count achievement VO
 * @author ghost
 *
 */
public class UrTMatchCountAchv {

    /**
     * Achievement type
     */
    private UrTCountAchvType achievementType = null;
    
    /**
     * Achievement count
     */
    private int achvCount =  0;

    /**
     * Players with achievement
     */
    private List<UrTPlayer> players = new ArrayList<UrTPlayer>();
    
    /**
     * Parameterized constructor
     * 
     * @param achievementType {@link UrTCountAchvType} achievement type
     */
    public UrTMatchCountAchv(UrTCountAchvType achievementType) {
        this.achievementType = achievementType;
    }

    /**
     * Parameterized constructor with all fields
     * 
     * @param achievementType {@link UrTCountAchvType} Achievement type
     * @param achvCount int - Achievement count
     */
    public UrTMatchCountAchv(
              UrTCountAchvType achievementType
            , int achvCount) {
        this.achievementType = achievementType;
        this.achvCount = achvCount;
    }

    /**
     * Returns Achievement type
     * @return {@link UrTCountAchvType} Achievement type
     */
    public UrTCountAchvType getAchievementType() {
        return achievementType;
    }

    /**
     * Sets Achievement type
     * @param achievementType {@link UrTCountAchvType} Achievement type
     */
    public void setAchievementType(UrTCountAchvType achievementType) {
        this.achievementType = achievementType;
    }

    /**
     * Returns Achievement count
     * @return int - Achievement count
     */
    public int getAchvCount() {
        return achvCount;
    }

    /**
     * Sets Achievement count
     * @param achvCount int - Achievement count
     */
    public void setAchvCount(int achvCount) {
        this.achvCount = achvCount;
    }

    /**
     * Returns list of Players with achievement
     * @return {@link List} of {@link UrTPlayer} Players with achievement
     */
    public List<UrTPlayer> getPlayers() {
        return players;
    }

    /**
     * Sets list of Players with achievement
     * @param players {@link List} of {@link UrTPlayer} Players with achievement
     */
    public void setPlayers(List<UrTPlayer> players) {
        this.players = players;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((achievementType == null) ? 0 : achievementType.hashCode());
        result = prime * result + achvCount;
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTMatchCountAchv other = (UrTMatchCountAchv) obj;
        if (achievementType != other.achievementType)
            return false;
        if (achvCount != other.achvCount)
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        return true;
    }

    public String toString() {
        return "UrTMatchCountAchv [achievementType=" + achievementType
                + ", achvCount=" + achvCount + ", players=" + players + "]";
    }
    
    
}
