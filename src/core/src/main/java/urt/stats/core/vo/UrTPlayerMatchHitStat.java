package urt.stats.core.vo;

import urt.stats.core.enums.UrTHitTarget;

/**
 * Player hit(how many times player had hit the target) stats during match
 * @author ghost
 *
 */
public class UrTPlayerMatchHitStat {

    /**
     * Player match DB ID
     */
    private Long playerMatchId = null;
    
    /**
     * Hit target
     */
    private UrTHitTarget target = null;
    
    /**
     * Hit count
     */
    private int hitCount = 0;

    /**
     * Parameterized constructor
     * 
     * @param target {@link UrTHitTarget} hit target 
     */
    public UrTPlayerMatchHitStat(UrTHitTarget target) {
        this.target = target;
    }

    /**
     * Parameterized constructor with all fields
     * 
     * @param playerMatchId {@link Long} Player match DB ID
     * @param target {@link UrTHitTarget} Hit target
     * @param hitCount int Hit count
     */
    public UrTPlayerMatchHitStat(
            Long playerMatchId
          , UrTHitTarget target
          , int hitCount) {
        this.playerMatchId = playerMatchId;
        this.target = target;
        this.hitCount = hitCount;
    }

    /**
     * Returns player match DB ID
     * 
     * @return {@link Long} player match BD  ID
     */
    public Long getPlayerMatchId() {
        return playerMatchId;
    }

    /**
     * Sets player match DB ID
     * 
     * @param playerMatchId {@link Long} player match BD  ID
     */
    public void setPlayerMatchId(Long playerMatchId) {
        this.playerMatchId = playerMatchId;
    }

    /**
     * Returns hit target
     * 
     * @return {@link UrTHitTarget} hit target
     */
    public UrTHitTarget getTarget() {
        return target;
    }

    /**
     * Sets hit taarget
     * 
     * @param target {@link UrTHitTarget} hit target
     */
    public void setTarget(UrTHitTarget target) {
        this.target = target;
    }

    /**
     * Returns target hit count
     * 
     * @return int - target git count
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Sets target hit count
     * 
     * @param hitCount int - target hit count
     */
    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
    
    /**
     * Increments hit count
     */
    public void incrementHitCount(){
        this.hitCount ++;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hitCount;
        result = prime * result
                + ((playerMatchId == null) ? 0 : playerMatchId.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerMatchHitStat other = (UrTPlayerMatchHitStat) obj;
        if (hitCount != other.hitCount)
            return false;
        if (playerMatchId == null) {
            if (other.playerMatchId != null)
                return false;
        } else if (!playerMatchId.equals(other.playerMatchId))
            return false;
        if (target != other.target)
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerMatchHitStats [playerMatchId=" + playerMatchId
                + ", target=" + target + ", hitCount=" + hitCount + "]";
    }
    
}
