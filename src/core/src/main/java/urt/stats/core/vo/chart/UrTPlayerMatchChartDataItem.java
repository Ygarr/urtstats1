package urt.stats.core.vo.chart;

import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTPlayerMatchStats;

/**
 * Player match chart data
 * @author ghost
 *
 */
public class UrTPlayerMatchChartDataItem extends UrTAbstractChartDataItem {

    /**
     * Player match stats VO
     */
    private UrTPlayerMatchStats playerMatchStats = null;
    
    /**
     * Match details
     */
    private UrTMatch match = null;

    /**
     * Returns player match stats
     * @return {@link UrTPlayerMatchStats} player match stats
     */
    public UrTPlayerMatchStats getPlayerMatchStats() {
        return playerMatchStats;
    }

    /**
     * Sets player match stats
     * @param playerMatchStats {@link UrTPlayerMatchStats} player match stats
     */
    public void setPlayerMatchStats(UrTPlayerMatchStats playerMatchStats) {
        this.playerMatchStats = playerMatchStats;
    }

    /**
     * Returns match details
     * @return {@link UrTMatch} match details
     */
    public UrTMatch getMatch() {
        return match;
    }

    /**
     * Sets match details
     * @param match {@link UrTMatch} match details
     */
    public void setMatch(UrTMatch match) {
        this.match = match;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((match == null) ? 0 : match.hashCode());
        result = prime
                * result
                + ((playerMatchStats == null) ? 0 : playerMatchStats.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerMatchChartDataItem other = (UrTPlayerMatchChartDataItem) obj;
        if (match == null) {
            if (other.match != null)
                return false;
        } else if (!match.equals(other.match))
            return false;
        if (playerMatchStats == null) {
            if (other.playerMatchStats != null)
                return false;
        } else if (!playerMatchStats.equals(other.playerMatchStats))
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerMatchChartData [playerMatchStats=" + playerMatchStats
                + ", match=" + match + "]";
    }
    
}
