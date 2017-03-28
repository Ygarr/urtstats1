package urt.stats.core.vo;

import urt.stats.core.enums.UrTCountAchvType;

/**
 * Quantitative achievements for player match
 * 
 * @author ghost
 *
 */
public class UrTPlayerMatchCountAchv {

    /**
     * Player match DB ID
     */
    private Long playerMatchId = null;
    
    /**
     * Achievement type
     */
    private UrTCountAchvType achievementType = null;
    
    /**
     * Achievement count
     */
    private int achvCount =  0;

    /**
     * Parameterized constructor
     * 
     * @param achievementType {@link UrTCountAchvType} achievement type
     */
    public UrTPlayerMatchCountAchv(UrTCountAchvType achievementType) {
        this.achievementType = achievementType;
    }

    /**
     * Parameterized constructor with all fields
     * 
     * @param playerMatchId {@link Long} Player match DB ID
     * @param achievementType {@link UrTCountAchvType} Achievement type
     * @param achvCount int - Achievement count
     */
    public UrTPlayerMatchCountAchv(Long playerMatchId,
            UrTCountAchvType achievementType, int achvCount) {
        this.playerMatchId = playerMatchId;
        this.achievementType = achievementType;
        this.achvCount = achvCount;
    }

    /**
     * Returns Player match DB ID
     * @return {@link Long} Player match DB ID
     */
    public Long getPlayerMatchId() {
        return playerMatchId;
    }

    /**
     * Sets Player match DB ID
     * @param playerMatchId {@link Long} Player match DB ID
     */
    public void setPlayerMatchId(Long playerMatchId) {
        this.playerMatchId = playerMatchId;
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
     * Increments Achievement count
     */
    public void incrementAchvCount(){
        this.achvCount ++;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + achvCount;
        result = prime * result
                + ((achievementType == null) ? 0 : achievementType.hashCode());
        result = prime * result
                + ((playerMatchId == null) ? 0 : playerMatchId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerMatchCountAchv other = (UrTPlayerMatchCountAchv) obj;
        if (achvCount != other.achvCount)
            return false;
        if (achievementType != other.achievementType)
            return false;
        if (playerMatchId == null) {
            if (other.playerMatchId != null)
                return false;
        } else if (!playerMatchId.equals(other.playerMatchId))
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerMatchCountAchievement [playerMatchId=" + playerMatchId
                + ", achievementType=" + achievementType + ", achCount="
                + achvCount + "]";
    }
    
}
