package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.chart.UrTPlayerMatchChartDataItem;

/**
 * UrT player match DAO interface
 * @author ghost
 *
 */
public interface UrTPlayerMatchDao extends UrTDao {

    /**
     * Returns player match stats by ID
     * 
     * @param id {@link Long} unique player match ID
     * 
     * @return {@link UrTPlayerMatchStats} player match stats
     * 
     * @throws NoSuchObjectException if there is no such object with the specified ID
     * @throws JdbcException on failure
     */
    public UrTPlayerMatchStats getPlayerMatchStatsById(long id) throws JdbcException, NoSuchObjectException;
    
    /**
     * Returns list of {@link UrTPlayerMatchStats} objects by match ID
     * 
     * @param matchId long match ID
     * @param isTeamBasedMatch boolean <code>true</code> - team-based match
     * 
     * @return {@link List} of {@link UrTPlayerMatchStats} objects ordered by KD-ratio descending
     * 
     * @throws JdbcException on failure
     */
    public List<UrTPlayerMatchStats> getPlayersByMatchId(long matchId, boolean isTeamBasedMatch) throws JdbcException;
    
    /**
     * Returns all player match detailed stats by player ID
     * 
     * @param playerId long - player ID
     * 
     * @return {@link List} of {@link UrTPlayerMatchChartDataItem} objects
     *  
     * @throws JdbcException on failure
     */
    public List<UrTPlayerMatchChartDataItem> getPlayerMatchStatsByPlayerId(long playerId) throws JdbcException;
    
    /**
     * Creates player match record
     * 
     * @param entity {@link UrTPlayerMatchStats} player match stats
     * 
     * @return long new record ID
     * 
     * @throws JdbcException on failure
     */
    public long createPlayerMatchStats(UrTPlayerMatchStats entity) throws JdbcException;
    
    /**
     * Delete all player matches by player ID
     * 
     * @param playerId long playerId
     * 
     * 
     * @throws JdbcException on failure
     */
    public void deleteAllPlayerMatches(long playerId) throws JdbcException;

}
