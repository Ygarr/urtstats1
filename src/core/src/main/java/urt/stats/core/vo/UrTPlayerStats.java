package urt.stats.core.vo;

import java.util.Date;

import urt.stats.core.enums.UrTTeam;

/**
 * Player total stats VO
 * 
 * @author ghost
 *
 */
public class UrTPlayerStats extends UrTPlayer {

    /**
     * Kill count total (how many foes player had killed)
     */
    private int kills = 0;

    /**
     * Death count total(how many times player had died)
     */
    private int deaths = 0;

    /**
     * Total kills-to-deaths ratio
     */
    private double totalKDRatio = 0;
    
    /**
     * Average kills-to-deaths ratio
     */
    private double avgKDRatio = 0;
    
    /**
     * Games played
     */
    private int gamesPlayed = 0;
    
    /**
     * Last game time
     */
    private Date lastGameTime = null;
    
    /**
     * Default constructor
     */
    public UrTPlayerStats(){
        
    }
    
    /**
     * Constructor
     * 
     * @param playerName {@link String} player name
     */
    public UrTPlayerStats(String playerName){
        super(playerName);
    }

    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} unique player ID from DB
     * @param playerName {@link String} player name
     * @param kills int Kill count total (how many foes player had killed)
     * @param deaths int Death count total(how many times player had died)
     * @param totalKDRatio double Total kills-to-deaths ratio
     * @param avgKDRatio double Average kills-to-deaths ratio
     * @param gamesPlayed int Games played
     * @param lastGameTime {@link Date} last player game time
     */
    public UrTPlayerStats(
            Long id
          , String playerName
          , int kills
          , int deaths
          , double totalKDRatio
          , double avgKDRatio
          , int gamesPlayed
          , Date lastGameTime) {
        super(id, playerName);
        this.kills = kills;
        this.deaths = deaths;
        this.totalKDRatio = totalKDRatio;
        this.avgKDRatio = avgKDRatio;
        this.gamesPlayed = gamesPlayed;
        this.lastGameTime = lastGameTime;
    }

    /**
     * Constructor from player match stats
     * 
     * @param playerMatchStats {@link UrTPlayerMatchStats} player match stats
     */
    public UrTPlayerStats(UrTPlayerMatchStats playerMatchStats){
        super(playerMatchStats.getPlayerName());
        this.kills = playerMatchStats.getKills();
        this.deaths = playerMatchStats.getDeaths();
        this.gamesPlayed = 1;
    }
    
    /**
     * Returns kill count total
     * 
     * @return int kill count
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Returns death count total
     * 
     * @return int death count
     */
    public int getDeaths() {
        return this.deaths;
    }
    
    /**
     * Returns games played count
     * @return int games played count
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Sets games played count
     * @param gamesPlayed int games played count
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * Returns last player game time
     * 
     * @return {@link Date} last player game time
     */
    public Date getLastGameTime() {
        return lastGameTime;
    }

    /**
     * Set last player game time
     * @param lastGameTime {@link Date} last player game time
     */
    public void setLastGameTime(Date lastGameTime) {
        this.lastGameTime = lastGameTime;
    }

    /**
     * Updates player stats with match stats (kills, deaths...)
     * 
     * @param playerMatchStats {@link UrTPlayerMatchStats} player match stats
     */
    public void updateStats(UrTPlayerMatchStats playerMatchStats){
        this.kills += playerMatchStats.getKills();
        this.deaths += playerMatchStats.getDeaths();
        if(playerMatchStats.getPlayerTeam() != null && !UrTTeam.SPECTATOR.equals(playerMatchStats.getPlayerTeam()) ){
            this.gamesPlayed += 1;
        }
    }
    
    /**
     * Calculates and returns total kills-to-deaths ratio
     * 
     * @return double calculated kills-to-deaths ratio
     */
    public double calculateTotalKDRatio() {
        double result = 0;
        int kills = getKills();
        int deaths = getDeaths();
        
        if(deaths != 0){
            result = (double)kills / (double)deaths;
        } else {
            result = (double)kills;
        }
        
        return result;
    }

    /**
     * Returns Total kills-to-deaths ratio
     * @return double - Total kills-to-deaths ratio
     */
    public double getTotalKDRatio() {
        return totalKDRatio;
    }

    /**
     * Sets Total kills-to-deaths ratio
     * 
     * @param totalKDRatio double - Total kills-to-deaths ratio
     */
    public void setTotalKDRatio(double totalKDRatio) {
        this.totalKDRatio = totalKDRatio;
    }

    /**
     * Returns Average kills-to-deaths ratio
     * @return double - Average kills-to-deaths ratio
     */
    public double getAvgKDRatio() {
        return avgKDRatio;
    }

    /**
     * Sets Average kills-to-deaths ratio
     * 
     * @param avgKDRatio double - Average kills-to-deaths ratio
     */
    public void setAvgKDRatio(double avgKDRatio) {
        this.avgKDRatio = avgKDRatio;
    }

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(avgKDRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + deaths;
        result = prime * result + gamesPlayed;
        result = prime * result + kills;
        result = prime * result
                + ((lastGameTime == null) ? 0 : lastGameTime.hashCode());
        temp = Double.doubleToLongBits(totalKDRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerStats other = (UrTPlayerStats) obj;
        if (Double.doubleToLongBits(avgKDRatio) != Double
                .doubleToLongBits(other.avgKDRatio))
            return false;
        if (deaths != other.deaths)
            return false;
        if (gamesPlayed != other.gamesPlayed)
            return false;
        if (kills != other.kills)
            return false;
        if (lastGameTime == null) {
            if (other.lastGameTime != null)
                return false;
        } else if (!lastGameTime.equals(other.lastGameTime))
            return false;
        if (Double.doubleToLongBits(totalKDRatio) != Double
                .doubleToLongBits(other.totalKDRatio))
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerStats [kills=" + kills + ", deaths=" + deaths
                + ", totalKDRatio=" + totalKDRatio + ", avgKDRatio="
                + avgKDRatio + ", gamesPlayed=" + gamesPlayed
                + ", lastGameTime=" + lastGameTime + ", toString()="
                + super.toString() + "]";
    }

}
