package urt.stats.core.vo;

import java.util.ArrayList;
import java.util.List;

import urt.stats.core.enums.UrTHitTarget;

/**
 * Best Player hit(how many times player had hit the target) stats during match
 * @author ghost
 *
 */
public class UrTMatchHitStat {

    /**
     * Hit target
     */
    private UrTHitTarget target = null;
    
    /**
     * Hit count
     */
    private int hitCount = 0;

    /**
     * Players with stat
     */
    private List<UrTPlayer> players = new ArrayList<UrTPlayer>();
    
    /**
     * Parameterized constructor
     * 
     * @param target {@link UrTHitTarget} hit target 
     */
    public UrTMatchHitStat(UrTHitTarget target) {
        this.target = target;
    }

    /**
     * Parameterized constructor with all fields
     * 
     * @param target {@link UrTHitTarget} Hit target
     * @param hitCount int Hit count
     */
    public UrTMatchHitStat(
            UrTHitTarget target
          , int hitCount) {
        this.target = target;
        this.hitCount = hitCount;
    }

    /**
     * Returns Hit target
     * @return {@link UrTHitTarget} Hit target
     */
    public UrTHitTarget getTarget() {
        return target;
    }

    /**
     * Sets Hit target
     * 
     * @param target {@link UrTHitTarget} Hit target
     */
    public void setTarget(UrTHitTarget target) {
        this.target = target;
    }

    /**
     * Returns Hit count
     * 
     * @return int - Hit count
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Sets Hit count
     * 
     * @param hitCount int - Hit count
     */
    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    /**
     * Returns list of Players with stat
     * @return {@link List} of {@link UrTPlayer} Players with stat
     */
    public List<UrTPlayer> getPlayers() {
        return players;
    }

    /**
     * Sets list of Players with stat
     * @param players {@link List} of {@link UrTPlayer} Players with stat
     */
    public void setPlayers(List<UrTPlayer> players) {
        this.players = players;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hitCount;
        result = prime * result + ((players == null) ? 0 : players.hashCode());
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
        UrTMatchHitStat other = (UrTMatchHitStat) obj;
        if (hitCount != other.hitCount)
            return false;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (target != other.target)
            return false;
        return true;
    }

    public String toString() {
        return "UrTMatchHitStat [target=" + target + ", hitCount=" + hitCount
                + ", players=" + players + "]";
    }
}
