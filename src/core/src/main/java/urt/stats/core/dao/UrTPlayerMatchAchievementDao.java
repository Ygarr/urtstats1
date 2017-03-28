package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.vo.UrTMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;

/**
 * UrT player match achievement DAO interface
 * @author ghost
 *
 */
public interface UrTPlayerMatchAchievementDao extends UrTDao {

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
     * Returns list of best achievs for match
     * 
     * @param matchId long - match Id
     * 
     * @return {@link List} of {@link UrTMatchCountAchv} - list of best achievs for match
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatchCountAchv> getMatchBestAchievs(long matchId) throws JdbcException;

    /**
     * Stores list of player match achievements to DB
     * 
     * @param playerMatchAchvs {@link List} of {@link UrTPlayerMatchHitStat} objects
     * @param playerMatchId  {@link Long} player match ID
     * 
     * @throws JdbcException on failure
     */
    public void savePlayerMatchAchvs(List<UrTPlayerMatchCountAchv> playerMatchAchvs, Long playerMatchId) throws JdbcException;
    
    /**
     * Delete all player match achievs by player ID
     * 
     * @param playerId long playerId
     * 
     * 
     * @throws JdbcException on failure
     */
    public void deleteAllMatchAchvs(long playerId) throws JdbcException;

}
