package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTMatchDao;
import urt.stats.core.enums.UrTGameType;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT match DAO implementation
 * 
 * @author ghost
 *
 */
@Repository
public class UrTMatchJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTMatchDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_match";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "m";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_match";
    
    /**
     * Sort columns
     */
    protected static final String[] SORT_COLUMNS = new String[]{
          "id"
        , "start_time"
        , "game_type"
        , "name" // map name
    };

    /**
     * Default sort column number (first=0)
     */
    public static int DEFAULT_SORT_COLUMN = 1;
    
    /**
     * Default sort order
     */
    public static boolean DEFAULT_SORT_ASC = false;

    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id             AS m_id "
          + "," + TABLE_ALIAS + ".start_time     AS m_start_time "
          + "," + TABLE_ALIAS + ".end_time       AS m_end_time "
          + "," + TABLE_ALIAS + ".game_type      AS m_game_type "
          + "," + TABLE_ALIAS + ".map_id         AS m_map_id "
          + "," + TABLE_ALIAS + ".log_file_name  AS m_log_file_name "
        ;
    
    /**
     * Match select query
     */
    protected static final String TABLE_SELECT = ""
            + "SELECT "
            + TABLE_COLUMNS
            + ", "
            + UrTMapJdbcDaoImpl.TABLE_COLUMNS
            + " FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + " JOIN " + UrTMapJdbcDaoImpl.TABLE_NAME + " " + UrTMapJdbcDaoImpl.TABLE_ALIAS
            + "    ON " + UrTMapJdbcDaoImpl.TABLE_ALIAS + ".id = " + TABLE_ALIAS + ".map_id "
            ;
    
    /**
     * Match select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Match select query: ID condition
     */
    protected static final String TABLE_WHERE_PLAYER_ID = ""
            + " JOIN " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + " ON pm.match_id = m.id "
            + " AND pm.player_id = :player_id "
           ;
    
    /**
     * Match select query: current day condition
     */
    protected static final String TABLE_WHERE_CURR_DAY = ""
            + " WHERE " + TABLE_ALIAS + ".start_time >= CURRENT_DATE ";
    
    /**
     * Match select query: map condition
     */
    protected static final String TABLE_WHERE_MAP = ""
            + " WHERE " + TABLE_ALIAS + ".map_id = :map_id ";

    /**
     * Order by clause: start time descending
     */
    protected static final String TABLE_ORDER_BY_START_TIME = ""
            + " ORDER BY " + TABLE_ALIAS + ".start_time DESC ";
    
    /**
     * Match select query: paged condition
     */
    protected static final String TABLE_OFFSET_AND_LIMIT = ""
            + " LIMIT :limit OFFSET :offset ";

    /**
     * Match count query
     */
    protected static final String TABLE_COUNT_QUERY =  ""
            + "SELECT COUNT(" + TABLE_ALIAS + ".id) FROM " + TABLE_NAME + " " + TABLE_ALIAS ;
    
    /**
     * Match insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  id"
            + ", start_time"
            + ", end_time"
            + ", game_type"
            + ", map_id"
            + ", log_file_name"
            + ")"
            + " VALUES ("
            + "  :id"
            + ", :start_time"
            + ", :end_time"
            + ", :game_type"
            + ", :map_id"
            + ", :log_file_name"
            + ")"
          ;
    
    /**
     * UrTMatchRowMapper instance
     */
    protected static UrTMatchRowMapper matchRowMapper = new UrTMatchRowMapper();

    public UrTMatch getMatchById(long id) throws NoSuchObjectException, JdbcException {
        UrTMatch match = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(id)));
        try{
            match = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, matchRowMapper);
        } catch(EmptyResultDataAccessException e) {
            throw new NoSuchObjectException("Failed to get match details by ID: " + id, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + id, e);
        }
        return match;
    }
    
    public UrTMatchStats getMatchStatsById(long id) throws JdbcException, NoSuchObjectException {
        UrTMatchStats matchStats = null;
        UrTMatch match = getMatchById(id);
        matchStats = new UrTMatchStats(match);
        return matchStats;
    }
    
    public int getMatchCount() throws JdbcException {
        return queryForCount(TABLE_COUNT_QUERY, null);
    }
    
    public int getMatchCountByPlayerId(long playerId) throws JdbcException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));

        return queryForCount(TABLE_COUNT_QUERY + TABLE_WHERE_PLAYER_ID, params);
    }
    
    public List<UrTMatch> getMatchPagedList(int offset, int limit, UrTSortHelper sortHelper) throws JdbcException{
        List<UrTMatch> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", QueryUtil.convertToQueryParam(Integer.valueOf(offset)));
        params.addValue("limit", QueryUtil.convertToQueryParam(Integer.valueOf(limit)));

        try{
            result = getJdbcTemplate().query(
                    TABLE_SELECT + getOrderByClause(sortHelper) + TABLE_OFFSET_AND_LIMIT
                  , params
                  , matchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatch>();
        } catch(Exception e){
            throw new JdbcException("Failed to get paged match list offset: " + offset + ", limit: " + limit, e);
        }
        return result;
    }
    
    public List<UrTMatch> getMatchPagedListByPlayerId(long playerId, int offset, int limit, UrTSortHelper sortHelper) throws JdbcException {
        List<UrTMatch> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));
        params.addValue("offset", QueryUtil.convertToQueryParam(Integer.valueOf(offset)));
        params.addValue("limit", QueryUtil.convertToQueryParam(Integer.valueOf(limit)));

        try{
            result = getJdbcTemplate().query(
                    TABLE_SELECT
                    + TABLE_WHERE_PLAYER_ID
                    + getOrderByClause(sortHelper) 
                    + TABLE_OFFSET_AND_LIMIT
                  , params
                  , matchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatch>();
        } catch(Exception e){
            throw new JdbcException("Failed to get paged match list offset: " + offset + ", limit: " + limit, e);
        }
        return result;
    }
    
    public List<UrTMatch> getMatchListForCurrentDay(UrTSortHelper sortHelper) throws JdbcException {
        List<UrTMatch> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();

        try{
            result = getJdbcTemplate().query(
                    TABLE_SELECT + TABLE_WHERE_CURR_DAY + getOrderByClause(sortHelper)
                  , params
                  , matchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatch>();
        } catch(Exception e){
            throw new JdbcException("Failed to get paged match list for current day.", e);
        }
        return result;
    }

    public List<UrTMatch> getMatchListForMap(long mapId, UrTSortHelper sortHelper) throws JdbcException{
        List<UrTMatch> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();

        try{
            result = getJdbcTemplate().query(
                    TABLE_SELECT + TABLE_WHERE_MAP + getOrderByClause(sortHelper)
                  , params
                  , matchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMatch>();
        } catch(Exception e){
            throw new JdbcException("Failed to get paged match list for map with ID: " + mapId, e);
        }
        return result;
    }
    
    /**
     * Returns order by clause for List queries
     * 
     * @param sortHelper {@link UrTSortHelper} sort helper
     * @return {@link String} ordder by clause
     */
    protected String getOrderByClause(UrTSortHelper sortHelper){
        StringBuilder result = new StringBuilder();
        result.append(" ORDER BY ");
        String sortColName = getSortColumn(sortHelper.getColumn());
        if("name".equals(sortColName)){
            result.append(UrTMapJdbcDaoImpl.TABLE_ALIAS);
        } else {
            result.append(TABLE_ALIAS);
        }
        result.append(".");
        result.append(sortColName);
        if(!sortHelper.isAsc()){
            result.append(" DESC");
        }
        return result.toString();
    }
    
    protected String[] getSortColumns() {
        return SORT_COLUMNS;
    }

    protected int getDefaultSortColNum() {
        return DEFAULT_SORT_COLUMN;
    }
    
    protected boolean isDefaultSortAsc() {
        return DEFAULT_SORT_ASC;
    }
    
    public long createMatchStats(UrTMatchStats entity) throws JdbcException{
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        if(entity.getMap() == null){
            throw new IllegalArgumentException("Map required");
        }
        if(entity.getMap().getId() == null){
            throw new IllegalArgumentException("Map ID required");
        }
        // get new ID from sequence
        long matchId = getNewLongId(SEQUENCE_NAME);
        entity.setId(Long.valueOf(matchId));
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("start_time", QueryUtil.convertToQueryParam(entity.getStartTime()));
        params.addValue("end_time", QueryUtil.convertToQueryParam(entity.getEndTime()));
        params.addValue("game_type", QueryUtil.convertToQueryParam(entity.getGameType()));
        params.addValue("map_id", QueryUtil.convertToQueryParam(entity.getMap().getId()));
        params.addValue("log_file_name", QueryUtil.convertToQueryParam(entity.getLogFileName()));
        
        
        try{
            getJdbcTemplate().update(TABLE_INSERT_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create record: " + entity.toString(), e);
        }
        
        return matchId;
    }
    
    /**
     * UrT Match row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTMatchRowMapper implements RowMapper<UrTMatch> {
        
        public UrTMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTMatch result = null;
            UrTGameType gameType = null;
            if(rs.getObject("m_game_type") != null){
                gameType = UrTGameType.values()[rs.getInt("m_game_type")];
            }
            UrTMap map = UrTMapJdbcDaoImpl.mapRowMapper.mapRow(rs, rowNum);
            result = new UrTMatch(
                    rs.getLong("m_id")
                  , map
                  , rs.getString("m_log_file_name")
                  , gameType
                  , rs.getTimestamp("m_start_time")
                  , rs.getTimestamp("m_end_time"));
            return result;
        }
    }
    
}
