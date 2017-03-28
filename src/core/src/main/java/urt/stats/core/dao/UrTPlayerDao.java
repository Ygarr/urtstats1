package urt.stats.core.dao;


import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT player DAO interface
 * 
 * @author ghost
 *
 */
public interface UrTPlayerDao extends UrTDao {

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
     * Returns Player total stats by player name
     * 
     * @param name {@link String} player name 
     * 
     * @return {@link UrTPlayerStats} player stats
     *
     * @throws NoSuchObjectException if there is no such player
     * @throws JdbcException on failure 
     */
    public UrTPlayerStats getPlayerStatsByName(String name) throws NoSuchObjectException, JdbcException;

    /**
     * Checks for player existance by player name and returns player ID
     * 
     * @param name {@link String} player name
     * 
     * @return {@link Long} player ID or <code>null</code> if player does not exist
     * 
     * @throws JdbcException on failure
     */
    public Long getPlayerIdByName(String name) throws JdbcException;

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
     * Creates player alias
     * 
     * @param playerId long - player ID
     * @param alias {@link String} player alias
     * 
     * @throws JdbcException on failure
     */
    public void createPlayerAlias(long playerId, String alias) throws JdbcException;

    /**
     * Updates player total stats
     * 
     * @param playerStats {@link UrTPlayerStats} player stats
     * 
     * @throws JdbcException on failure
     */
    public void updatePlayerStats(UrTPlayerStats playerStats) throws JdbcException;
    
    /**
     * Updates player name
     * 
     * @param playerId long player ID
     * @param playerName {@link String} new player name
     * 
     * @throws JdbcException
     */
    public void updatePlayerName(long playerId, String playerName) throws JdbcException;
    
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
     * Delete player by player ID
     * 
     * @param playerId long playerId
     * 
     * @throws NoSuchObjectException if there is no such player
     * @throws JdbcException on failure
     */
    public void deletePlayer(long playerId) throws NoSuchObjectException, JdbcException;

    /**
     * Delete player aliases by player ID
     * 
     * @param playerId long playerId
     * 
     * @throws NoSuchObjectException if there is no player aliases with the specified ID
     * @throws JdbcException on failure
     */
    public void deletePlayerAliases(long playerId) throws NoSuchObjectException, JdbcException;
}
