package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.vo.UrTMatchHitStat;
import urt.stats.core.vo.UrTPlayerMatchHitStat;

/**
 * Player match hit DAO interface
 * 
 * @author ghost
 *
 */
public interface UrTPlayerMatchHitDao extends UrTDao {

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
     * Returns list of best hits for match
     * 
     * @param matchId long - match Id
     * 
     * @return {@link List} of {@link UrTMatchHitStat} - list of best hits for match
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatchHitStat> getMatchBestHits(long matchId) throws JdbcException;
    
    /**
     * Stores list of player match hits on target to DB
     * 
     * @param playerMatchHits {@link List} of {@link UrTPlayerMatchHitStat} objects
     * @param playerMatchId  {@link Long} player match ID
     * 
     * @throws JdbcException on failure
     */
    public void savePlayerMatchHits(List<UrTPlayerMatchHitStat> playerMatchHits, Long playerMatchId) throws JdbcException;
    
    /**
     * Delete all player match hits by player ID
     * 
     * @param playerId long playerId
     * 
     * 
     * @throws JdbcException on failure
     */
    public void deleteAllMatchHits(long playerId) throws JdbcException;

}
