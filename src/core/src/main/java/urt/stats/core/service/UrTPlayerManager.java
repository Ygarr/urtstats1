package urt.stats.core.service;

import java.util.List;
import java.util.Map;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTPlayerWeaponStats;
import urt.stats.core.vo.UrTSortHelper;
import urt.stats.core.vo.chart.UrTPlayerMatchChartDataItem;

/**
 * UrT Player manager interface
 * 
 * @author ghost
 *
 */
public interface UrTPlayerManager extends UrTManager {

    /**
     * Returns list of player match hits on target
     * 
     * @param playerMatchId {@link Long} player match ID
     * 
     * @return {@link List} of {@link UrTPlayerMatchHitStat} objects
     * 
     * @throws JdbcException on failure
     */
    public List<UrTPlayerMatchHitStat> getPlayerMatchHits(long playerMatchId) throws JdbcException;
    
    /**
     * Returns list fo all player hits
     * 
     * @param playerId player ID
     * 
     * @return {@link List} of {@link UrTPlayerMatchHitStat} objects
     * 
     * @throws JdbcException on failure
     */
    public List<UrTPlayerMatchHitStat> getAllPlayerHits(long playerId) throws JdbcException;

    /**
     * Returns list of player match achievements
     * 
     * @param playerMatchId {@link Long} player match ID
     * 
     * @return {@link List} of {@link UrTPlayerMatchHitStat} objects
     * 
     * @throws JdbcException on failure
     */
    public List<UrTPlayerMatchCountAchv> getPlayerMatchAchvs(long playerMatchId) throws JdbcException;

    /**
     * Returns list of player achievements
     * 
     * @param playerId {@link Long} player match ID
     * 
     * @return {@link List} of {@link UrTPlayerMatchHitStat} objects
     * 
     * @throws JdbcException
     */
    public List<UrTPlayerMatchCountAchv> getAllPlayerAchvs(long playerId) throws JdbcException;
    
    /**
     * Returns Player total stats by player ID
     * 
     * @param id {@link Long} player unique ID in DB 
     * 
     * @return {@link UrTPlayerStats} player stats
     * 
     * @throws NoSuchObjectException if there is no such player
     * @throws JdbcException on failure 
     */
    public UrTPlayerStats getPlayerStatsById(long id) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns Player total stats for all players ordered by rate
     * 
     * @param sortHelper {@link UrTSortHelper} sortHelper
     * 
     * @return {@link List} of {@link UrTPlayerStats} player stats
     * 
     * @throws JdbcException on failure 
     */
    public List<UrTPlayerStats> getAllPlayerStats(UrTSortHelper sortHelper) throws JdbcException;

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
     * Returns player match stats by ID
     * 
     * @param playerMatchId {@link Long} unique player match ID
     * 
     * @return {@link UrTPlayerMatchStats} player match stats
     * 
     * @throws NoSuchObjectException if there is no such object with the specified ID
     * @throws JdbcException on failure
     */
    public UrTPlayerMatchStats getPlayerMatchStatsById(long playerMatchId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns player weapon stats based on weapon type
     * 
     * @param playerId player ID
     * 
     * @return {@link Map} player weapon stats with weapon type as key and list of weapon stats as value
     * 
     * @throws JdbcException on failure
     */
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerWpnStats(long playerId) throws JdbcException;
    
    /**
     * Returns player match weapon stats based on weapon type
     * 
     * @param playerMatchId player match ID
     * 
     * @return {@link Map} player weapon stats with weapon type as key and list of weapon stats as value
     * 
     * @throws JdbcException on failure
     */
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerMatchWpnStats(long playerMatchId) throws JdbcException;
    
    /**
     * Creates Player stats record
     * 
     * @param entity {@link UrTPlayerStats} player stats
     * 
     * @return long - new player ID
     * 
     * @throws JdbcException on failure
     */
    public long createPlayerStats(UrTPlayerStats entity) throws JdbcException;

    /**
     * Deletes player (and all related records) by player ID
     * 
     * @param playerId long - player ID
     * 
     * @throws NoSuchObjectException if there is no such player
     * @throws JdbcException on failure
     */
    public void deletePlayer(long playerId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Creates player alias
     * 
     * @param playerId long - player ID
     * @param alias {@link String} player alias
     * 
     * @throws NoSuchObjectException if there is no such player
     * @throws JdbcException on failure
     */
    public void createPlayerAlias(long playerId, String alias) throws NoSuchObjectException, JdbcException;

    /**
     * Returns sort helper with the specified sort column
     * 
     * @param column int - sort column number
     * @param isAsc boolean sort order
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper(int column, boolean isAsc);

}
