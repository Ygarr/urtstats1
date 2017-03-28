package urt.stats.core.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urt.stats.core.enums.UrTGameType;
import urt.stats.core.enums.UrTTeam;

/**
 * Game match stats VO
 * 
 * @author ghost
 *
 */
public class UrTMatchStats extends UrTMatch {
    
    /**
     * Player List
     */
    private List<UrTPlayerMatchStats> playerList = new ArrayList<UrTPlayerMatchStats>();
    
    /**
     * Team stats
     */
    private Map<UrTTeam, UrTTeamMatchStats> teams = new HashMap<UrTTeam, UrTTeamMatchStats>();
    
    /**
     * Constructor
     */
    public UrTMatchStats(){
        super();
    }
    
    /**
     * Constructor with parent object
     * 
     * @param match {@link UrTMatch} match object
     */
    public UrTMatchStats(UrTMatch match){
        super(
                match.getId()
              , match.getMap()
              , match.getLogFileName()
              , match.getGameType()
              , match.getStartTime()
              , match.getEndTime());
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
    public UrTMatchStats(
            Long id
          , UrTMap map
          , String logFileName
          , UrTGameType gameType
          , Date startTime
          , Date endTime) {
        super(
                id
              , map
              , logFileName
              , gameType
              , startTime
              , endTime);
    }

    /**
     * Adds player match stats
     * 
     * @param playerMatchStats {@link UrTPlayerMatchStats} player match stats
     */
    public void addPlayer(UrTPlayerMatchStats playerMatchStats) {
        this.playerList.add(playerMatchStats);
    }
    
    /**
     * Returns player list
     * 
     * @return {@link List} of {@link UrTPlayerMatchStats} objects 
     */
    public List<UrTPlayerMatchStats> getPlayerList() {
        return playerList;
    }

    /**
     * Sets player list (and fills player map)
     * 
     * @param playerList {@link List} of {@link UrTPlayerMatchStats} objects
     */
    public void setPlayerList(List<UrTPlayerMatchStats> playerList) {
        this.playerList = playerList;
    }

    /**
     * Returns match teams
     * @return {@link Map} match teams
     */
    public Map<UrTTeam, UrTTeamMatchStats> getTeams() {
        return teams;
    }
    
    /**
     * Returns list of two teams (for team based match): red, blue
     * 
     * @return {@link List} of two teams (for team based match): red, blue
     */
    public List<UrTTeamMatchStats> getTeamList() {
        List<UrTTeamMatchStats> result = new ArrayList<UrTTeamMatchStats>();
        if(isTeamBasedMatch()){
            result.add(getTeams().get(UrTTeam.RED));
            result.add(getTeams().get(UrTTeam.BLUE));
        }
        return result;
    }

    /**
     * Fills team map with match team list
     *  
     * @param teamList {@link List} of {@link UrTTeamMatchStats} objects
     */
    public void setTeams(List<UrTTeamMatchStats> teamList){
        if(teamList != null && !teamList.isEmpty()){
            for(UrTTeamMatchStats team : teamList){
                this.teams.put(team.getTeamType(), team);
            }
        }
    }
    
    
}
