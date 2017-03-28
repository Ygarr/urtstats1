package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT match DAO interface
 * 
 * @author ghost
 *
 */
public interface UrTMatchDao extends UrTDao{

    /**
     * Returns match by ID
     * 
     * @param id long - match unique ID
     * 
     * @return {@link UrTMatch} match stats
     *  
     * @throws NoSuchObjectException if there is no such object with the specified ID
     * @throws JdbcException on failure
     */
    public UrTMatch getMatchById(long id) throws NoSuchObjectException, JdbcException;

    
    /**
     * Returns match stats by ID
     * 
     * @param id long - match unique ID
     * 
     * @return {@link UrTMatchStats} match stats
     *  
     * @throws NoSuchObjectException if there is no such object with the specified ID
     * @throws JdbcException on failure
     */
    public UrTMatchStats getMatchStatsById(long id) throws NoSuchObjectException, JdbcException;
    
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
     * Creates match stats record and returns created unique ID
     * 
     * @param entity {@link UrTMatchStats} match stats entity
     * 
     * @return long created unique ID
     * 
     * @throws JdbcException on failure
     */
    public long createMatchStats(UrTMatchStats entity) throws JdbcException;
    
}
