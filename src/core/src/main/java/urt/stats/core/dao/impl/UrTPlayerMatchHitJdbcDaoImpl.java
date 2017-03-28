package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTPlayerMatchHitDao;
import urt.stats.core.enums.UrTHitTarget;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMatchHitStat;
import urt.stats.core.vo.UrTPlayer;
import urt.stats.core.vo.UrTPlayerMatchHitStat;


/**
 * Player match hit DAO implementation
 * 
 * @author ghost
 *
 */
@Repository
public class UrTPlayerMatchHitJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTPlayerMatchHitDao{

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_pm_hits";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "pmh";
    
    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".player_match_id         AS pmh_player_match_id "
          + ", " + TABLE_ALIAS + ".target_id               AS pmh_target_id "
          + ", " + TABLE_ALIAS + ".hit_count               AS pmh_hit_count "
        ;
    
    /**
     * Table select query
     */
    protected static final String TABLE_SELECT = ""
            + "SELECT "
            + TABLE_COLUMNS
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " ";
    
    /**
     * Table select query: player match ID condition
     */
    protected static final String TABLE_WHERE_PLAYER_MATCH_ID = ""
            + " WHERE " + TABLE_ALIAS + ".player_match_id = :player_match_id ";
    
    /**
     * Table order by target ID
     */
    protected static final String TABLE_ORDER_BY_TARGET_ID = ""
            + " ORDER BY " + TABLE_ALIAS + ".target_id ";
    
    /**
     * Select player hit stats for all the time by player ID
     */
    protected static final String TABLE_SELECT_BY_PLAYER_ID = ""
            + "SELECT "
            + "      pm.player_id                       AS pmh_player_match_id "
            + ", " + TABLE_ALIAS + ".target_id          AS pmh_target_id "
            + ",     SUM(" + TABLE_ALIAS + ".hit_count) AS pmh_hit_count "
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + "JOIN " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + "  ON pm.id = " + TABLE_ALIAS + ".player_match_id "
            + " AND pm.player_id = :player_id "
            + " GROUP BY "
            + "       pm.player_id "
            + " , " + TABLE_ALIAS + ".target_id "
            + TABLE_ORDER_BY_TARGET_ID
            ;
    
    /**
     * Delete all hits for the specified player
     */
    protected static final String TABLE_DELETE_QUERY = ""
            + "DELETE FROM " + TABLE_NAME + " pmh "
            + " WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + " WHERE pmh.player_match_id = pm.id AND pm.player_id = :player_id) ";
    
    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  player_match_id "
            + ", target_id "
            + ", hit_count "
            + ")"
            + " VALUES ("
            + "  :player_match_id "
            + ", :target_id "
            + ", :hit_count "
            + ")"
          ;
    
    /**
     * Get best match hits query
     */
    protected static final String GET_BEST_MATCH_HITS_QUERY = ""
            + "SELECT "
            + "       max_pmh.p_id           AS player_id "
            + "     , max_pmh.p_name         AS player_name "
            + "     , max_pmh.pmh_target_id  AS target_id "
            + "     , max_pmh.pmh_hit_count  AS hit_count "
            + "FROM "
            + "( SELECT "
            + "       p.id              AS p_id "
            + "     , p.name            AS p_name "
            + "     , pmh.target_id     AS pmh_target_id "
            + "     , pmh.hit_count     AS pmh_hit_count "
            + "     , MAX(pmh.hit_count) OVER (PARTITION BY pmh.target_id) AS pmh_max_hits "
            + "  FROM "
            + "       " + TABLE_NAME + " pmh "
            + "  JOIN " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + "    ON pm.id = pmh.player_match_id "
            + "   AND pm.match_id = :match_id "
            + "  JOIN urt_player p "
            + "    ON p.id = pm.player_id "
            + ") max_pmh "
            + "WHERE max_pmh.pmh_hit_count = max_pmh.pmh_max_hits "
            + "ORDER BY max_pmh.pmh_target_id,  max_pmh.p_id "
         ;
    
    /**
     * UrTPlayerMatchHitRowMapper instance
     */
    protected static UrTPlayerMatchHitRowMapper playerMatchHitRowMapper = new UrTPlayerMatchHitRowMapper();
    
    /**
     * Best match hits data extractor
     */
    protected static UrTMatchBestHitsExtractor matchBestHitsExtractor = new UrTMatchBestHitsExtractor();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    public List<UrTPlayerMatchHitStat> getPlayerMatchHits(long playerMatchId) throws JdbcException {
        List<UrTPlayerMatchHitStat> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT + TABLE_WHERE_PLAYER_MATCH_ID + TABLE_ORDER_BY_TARGET_ID, params, playerMatchHitRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerMatchHitStat>();
        } catch(Exception e){
            throw new JdbcException("Failed to get hit list by player match ID " + playerMatchId, e);
        }
        return result;
    }
    
    public List<UrTPlayerMatchHitStat> getAllPlayerHits(long playerId) throws JdbcException {
        List<UrTPlayerMatchHitStat> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT_BY_PLAYER_ID, params, playerMatchHitRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerMatchHitStat>();
        } catch(Exception e){
            throw new JdbcException("Failed to get hit list by player ID " + playerId, e);
        }
        return result;
    }

    public List<UrTMatchHitStat> getMatchBestHits(long matchId) throws JdbcException {
        List<UrTMatchHitStat> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("match_id", QueryUtil.convertToQueryParam(Long.valueOf(matchId)));
        try{
            result = getJdbcTemplate().query(GET_BEST_MATCH_HITS_QUERY, params, matchBestHitsExtractor);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatchHitStat>();
        } catch (Exception e){
            throw new JdbcException("Failed to get best hits for match with id: " + matchId, e);
        }
        return result;
    }

    
    public void savePlayerMatchHits(List<UrTPlayerMatchHitStat> playerMatchHits, Long playerMatchId) throws JdbcException{
        if(playerMatchHits == null || playerMatchHits.isEmpty()){
            throw new IllegalArgumentException("Null or empty list");
        }
        if(playerMatchId == null) {
            throw new IllegalArgumentException("Player match ID is null.");
        }
        
        MapSqlParameterSource[] batchParamArray = new MapSqlParameterSource[playerMatchHits.size()];
        for(int i=0; i< playerMatchHits.size(); i++){
            UrTPlayerMatchHitStat hitStat = playerMatchHits.get(i);
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
            paramMap.addValue("target_id", QueryUtil.convertToQueryParam(hitStat.getTarget()));
            paramMap.addValue("hit_count", QueryUtil.convertToQueryParam(Integer.valueOf(hitStat.getHitCount())));
            batchParamArray[i] = paramMap;
        }
        
        try{
            getJdbcTemplate().batchUpdate(TABLE_INSERT_QUERY, batchParamArray);
        } catch (Exception e){
            throw new JdbcException("Failed to save player match hit list.", e);
        }
        
        
    }
    
    public void deleteAllMatchHits(long playerId) throws JdbcException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(playerId));
        try{
            int rc = getJdbcTemplate().update(TABLE_DELETE_QUERY, params);
            logger.debug("Deleted record count: " + rc);
        } catch (Exception e){
            throw new JdbcException("Failed to delete all matches with playerId: " + playerId, e);
        }
    }

    
    /**
     * UrT player match hits row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerMatchHitRowMapper implements RowMapper<UrTPlayerMatchHitStat> {
        
        public UrTPlayerMatchHitStat mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTPlayerMatchHitStat result = null;
            UrTHitTarget target = null;
            if(rs.getObject("pmh_target_id") != null){
                target = UrTHitTarget.values()[rs.getInt("pmh_target_id")];
            }
            
            result = new UrTPlayerMatchHitStat(
                    rs.getLong("pmh_player_match_id")
                  , target
                  , rs.getInt("pmh_hit_count"));
            return result;
        }
    }

    /**
     * Best match hits data extractor
     * 
     * @author ghost
     *
     */
    protected static class UrTMatchBestHitsExtractor implements ResultSetExtractor<List<UrTMatchHitStat>> {
        
        /**
         * Logger
         */
        protected Logger logger = Logger.getLogger(getClass());

        public List<UrTMatchHitStat> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<UrTMatchHitStat> result = new ArrayList<UrTMatchHitStat>();
            UrTMatchHitStat hitStat = null;
            UrTHitTarget target = null;
            while (rs.next()) {
                UrTHitTarget currentTarget = null;
                if(rs.getObject("target_id") != null){
                    currentTarget = UrTHitTarget.values()[rs.getInt("target_id")];
                } else {
                    logger.error("Target ID is null");
                    continue;
                }
                if(target == null || !currentTarget.equals(target)){
                    int hitCount = rs.getInt("hit_count");
                    hitStat = new UrTMatchHitStat(currentTarget, hitCount);
                    target = currentTarget;
                    result.add(hitStat);
                }
                // get player
                UrTPlayer player = new UrTPlayer(rs.getLong("player_id"), rs.getString("player_name"));
                hitStat.getPlayers().add(player);
            }
            return result;
        }
    }
    
}
