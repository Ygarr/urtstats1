package urt.stats.core.service;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTMatchCountAchv;
import urt.stats.core.vo.UrTMatchHitStat;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT Match manager interface
 * 
 * @author ghost
 *
 */
public interface UrTMatchManager extends UrTManager {

    /**
     * Returns UrT match paged list.<br/>
     * 
     * @param offset int - rows to skip
     * @param limit int - rows per page
     * @param sortHelper {@link UrTSortHelper} sort helper
     * 
     * @return {@link List} of {@link UrTMatchStats} objects
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatch> getMatchPagedList(int offset, int limit, UrTSortHelper sortHelper) throws JdbcException;
    
    /**
     * Returns UrT match paged list for the specified player.<br/>
     * 
     * @param playerId - player ID
     * @param offset int - rows to skip
     * @param limit int - rows per page
     * @param sortHelper {@link UrTSortHelper} sort helper
     * 
     * @return {@link List} of {@link UrTMatchStats} objects
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatch> getMatchPagedListByPlayerId(long playerId, int offset, int limit, UrTSortHelper sortHelper) throws JdbcException;
    
    /**
     * Returns UrT match list for current day.<br/>
     * <b>Notice:</b> each {@link UrTMatchStats} contains only math details (no child objects such as player info etc.).
     * 
     * @param sortHelper {@link UrTSortHelper} sort helper
     * 
     * @return {@link List} of {@link UrTMatchStats} objects for current day
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatch> getMatchListForCurrentDay(UrTSortHelper sortHelper) throws JdbcException;

    /**
     * Returns UrT match list for the specified map.<br/>
     * 
     * @param mapId long - map ID
     * @param sortHelper {@link UrTSortHelper} sort helper
     * 
     * @return {@link List} of {@link UrTMatchStats} objects for the specified map
     * 
     * @throws JdbcException on failure
     */
    public List<UrTMatch> getMatchListForMap(long mapId, UrTSortHelper sortHelper) throws JdbcException;

    /**
     * Returs match details
     * 
     * @param matchId long - match ID
     * 
     * @return {@link UrTMatch} match details
     * 
     * @throws NoSuchObjectException if there is no such objct with the specified ID
     * @throws JdbcException on failure
     */
    public UrTMatch getMatch(long matchId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returs full match details
     * 
     * @param matchId long - match ID
     * 
     * @return {@link UrTMatchStats} match details
     * 
     * @throws NoSuchObjectException if there is no such objct with the specified ID
     * @throws JdbcException on failure
     */
    public UrTMatchStats getMatchDetails(long matchId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns match count
     * 
     * @return int - match count
     * 
     * @throws JdbcException on failure
     */
    public int getMatchCount() throws JdbcException;
    
    /**
     * Returns match count by player ID
     * 
     * @param playerId - player ID
     * 
     * @return int - match count
     * 
     * @throws JdbcException on failure
     */
    public int getMatchCountByPlayerId(long playerId) throws JdbcException;
    
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
     * Returns sort helper with the specified sort column
     * 
     * @param column int - sort column number
     * @param isAsc boolean sort order
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper(int column, boolean isAsc);

}
