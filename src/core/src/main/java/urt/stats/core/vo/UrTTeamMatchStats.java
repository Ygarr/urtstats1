package urt.stats.core.vo;

import urt.stats.core.enums.UrTTeam;

/**
 * Team stats
 * 
 * @author ghost
 *
 */
public class UrTTeamMatchStats implements Comparable<UrTTeamMatchStats>{

    /**
     * Unique DB ID
     */
    private Long id;
    
    /**
     * Match ID
     */
    private Long matchId;
    
    /**
     * Team name/type
     */
    private UrTTeam teamType = null;
    
    /**
     * Kills counter
     */
    private int kills = 0;
    
    /**
     * Deaths counter
     */
    private int deaths = 0;
    
    /**
     * Default constructor
     */
    public UrTTeamMatchStats(){
        
    }
    
    /**
     * Constructor
     * 
     * @param teamType {@link UrTTeam} team
     */
    public UrTTeamMatchStats(UrTTeam teamType){
        this.teamType = teamType;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} unique DB ID
     * @param matchId {@link Long} match ID
     * @param teamType {@link UrTTeam} team
     * @param kills int kill count
     * @param deaths int death count
     */
    public UrTTeamMatchStats(
              Long id
            , Long matchId
            , UrTTeam teamType
            , int kills
            , int deaths) {
        this.id = id;
        this.teamType = teamType;
        this.kills = kills;
        this.deaths = deaths;
    }

    /**
     * Increments kill counter
     */
    public void incrementKills(){
        this.kills ++;
    }
    
    /**
     * Decrements kill counter
     */
    public void decrementKills(){
        this.kills --;
    }

    /**
     * Increments death counter
     */
    public void incrementDeaths(){
        this.deaths ++;
    }

    /**
     * Returns unique DB ID
     * 
     * @return {@link Long} unique DB ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique DB ID
     * @param id {@link Long} unique DB ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns match ID
     * 
     * @return {@link Long} match ID
     */
    public Long getMatchId() {
        return matchId;
    }

    /**
     * Sets match ID
     * @param matchId {@link Long} match ID
     */
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    /**
     * Returns kill count
     * 
     * @return int kill count
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Returns death count
     * 
     * @return int death count
     */
    public int getDeaths() {
        return deaths;
    }
    
    /**
     * Team type
     * @return {@link UrTTeam} team type
     */
    public UrTTeam getTeamType() {
        return teamType;
    }
    
    /**
     * Calculates and returns kills-to-deaths ratio
     * 
     * @return double calculated kills-to-deaths ratio
     */
    public double getKDRatio() {
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
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deaths;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + kills;
        result = prime * result
                + ((teamType == null) ? 0 : teamType.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTTeamMatchStats other = (UrTTeamMatchStats) obj;
        if (deaths != other.deaths)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (kills != other.kills)
            return false;
        if (teamType != other.teamType)
            return false;
        return true;
    }

    public String toString() {
        return "UrTTeamMatchStats [id=" + id + ", teamType=" + teamType
                + ", kills=" + kills + ", deaths=" + deaths + "]";
    }

    public int compareTo(UrTTeamMatchStats o) {
        int result = 0;
        if(o != null){
            int otherKills = Integer.valueOf(o.getKills());
            result = Integer.valueOf(otherKills).compareTo(this.getKills());
            if(result == 0){
                result = Integer.valueOf(o.getDeaths()).compareTo(this.getDeaths());
            }
        }
        return result;
    }

}
