package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTPlayerMatchDao;
import urt.stats.core.enums.UrTTeam;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.chart.UrTPlayerMatchChartDataItem;

/**
 * UrT player match DAO implementation
 * 
 * @author ghost
 *
 */
@Repository
public class UrTPlayerMatchJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTPlayerMatchDao {
    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_player_match";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "pm";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_player_match";
    

    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id         AS pm_id "
          + "," + TABLE_ALIAS + ".player_id  AS pm_player_id "
          + "," + TABLE_ALIAS + ".match_id   AS pm_match_id "
          + "," + TABLE_ALIAS + ".team_id    AS pm_team_id "
          + "," + TABLE_ALIAS + ".kills      AS pm_kills "
          + "," + TABLE_ALIAS + ".deaths     AS pm_deaths "
          + "," + TABLE_ALIAS + ".kd_ratio   AS pm_kd_ratio "
          + "," + TABLE_ALIAS + ".has_hits   AS pm_has_hits "
          + "," + TABLE_ALIAS + ".has_achvs  AS pm_has_achvs ";
    
    /**
     * Player Match select query
     */
    protected static final String TABLE_SELECT = ""
            + "SELECT "
            + TABLE_COLUMNS
            + ", p.name AS p_name "
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + "JOIN " + UrTPlayerJdbcDaoImpl.TABLE_NAME + " " + UrTPlayerJdbcDaoImpl.TABLE_ALIAS
            + " ON " + UrTPlayerJdbcDaoImpl.TABLE_ALIAS + ".id = " + TABLE_ALIAS + ".player_id "
            ;
    
    /**
     * Player Match detailed stats select query
     */
    protected static final String SELECT_DETAILED_STATS = ""
            + "SELECT "
            + TABLE_COLUMNS
            + ", p.name AS p_name "
            + ", " + UrTMatchJdbcDaoImpl.TABLE_COLUMNS
            + ", " + UrTMapJdbcDaoImpl.TABLE_COLUMNS
            + " FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + "JOIN " + UrTPlayerJdbcDaoImpl.TABLE_NAME + " " + UrTPlayerJdbcDaoImpl.TABLE_ALIAS
            + " ON " + UrTPlayerJdbcDaoImpl.TABLE_ALIAS + ".id = " + TABLE_ALIAS + ".player_id "
            + "JOIN " + UrTMatchJdbcDaoImpl.TABLE_NAME + " " + UrTMatchJdbcDaoImpl.TABLE_ALIAS
            + " ON " + UrTMatchJdbcDaoImpl.TABLE_ALIAS + " .id = " + TABLE_ALIAS + ".match_id "
            + " JOIN " + UrTMapJdbcDaoImpl.TABLE_NAME + " " + UrTMapJdbcDaoImpl.TABLE_ALIAS
            + "    ON " + UrTMapJdbcDaoImpl.TABLE_ALIAS + ".id = " + UrTMatchJdbcDaoImpl.TABLE_ALIAS + ".map_id "
            + " WHERE " + TABLE_ALIAS + ".player_id = :player_id "
            + "  AND " + TABLE_ALIAS + ".team_id < 3 "
            + " ORDER BY " + UrTMatchJdbcDaoImpl.TABLE_ALIAS + ".start_time "
            ;
    
    /**
     * Player Match select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Player Match select query: match ID condition
     */
    protected static final String TABLE_WHERE_MATCH_ID = ""
            + " WHERE " + TABLE_ALIAS + ".match_id = :match_id ";

    /**
     * Order by clause: kills-to-deaths-ratio
     */
    protected static final String TABLE_ORDER_KD_RATIO = ""
            + " ORDER BY " + TABLE_ALIAS + ".kd_ratio DESC, " + TABLE_ALIAS + ".kills DESC, " + TABLE_ALIAS + ".deaths ";
    
    /**
     * Order by clause: kills-DESC than-deaths-ASC 
     */
    protected static final String TABLE_ORDER_KILLS_DEATHS = ""
            + " ORDER BY " + TABLE_ALIAS + ".kills DESC, " + TABLE_ALIAS + ".deaths ";
    
    /**
     * Player Match insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  id"
            + ", player_id"
            + ", match_id"
            + ", team_id"
            + ", kills"
            + ", deaths"
            + ", kd_ratio "
            + ", has_hits "
            + ", has_achvs "
            + ")"
            + " VALUES ("
            + "  :id"
            + ", :player_id"
            + ", :match_id"
            + ", :team_id"
            + ", :kills"
            + ", :deaths"
            + ", :kd_ratio "
            + ", :has_hits "
            + ", :has_achvs "
            + ")"
          ;
    
    /**
     * Delete all matches for specified player
     */
    protected static final String TABLE_DELETE_QUERY = ""
            + "DELETE FROM " + TABLE_NAME
            + " WHERE player_id = :player_id"
            ;
    
    /**
     * UrTPlayerMatchRowMapper instance
     */
    protected static UrTPlayerMatchRowMapper playerMatchRowMapper = new UrTPlayerMatchRowMapper();
    
    /**
     * UrTPlayerMatchChartDataRowMapper instance
     */
    protected static UrTPlayerMatchChartDataItemRowMapper playerMatchChartDataRowMapper = new UrTPlayerMatchChartDataItemRowMapper();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    public UrTPlayerMatchStats getPlayerMatchStatsById(long id) throws JdbcException, NoSuchObjectException {
        UrTPlayerMatchStats playerMatchStats = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(id)));
        try{
            playerMatchStats = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, playerMatchRowMapper);
        } catch(EmptyResultDataAccessException e) {
            throw new NoSuchObjectException("Failed to get player match details by ID: " + id, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + id, e);
        }
        return playerMatchStats;
    }
    
    public List<UrTPlayerMatchChartDataItem> getPlayerMatchStatsByPlayerId(long playerId) throws JdbcException {
        List<UrTPlayerMatchChartDataItem> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));
        try{
            result = getJdbcTemplate().query(SELECT_DETAILED_STATS, params, playerMatchChartDataRowMapper);
        } catch(EmptyResultDataAccessException e) {
            result = new ArrayList<UrTPlayerMatchChartDataItem>();
        } catch(Exception e){
            throw new JdbcException("Failed to get record with player ID " + playerId, e);
        }
        return result;
    } 

    
    public List<UrTPlayerMatchStats> getPlayersByMatchId(long matchId, boolean isTeamBasedMatch) throws JdbcException{
        List<UrTPlayerMatchStats> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("match_id", QueryUtil.convertToQueryParam(Long.valueOf(matchId)));
        
        try{
            StringBuilder query = new StringBuilder();
            query.append(TABLE_SELECT);
            query.append(TABLE_WHERE_MATCH_ID);
            if(isTeamBasedMatch) {
                query.append(TABLE_ORDER_KD_RATIO);
            } else {
                query.append(TABLE_ORDER_KILLS_DEATHS);
            }
            result = getJdbcTemplate().query(
                    query.toString()
                  , params
                  , playerMatchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerMatchStats>();
        } catch(Exception e){
            throw new JdbcException("Failed to get player match list by match ID " + matchId, e);
        }
        
        return result;

    }
    
    public long createPlayerMatchStats(UrTPlayerMatchStats entity) throws JdbcException {
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        if(entity.getMatchId() == null){
            throw new IllegalArgumentException("Match ID is null");
        }
        if(entity.getPlayerId() == null){
            throw new IllegalArgumentException("Player ID is null");
        }
        
        // get new ID from sequence
        long playerMatchId = getNewLongId(SEQUENCE_NAME);
        entity.setId(Long.valueOf(playerMatchId));
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("player_id", QueryUtil.convertToQueryParam(entity.getPlayerId()));
        params.addValue("match_id", QueryUtil.convertToQueryParam(entity.getMatchId()));
        params.addValue("team_id", QueryUtil.convertToQueryParam(entity.getPlayerTeam()));
        params.addValue("kills", QueryUtil.convertToQueryParam(entity.getKills()));
        params.addValue("deaths", QueryUtil.convertToQueryParam(entity.getDeaths()));
        params.addValue("kd_ratio", QueryUtil.convertToQueryParam(Double.valueOf(entity.getKDRatio())));
        params.addValue("has_hits", QueryUtil.convertToQueryParam(Boolean.valueOf(!entity.getPlayerMatchHits().isEmpty())));
        params.addValue("has_achvs", QueryUtil.convertToQueryParam(Boolean.valueOf(!entity.getPlayerMatchAchvs().isEmpty())));
        
        try{
            getJdbcTemplate().update(TABLE_INSERT_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create record: " + entity.toString(), e);
        }
        
        return playerMatchId;
    }
    
    public void deleteAllPlayerMatches(long playerId) throws JdbcException{
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
     * UrT Player Match row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerMatchRowMapper implements RowMapper<UrTPlayerMatchStats> {
        
        public UrTPlayerMatchStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTPlayerMatchStats result = null;
            UrTTeam playerTeam = null;
            if(rs.getObject("pm_team_id") != null){
                playerTeam = UrTTeam.values()[rs.getInt("pm_team_id")];
            }
            result = new UrTPlayerMatchStats(
                    rs.getLong("pm_id")
                  , rs.getLong("pm_player_id")
                  , rs.getString("p_name")
                  , rs.getLong("pm_match_id")
                  , playerTeam
                  , rs.getInt("pm_kills")
                  , rs.getInt("pm_deaths")
                  , rs.getBoolean("pm_has_hits")
                  , rs.getBoolean("pm_has_achvs"));
            return result;
        }
    }
    
    /**
     * UrT Player Match chart data row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerMatchChartDataItemRowMapper implements RowMapper<UrTPlayerMatchChartDataItem> {
        
        public UrTPlayerMatchChartDataItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTPlayerMatchChartDataItem result = new UrTPlayerMatchChartDataItem();
            UrTPlayerMatchStats playerMatchStats = playerMatchRowMapper.mapRow(rs, rowNum);
            UrTMatch match = UrTMatchJdbcDaoImpl.matchRowMapper.mapRow(rs, rowNum);
            result.setPlayerMatchStats(playerMatchStats);
            result.setMatch(match);
            return result;
        }
    }

}
