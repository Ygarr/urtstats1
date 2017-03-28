package urt.stats.core.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.dao.UrTMatchDao;
import urt.stats.core.dao.UrTPlayerMatchAchievementDao;
import urt.stats.core.dao.UrTPlayerMatchDao;
import urt.stats.core.dao.UrTPlayerMatchHitDao;
import urt.stats.core.dao.UrTTeamMatchDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTMatchManager;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTMatchCountAchv;
import urt.stats.core.vo.UrTMatchHitStat;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTSortHelper;
import urt.stats.core.vo.UrTTeamMatchStats;

/**
 * UrT Match manager implementation
 * 
 * @author ghost
 *
 */
@Service
@Transactional(readOnly = true)
public class UrTMatchManagerImpl extends UrTAbstractManager implements UrTMatchManager {

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * UrT match DAO
     */
    @Autowired
    protected UrTMatchDao matchDao;
    
    /**
     * UrT player match DAO
     */
    @Autowired
    protected UrTPlayerMatchDao playerMatchDao;
    
    /**
     * UrT team match DAO
     */
    @Autowired
    protected UrTTeamMatchDao teamMatchDao;
 
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

    public List<UrTMatch> getMatchPagedList(int offset, int limit, UrTSortHelper sortHelper) throws JdbcException {
        return matchDao.getMatchPagedList(offset, limit, sortHelper);
    }
    
    public List<UrTMatch> getMatchPagedListByPlayerId(long playerId, int offset, int limit, UrTSortHelper sortHelper) throws JdbcException {
        return matchDao.getMatchPagedListByPlayerId(playerId, offset, limit, sortHelper);
    }

    public List<UrTMatch> getMatchListForCurrentDay(UrTSortHelper sortHelper) throws JdbcException{
        return matchDao.getMatchListForCurrentDay(sortHelper);
    }
    
    public List<UrTMatch> getMatchListForMap(long mapId, UrTSortHelper sortHelper) throws JdbcException{
        return matchDao.getMatchListForMap(mapId, sortHelper);
    }

    public UrTMatch getMatch(long matchId) throws NoSuchObjectException, JdbcException {
        return matchDao.getMatchById(matchId);
    }
    
    public UrTMatchStats getMatchDetails(long matchId) throws NoSuchObjectException, JdbcException {
        UrTMatchStats matchStats = matchDao.getMatchStatsById(matchId);
        List<UrTPlayerMatchStats> players = playerMatchDao.getPlayersByMatchId(matchId, matchStats.isTeamBasedMatch());
        matchStats.setPlayerList(players);
        if(matchStats.isTeamBasedMatch()){
            List<UrTTeamMatchStats> teamList = teamMatchDao.getTeamMatchStatsByMatchId(matchId);
            matchStats.setTeams(teamList);
        }
        
        return matchStats;
    }

    public int getMatchCount() throws JdbcException{
        return matchDao.getMatchCount();
    }
    
    public int getMatchCountByPlayerId(long playerId) throws JdbcException{
        return matchDao.getMatchCountByPlayerId(playerId);
    }

    
    protected UrTDao getDao() {
        return this.matchDao;
    }

    public List<UrTMatchHitStat> getMatchBestHits(long matchId) throws JdbcException {
        return playerMatchHitDao.getMatchBestHits(matchId);
    }

    public List<UrTMatchCountAchv> getMatchBestAchievs(long matchId) throws JdbcException {
        return playerMatchAchvDao.getMatchBestAchievs(matchId);
    }

    public UrTSortHelper getSortHelper(int column, boolean isAsc) {
        return matchDao.getSortHelper(column, isAsc);
    }

}
