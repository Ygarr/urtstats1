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

import urt.stats.core.dao.UrTPlayerMatchAchievementDao;
import urt.stats.core.enums.UrTCountAchvType;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMatchCountAchv;
import urt.stats.core.vo.UrTPlayer;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;

/**
 * UrT player match achievement DAO implementation
 * @author ghost
 *
 */
@Repository
public class UrTPlayerMatchAchievementJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTPlayerMatchAchievementDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_pm_count_achv";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "pma";
    
    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " "  + TABLE_ALIAS + ".player_match_id         AS pma_player_match_id "
          + ", " + TABLE_ALIAS + ".achv_id                 AS pma_achv_id "
          + ", " + TABLE_ALIAS + ".achv_count              AS pma_achv_count "
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
     * Table order by achievement ID
     */
    protected static final String TABLE_ORDER_BY_ACHV_ID = ""
            + " ORDER BY " + TABLE_ALIAS + ".achv_id ";

    /**
     * Select player achvs stats for all the time by player ID
     */
    protected static final String TABLE_SELECT_BY_PLAYER_ID = ""
            + "SELECT "
            + "      pm.player_id                       AS pma_player_match_id "
            + ", " + TABLE_ALIAS + ".achv_id          AS pma_achv_id "
            + ",     SUM(" + TABLE_ALIAS + ".achv_count) AS pma_achv_count "
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + "JOIN " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + "  ON pm.id = " + TABLE_ALIAS + ".player_match_id "
            + " AND pm.player_id = :player_id "
            + " GROUP BY "
            + "       pm.player_id "
            + " , " + TABLE_ALIAS + ".achv_id "
            + TABLE_ORDER_BY_ACHV_ID
            ;
    
    /**
     * Delete all achievs for the specified player
     */
    protected static final String TABLE_DELETE_QUERY = ""
            + "DELETE FROM " + TABLE_NAME + " pma "
            + " WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + " WHERE pma.player_match_id = pm.id AND pm.player_id = :player_id) ";
    
    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  player_match_id "
            + ", achv_id "
            + ", achv_count "
            + ")"
            + " VALUES ("
            + "  :player_match_id "
            + ", :achv_id "
            + ", :achv_count "
            + ")"
          ;
    
    /**
     * Get best match achievements query
     */
    protected static final String GET_BEST_MATCH_ACHVS_QUERY = ""
            + "SELECT "
            + "       max_pma.p_id            AS player_id  "
            + "     , max_pma.p_name          AS player_name "
            + "     , max_pma.pma_achv_id     AS achv_id "
            + "     , max_pma.pma_achv_count  AS achv_count "
            + "FROM "
            + "( SELECT "
            + "       p.id              AS p_id "
            + "     , p.name            AS p_name "
            + "     , pma.achv_id       AS pma_achv_id "
            + "     , pma.achv_count    AS pma_achv_count "
            + "     , MAX(pma.achv_count) OVER (PARTITION BY pma.achv_id) AS pma_max_achv "
            + "  FROM "
            + "       " + TABLE_NAME + " pma "
            + "  JOIN " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + "    ON pm.id = pma.player_match_id "
            + "   AND pm.match_id = :match_id "
            + "  JOIN urt_player p "
            + "    ON p.id = pm.player_id "
            + ") max_pma "
            + "WHERE max_pma.pma_achv_count = max_pma.pma_max_achv "
            + "ORDER BY max_pma.pma_achv_id, max_pma.p_id "
         ;
    
    /**
     * UrTPlayerMatchAchvRowMapper instance
     */
    protected static UrTPlayerMatchAchvRowMapper playerMatchAchvRowMapper = new UrTPlayerMatchAchvRowMapper();
    
    /**
     * UrTMatchBestCountAchvsExtractor instance
     */
    protected static UrTMatchBestCountAchvsExtractor matchBestContAchvsExtractor = new UrTMatchBestCountAchvsExtractor();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());
    
    public List<UrTPlayerMatchCountAchv> getPlayerMatchAchvs(long playerMatchId) throws JdbcException {
        List<UrTPlayerMatchCountAchv> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT + TABLE_WHERE_PLAYER_MATCH_ID + TABLE_ORDER_BY_ACHV_ID, params, playerMatchAchvRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerMatchCountAchv>();
        } catch(Exception e){
            throw new JdbcException("Failed to get achievement list by player match ID " + playerMatchId, e);
        }
        return result;
    }
    
    public List<UrTPlayerMatchCountAchv> getAllPlayerAchvs(long playerId) throws JdbcException {
        List<UrTPlayerMatchCountAchv> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT_BY_PLAYER_ID, params, playerMatchAchvRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerMatchCountAchv>();
        } catch(Exception e){
            throw new JdbcException("Failed to get achievement list by player ID " + playerId, e);
        }
        return result;
    }
    
    public List<UrTMatchCountAchv> getMatchBestAchievs(long matchId) throws JdbcException {
        List<UrTMatchCountAchv> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("match_id", QueryUtil.convertToQueryParam(Long.valueOf(matchId)));
        try{
            result = getJdbcTemplate().query(GET_BEST_MATCH_ACHVS_QUERY, params, matchBestContAchvsExtractor);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatchCountAchv>();
        } catch (Exception e){
            throw new JdbcException("Failed to get best achvs for match with id: " + matchId, e);
        }
        
        return result;
    }
    
    public void savePlayerMatchAchvs(List<UrTPlayerMatchCountAchv> playerMatchAchvs, Long playerMatchId) throws JdbcException{
        if(playerMatchAchvs == null || playerMatchAchvs.isEmpty()){
            throw new IllegalArgumentException("Null or empty list");
        }
        if(playerMatchId == null) {
            throw new IllegalArgumentException("Player match ID is null.");
        }
        
        MapSqlParameterSource[] batchParamArray = new MapSqlParameterSource[playerMatchAchvs.size()];
        for(int i=0; i< playerMatchAchvs.size(); i++){
            UrTPlayerMatchCountAchv achv = playerMatchAchvs.get(i);
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
            paramMap.addValue("achv_id", QueryUtil.convertToQueryParam(achv.getAchievementType()));
            paramMap.addValue("achv_count", QueryUtil.convertToQueryParam(Integer.valueOf(achv.getAchvCount())));
            batchParamArray[i] = paramMap;
        }
        
        try{
            getJdbcTemplate().batchUpdate(TABLE_INSERT_QUERY, batchParamArray);
        } catch (Exception e){
            throw new JdbcException("Failed to save player match achievement list.", e);
        }
    }
    
    /**
     * UrT player match achievement row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerMatchAchvRowMapper implements RowMapper<UrTPlayerMatchCountAchv> {
        
        public UrTPlayerMatchCountAchv mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTPlayerMatchCountAchv result = null;
            UrTCountAchvType achvType = null;
            if(rs.getObject("pma_achv_id") != null){
                achvType = UrTCountAchvType.values()[rs.getInt("pma_achv_id")];
            }
            
            result = new UrTPlayerMatchCountAchv(
                    rs.getLong("pma_player_match_id")
                  , achvType
                  , rs.getInt("pma_achv_count"));
            return result;
        }
    }

    public void deleteAllMatchAchvs(long playerId) throws JdbcException {
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
     * Best match achvs data extractor
     * 
     * @author ghost
     *
     */
    protected static class UrTMatchBestCountAchvsExtractor implements ResultSetExtractor<List<UrTMatchCountAchv>> {

        /**
         * Logger
         */
        protected Logger logger = Logger.getLogger(getClass());

        public List<UrTMatchCountAchv> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<UrTMatchCountAchv> result = new ArrayList<UrTMatchCountAchv>();
            UrTMatchCountAchv achv = null;
            UrTCountAchvType achvType = null;
            while (rs.next()) {
                UrTCountAchvType currentAchvType = null;
                if(rs.getObject("achv_id") != null){
                    currentAchvType = UrTCountAchvType.values()[rs.getInt("achv_id")];
                } else {
                    logger.error("Achv ID is null");
                    continue;
                }
                if(achvType == null || !currentAchvType.equals(achvType)){
                    int achvCount = rs.getInt("achv_count");
                    achv = new UrTMatchCountAchv(currentAchvType, achvCount);
                    achvType = currentAchvType;
                    result.add(achv);
                }
                // get player
                UrTPlayer player = new UrTPlayer(rs.getLong("player_id"), rs.getString("player_name"));
                achv.getPlayers().add(player);
            }
            return result;
        }
    }

}
