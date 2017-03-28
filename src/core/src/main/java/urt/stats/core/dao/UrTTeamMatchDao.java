package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.vo.UrTTeamMatchStats;

/**
 * UrT Team match DAO interface
 * @author ghost
 *
 */
public interface UrTTeamMatchDao extends UrTDao {

    /**
     * Returns team match stats by ID
     * 
     * @param id {@link Long} unique DB ID
     * 
     * @return {@link UrTTeamMatchStats} team match stats
     * 
     * @throws JdbcException on failure
     */
    public UrTTeamMatchStats getTeamMatchStatsById(long id) throws JdbcException;

    /**
     * Returns list team match stats by match ID
     * 
     * @param matchId {@link Long} unique DB match ID
     * 
     * @return {@link List} of  {@link UrTTeamMatchStats} team match stats
     * 
     * @throws JdbcException on failure
     */
    public List<UrTTeamMatchStats> getTeamMatchStatsByMatchId(long matchId) throws JdbcException;
    
    /**
     * Creates team match record
     * 
     * @param entity {@link UrTTeamMatchStats} team match
     * 
     * @return long new record ID
     * 
     * @throws JdbcException on failure
     */
    public long createTeamMatchStats(UrTTeamMatchStats entity) throws JdbcException;
    
}
