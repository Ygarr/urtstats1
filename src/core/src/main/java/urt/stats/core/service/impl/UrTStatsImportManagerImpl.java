package urt.stats.core.service.impl;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.dao.UrTMapDao;
import urt.stats.core.dao.UrTMatchDao;
import urt.stats.core.dao.UrTPlayerDao;
import urt.stats.core.dao.UrTPlayerMatchAchievementDao;
import urt.stats.core.dao.UrTPlayerMatchDao;
import urt.stats.core.dao.UrTPlayerMatchHitDao;
import urt.stats.core.dao.UrTPlayerWpnsDao;
import urt.stats.core.dao.UrTTeamMatchDao;
import urt.stats.core.enums.UrTTeam;
import urt.stats.core.exception.EmptyLogFileException;
import urt.stats.core.exception.ImportFailedException;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.service.UrTStatsImportManager;
import urt.stats.core.util.TimingAnalyzer;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTTeamMatchStats;

/**
 * Stats import manager implementation.
 * Calculates parsed stats and stores to DB
 * 
 * @author ghost
 *
 */
@Service("statsImportManager")
@Transactional(readOnly = true)
public class UrTStatsImportManagerImpl extends UrTAbstractManager implements UrTStatsImportManager{

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * Player manager
     */
    @Autowired
    protected UrTPlayerManager playerManager;
    
    /**
     * UrT match DAO
     */
    @Autowired
    protected UrTMatchDao matchDao;
    
    /**
     * UrT map DAO
     */
    @Autowired
    protected UrTMapDao mapDao;
    
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
     * Player match hit DAO
     */
    @Autowired
    protected UrTPlayerMatchHitDao playerMatchHitDao;
    
    /**
     * Player match achievement DAO
     */
    @Autowired
    protected UrTPlayerMatchAchievementDao playerMatchAchvDao;
    
    /**
     * Player weapon stats DAO
     */
    @Autowired
    protected UrTPlayerWpnsDao playerWeaponStatsDao;
    
    /**
     * UrT team match DAO
     */
    @Autowired
    protected UrTTeamMatchDao teamMatchDao;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void importMatchStats(UrTMatchStats matchStats) throws EmptyLogFileException, ImportFailedException{
        if(matchStats.getPlayerList().isEmpty()){
            throw new EmptyLogFileException("There are no players, nothing to import.", null);
        }
        TimingAnalyzer timer = new TimingAnalyzer(logger);
        timer.start();
        logger.info("DB import started...");
        long matchId = -1;
        try{
            UrTMap map = mapDao.getMapByFileName(matchStats.getMap().getFileName());
            // map exists
            matchStats.getMap().setId(map.getId());
            mapDao.incrementMatchCount(map.getId());
        } catch (JdbcException e){
            throw new ImportFailedException("Failed to get map by file name '" + matchStats.getMap().getFileName() + "'", e);
        } catch (NoSuchObjectException e) {
            logger.debug("Map '" + matchStats.getMap().getFileName() + "' not found, will be created.");
            try {
                matchStats.getMap().setMatchCount(1);
                mapDao.createMap(matchStats.getMap());
            } catch (JdbcException e1) {
                throw new ImportFailedException("Failed to create map", e);
            }
        }
        
        // saving match stats
        try {
            matchId = matchDao.createMatchStats(matchStats);
            matchStats.setId(matchId);
        } catch (JdbcException e) {
            throw new ImportFailedException("Failed to save match stats.", e);
        }
        
        
        // saving team match stats
        for(UrTTeamMatchStats teamMatchStats : matchStats.getTeams().values()) {
            try {
                teamMatchStats.setMatchId(matchId);
                teamMatchDao.createTeamMatchStats(teamMatchStats);
            } catch (JdbcException e) {
                throw new ImportFailedException("Failed to save team match stats.", e);
            }
        }
        
        // saving player match stats
        for(UrTPlayerMatchStats playerMatchStats : matchStats.getPlayerList()){
            try {
                playerMatchStats.setMatchId(matchId);
                if(UrTTeam.SPECTATOR != playerMatchStats.getPlayerTeam() && playerMatchStats.isSkippedMatch()) {
                    playerMatchStats.setPlayerTeam(UrTTeam.SKIPPED);
                }
                
                UrTPlayerStats playerStats = null;
                boolean isNewPlayer = false;
                try {
                    playerStats = playerDao.getPlayerStatsByName(playerMatchStats.getPlayerName());
                    // player exists 
                    playerMatchStats.setPlayerId(playerStats.getId());
                } catch (NoSuchObjectException e) {
                    // player does not exist in the DB, creating
                    playerStats = new UrTPlayerStats(playerMatchStats);
                    playerStats.setLastGameTime(matchStats.getStartTime());
                    long playerId = playerManager.createPlayerStats(playerStats);
                    playerMatchStats.setPlayerId(playerId);
                    isNewPlayer = true;
                }
                // creating player match stats record
                long playerMatchId = playerMatchDao.createPlayerMatchStats(playerMatchStats);
                if(!isNewPlayer){
                    // updating total player stats
                    playerStats.setLastGameTime(matchStats.getStartTime());
                    playerStats.updateStats(playerMatchStats);
                    if(!playerStats.getPlayerName().equals(playerMatchStats.getPlayerName())){
                        playerStats.setPlayerName(playerMatchStats.getPlayerName());
                    }
                    playerDao.updatePlayerStats(playerStats);
                }
                
                if(playerMatchStats.getPlayerMatchHits() != null && !playerMatchStats.getPlayerMatchHits().isEmpty()){
                    // saving player's hits on target
                    playerMatchHitDao.savePlayerMatchHits(playerMatchStats.getPlayerMatchHits(), playerMatchId);
                }
                if(playerMatchStats.getPlayerMatchAchvs() != null && !playerMatchStats.getPlayerMatchAchvs().isEmpty()){
                    // saving player's achievements
                    playerMatchAchvDao.savePlayerMatchAchvs(playerMatchStats.getPlayerMatchAchvs(), playerMatchId);
                }
                
                if(playerMatchStats.getPlayerWpns() != null && !playerMatchStats.getPlayerWpns().isEmpty()){
                    // saving player weapon stats
                    playerWeaponStatsDao.savePlayerWpnStats(playerMatchStats.getPlayerWpns(), playerMatchId);
                }
            } catch (JdbcException e) {
                throw new ImportFailedException("Failed to save player match stats.", e);
            }
        }
        
        StringBuffer sb = new StringBuffer("DB import finished. ");
        timer.stop(sb, Priority.INFO);
    }
    

    protected UrTDao getDao() {
        return this.matchDao;
    }

}
