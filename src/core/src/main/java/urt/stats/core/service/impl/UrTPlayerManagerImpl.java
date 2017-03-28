package urt.stats.core.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.dao.UrTPlayerDao;
import urt.stats.core.dao.UrTPlayerMatchAchievementDao;
import urt.stats.core.dao.UrTPlayerMatchDao;
import urt.stats.core.dao.UrTPlayerMatchHitDao;
import urt.stats.core.dao.UrTPlayerWpnsDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTPlayerWeaponStats;
import urt.stats.core.vo.UrTSortHelper;
import urt.stats.core.vo.chart.UrTPlayerMatchChartDataItem;

/**
 * UrT Player manager implmentation
 * 
 * @author ghost
 *
 */
@Service("playerManager")
@Transactional(readOnly = true)
public class UrTPlayerManagerImpl extends UrTAbstractManager implements UrTPlayerManager{

    /**
     * UrT player DAO
     */
    @Autowired
    protected UrTPlayerDao playerDao;
    
    /**
     * UrT player match DAO
     */
    @Autowired
    protected UrTPlayerMatchDao playerMatchDao;
    
    /**
     * UrT player match hits DAO
     */
    @Autowired
    protected UrTPlayerMatchHitDao playerMatchHitDao;

    /**
     * UrT player match achievements DAO
     */
    @Autowired
    protected UrTPlayerMatchAchievementDao playerMatchAchvDao;

    /**
     * Player weapon stats DAO
     */
    @Autowired
    protected UrTPlayerWpnsDao playerWeaponStatsDao;
    
    /**
     * Player weapon stats DAO
     */
    @Autowired
    protected UrTPlayerWpnsDao playerWpnsDao;
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    protected UrTDao getDao() {
        return this.playerDao;
    }

    public List<UrTPlayerMatchHitStat> getPlayerMatchHits(long playerMatchId) throws JdbcException {
        return playerMatchHitDao.getPlayerMatchHits(playerMatchId);
    }
    
    public List<UrTPlayerMatchHitStat> getAllPlayerHits(long playerId) throws JdbcException {
        return playerMatchHitDao.getAllPlayerHits(playerId);
    }

    public List<UrTPlayerMatchCountAchv> getPlayerMatchAchvs(long playerMatchId) throws JdbcException {
        return playerMatchAchvDao.getPlayerMatchAchvs(playerMatchId);
    }
    
    public List<UrTPlayerMatchCountAchv> getAllPlayerAchvs(long playerId) throws JdbcException {
        return playerMatchAchvDao.getAllPlayerAchvs(playerId);
    }
    
    public UrTPlayerMatchStats getPlayerMatchStatsById(long playerMatchId) throws NoSuchObjectException, JdbcException {
        return playerMatchDao.getPlayerMatchStatsById(playerMatchId);
    }
    
    public UrTPlayerStats getPlayerStatsById(long id) throws JdbcException, NoSuchObjectException{
        return playerDao.getPlayerStatsById(id);
    }

    public List<UrTPlayerStats> getAllPlayerStats(UrTSortHelper sortHelper) throws JdbcException {
        return playerDao.getAllPlayerStats(sortHelper);
    }
    
    public List<UrTPlayerMatchChartDataItem> getPlayerMatchStatsByPlayerId(long playerId) throws JdbcException {
        return playerMatchDao.getPlayerMatchStatsByPlayerId(playerId);
    }
    
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerWpnStats(long playerId) throws JdbcException {
        return playerWeaponStatsDao.getPlayerWpnStats(playerId);
    }
    
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerMatchWpnStats(long playerMatchId) throws JdbcException {
        return playerWeaponStatsDao.getPlayerMatchWpnStats(playerMatchId);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long createPlayerStats(UrTPlayerStats entity) throws JdbcException{
        long playerId = playerDao.createPlayerStats(entity);
        playerDao.createPlayerAlias(playerId, entity.getPlayerName());
        return playerId;
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deletePlayer(long playerId) throws NoSuchObjectException, JdbcException{
        UrTPlayerStats playerStats = playerDao.getPlayerStatsById(playerId);
        if(logger.isDebugEnabled()){
            logger.debug("Deleting player: " + playerStats);
        }
        playerMatchHitDao.deleteAllMatchHits(playerId);
        playerWpnsDao.deleteAllWpnStats(playerId);
        playerMatchAchvDao.deleteAllMatchAchvs(playerId);
        playerMatchDao.deleteAllPlayerMatches(playerId);
        playerDao.deletePlayerAliases(playerId);
        playerDao.deletePlayer(playerId);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createPlayerAlias(long playerId, String alias) throws NoSuchObjectException, JdbcException {
        UrTPlayerStats playerStats = null;
        try{
            logger.info("Getting player stats by alias");
            playerStats = playerDao.getPlayerStatsByName(alias);
            logger.info("Got player stats: " + playerStats.toString());
        } catch (NoSuchObjectException e){
            // ignore: normal situation
            logger.info("Alias is not used yet.");
        }
        boolean updateCurrentPlayerName = false;
        if(playerStats != null){
            if(playerStats.getId() != playerId){
                logger.info("Checking existing alias with player ID.");
                throw  new IllegalArgumentException("Alias '" + alias + "' is already defined for player with ID: " + playerStats.getId());
            } else {
                logger.info("Player with ID: " + playerId + " already has given alias '" + alias + "'.");
                if(!playerStats.getPlayerName().equals(alias)){
                    updateCurrentPlayerName = true;
                } else {
                    logger.info("Player with ID: " + playerId + " already has given alias '" + alias + "' and it is set to current name. Nothing to do.");
                    return;
                }
            }
        } else {
            playerDao.createPlayerAlias(playerId, alias);
            logger.info("New alias created");
            updateCurrentPlayerName = true;
        }
        if(updateCurrentPlayerName){
            playerDao.updatePlayerName(playerId, alias);
            logger.info("Player name updated");
        }
    }

    public UrTSortHelper getSortHelper(int column, boolean isAsc){
        return playerDao.getSortHelper(column, isAsc);
    }

    
}
