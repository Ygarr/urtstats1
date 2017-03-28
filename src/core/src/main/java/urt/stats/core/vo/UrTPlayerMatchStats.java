package urt.stats.core.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import urt.stats.core.enums.UrTTeam;
import urt.stats.core.enums.UrTWeapon;

/**
 * Player match stats VO
 * 
 * @author ghost
 *
 */
public class UrTPlayerMatchStats implements Comparable<UrTPlayerMatchStats> {

    /**
     * Unique player match ID in DB
     */
    private Long id = null;
    
    /**
     * Player ID in DB
     */
    private Long playerId = null;
    
    /**
     * Player ingame ID
     */
    private int playerIngameId = -1;
    
    /**
     * Match ID in DB
     */
    private Long matchId = null;
    
    /**
     * Player team
     */
    private UrTTeam playerTeam = null;
 
    /**
     * Player name
     */
    private String playerName = null;

    /**
     * Kill count (how many foes player had killed)
     */
    private int kills = 0;
    
    /**
     * Death count (how many times player had died)
     */
    private int deaths = 0;
    
    /**
     * Player has hit stats
     */
    private boolean hasHits = false;
    
    /**
     * Player has achievements
     */
    private boolean hasAchvs = false;
    
    /**
     * Player hits
     */
    private List<UrTPlayerMatchHitStat> playerMatchHits = new ArrayList<UrTPlayerMatchHitStat>();
    
    /**
     * Player match q achievements
     */
    private List<UrTPlayerMatchCountAchv> playerMatchAchvs = new ArrayList<UrTPlayerMatchCountAchv>();
    
    /**
     * Player weapon stats
     */
    private Map<UrTWeapon, UrTPlayerWeaponStats> playerWpns = new LinkedHashMap<UrTWeapon, UrTPlayerWeaponStats>();
    
    // TODO refactor to self-created VO structures
    /**
     * Kills count total by player
     */
    private Map<UrTPlayerMatchStats, AtomicInteger> killsByPlayer = new HashMap<UrTPlayerMatchStats, AtomicInteger>();
    
    
    
    /**
     * Default constructor
     */
    public UrTPlayerMatchStats(){
        
    }
    
    /**
     * Parameterized constructor (player name)
     *  
     * @param playerName {@link String} player name
     */
    public UrTPlayerMatchStats(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Parameterized constructor (all fields)
     *  
     * @param id {@link Long} Unique player match ID in DB
     * @param playerId {@link Long} Player ID in DB
     * @param playerName {@link String} Player name
     * @param matchId {@link Long} Match ID in DB
     * @param playerTeam {@link UrTTeam} player team
     * @param kills int Kill count (how many foes player had killed)
     * @param deaths int Death count (how many times player had died)
     * @param hasHits boolean - player has hit stats
     * @param hasAchvs boolean - player has ahievements
     */
    public UrTPlayerMatchStats(
             Long id
           , Long playerId
           , String playerName
           , Long matchId
           , UrTTeam playerTeam
           , int kills
           , int deaths
           , boolean hasHits
           , boolean hasAchvs) {
        this.id = id;
        this.playerId = playerId;
        this.playerName = playerName;
        this.matchId = matchId;
        this.playerTeam = playerTeam;
        this.kills = kills;
        this.deaths = deaths;
        this.hasHits = hasHits;
        this.hasAchvs = hasAchvs;
    }

    /**
     * Calculates and returns match kills-to-deaths ratio
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

    /**
     * Player weapon stats
     * @return {@link Map} player weapon stats
     */
    public Map<UrTWeapon, UrTPlayerWeaponStats> getPlayerWpns() {
        return playerWpns;
    }

    /**
     * Increments kills by player counter
     * @param defenderStats {@link UrTPlayerMatchStats} defender player stats
     */
    public void addKillByPlayer(UrTPlayerMatchStats defenderStats){
        if(killsByPlayer.containsKey(defenderStats)){
            killsByPlayer.get(defenderStats).incrementAndGet();
        } else {
            killsByPlayer.put(defenderStats, new AtomicInteger(1));
        }
    }
    
    /**
     * Returns kill count by player
     * 
     * @param playerStats {@link UrTPlayerMatchStats} player
     * 
     * @return int - kill ocunt by player
     */
    public int getKillCountByPlayer(UrTPlayerMatchStats playerStats){
        int result = 0;
        AtomicInteger count = killsByPlayer.get(playerStats);
        if(count != null){
            result = count.intValue();
        }
        return result;
    }
    
    /**
     * Returns unique player match ID from DB
     * @return {@link Long} unique player match ID from DB
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique player match ID from DB
     * @param id {@link Long} unique player match ID from DB
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns player ID from DB
     * @return {@link Long} player ID from DB
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets player ID fom DB
     * @param playerId {@link Long} player ID from DB
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns ingame inner player id
     * 
     * @return int ingame inner player id
     */
    public int getPlayerIngameId() {
        return this.playerIngameId;
    }

    /**
     * Sets ingame inner player id
     * 
     * @param playerIngameId int ingame inner player id
     */
    public void setPlayerIngameId(int playerIngameId) {
        this.playerIngameId = playerIngameId;
    }
    
    /**
     * Returns match ID from DB
     * @return {@link Long} match ID from DB
     */
    public Long getMatchId() {
        return matchId;
    }

    /**
     * Sets match ID from DB
     * @param matchId {@link Long} match ID from DB
     */
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    /**
     * Returns player team 
     * @return {@link UrTTeam} player team
     */
    public UrTTeam getPlayerTeam() {
        return playerTeam;
    }

    /**
     * Sets player Team
     * @param playerTeam {@link UrTTeam}
     */
    public void setPlayerTeam(UrTTeam playerTeam) {
        this.playerTeam = playerTeam;
    }

    /**
     * Returns player name
     * @return {@link String} player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets player name
     * @param playerName {@link String} player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns kill count (how many foes player had killed)
     * @return int kill count
     */
    public int getKills() {
        return kills;
    }

    /**
     * Sets player kill count
     * @param kills int - player kill count (how many foes player had killed) 
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * Increments player kill count (how many foes player had killed)
     */
    public void incrementKills(){
        this.kills ++;
    }
    
    /**
     * Decrements player kill count (how many foes player had killed)
     */
    public void decrementKills(){
        this.kills --;
    }
    
    /**
     * Returns player death count (how many times player had died)
     * @return int - player death count
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Sets player death count (how many times player had died)
     * @param deaths int - player death count 
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * Increments player death count (how many times player had died)
     */
    public void incrementDeaths(){
        this.deaths ++;
    }
    
    /**
     * Returns has hits sign
     * 
     * @return boolean <code>true</code> player has hit statistics
     */
    public boolean hasHits() {
        return hasHits;
    }

    /**
     * Returns has achievements sign
     * 
     * @return boolean <code>true</code> player has achievements
     */
    public boolean hasAchvs() {
        return hasAchvs;
    }

    /**
     * Returns player match hits on targets
     * 
     * @return {@link List} of {@link UrTPlayerMatchHitStat} objects
     */
    public List<UrTPlayerMatchHitStat> getPlayerMatchHits() {
        return playerMatchHits;
    }

    /**
     * Sets player match hits on targets
     * 
     * @param playerMatchHits {@link List} of {@link UrTPlayerMatchHitStat} objects
     */
    public void setPlayerMatchHits(List<UrTPlayerMatchHitStat> playerMatchHits) {
        this.playerMatchHits = playerMatchHits;
    }

    /**
     * Returns player match Q achievements
     * 
     * @return {@link List} of {@link UrTPlayerMatchCountAchv} objects
     */
    public List<UrTPlayerMatchCountAchv> getPlayerMatchAchvs() {
        return playerMatchAchvs;
    }

    /**
     * Sets player match Q achievements
     * 
     * @param playerMatchAchvs {@link List} of {@link UrTPlayerMatchCountAchv} objects
     */
    public void setPlayerMatchAchvs(
            List<UrTPlayerMatchCountAchv> playerMatchAchvs) {
        this.playerMatchAchvs = playerMatchAchvs;
    }

    /**
     * Returns <code>true</code> if player had skipped the match
     * 
     * @return <code>true</code> if player had skipped the match
     */
    public boolean isSkippedMatch() {
        boolean result = false;
        result = getPlayerMatchHits().isEmpty()
              && getPlayerMatchAchvs().isEmpty()
              && getPlayerWpns().isEmpty();
        return result;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deaths;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + kills;
        result = prime * result + ((matchId == null) ? 0 : matchId.hashCode());
        result = prime * result
                + ((playerId == null) ? 0 : playerId.hashCode());
        result = prime * result + playerIngameId;
        result = prime * result
                + ((playerName == null) ? 0 : playerName.hashCode());
        result = prime * result
                + ((playerTeam == null) ? 0 : playerTeam.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerMatchStats other = (UrTPlayerMatchStats) obj;
        if (deaths != other.deaths)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (kills != other.kills)
            return false;
        if (matchId == null) {
            if (other.matchId != null)
                return false;
        } else if (!matchId.equals(other.matchId))
            return false;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        } else if (!playerId.equals(other.playerId))
            return false;
        if (playerIngameId != other.playerIngameId)
            return false;
        if (playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!playerName.equals(other.playerName))
            return false;
        if (playerTeam != other.playerTeam)
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerMatch [id=" + id + ", playerId=" + playerId
                + ", playerIngameId=" + playerIngameId + ", matchId=" + matchId
                + ", playerTeam=" + playerTeam + ", playerName=" + playerName
                + ", kills=" + kills + ", deaths=" + deaths + "]";
    }

    /**
     * Compares two players by kill and death count
     */
    public int compareTo(UrTPlayerMatchStats o) {
        int result = 0;
        if(o != null){
            Double otherKills = Double.valueOf(o.getKDRatio());
            result = otherKills.compareTo(this.getKDRatio());
            if(result == 0){
                result = Integer.valueOf(o.getKills()).compareTo(this.getKills());
            }
        }
        return result;
    }

}
