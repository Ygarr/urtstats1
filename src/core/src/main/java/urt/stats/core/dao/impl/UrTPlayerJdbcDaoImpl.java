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

import urt.stats.core.dao.UrTPlayerDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.util.StringUtil;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT player DAO implementation
 * 
 * @author ghost
 *
 */
@Repository
public class UrTPlayerJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTPlayerDao{

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_player";
    
    /**
     * Player aliases Table name
     */
    public static final String PLAYER_ALIASES_TABLE_NAME = "urt_player_aliases";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "p";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_player";
    

    /**
     * Sort columns
     */
    protected static final String[] SORT_COLUMNS = new String[]{
          "id"
        , "name"
        , "avg_kd_ratio"
        , "kd_ratio"
        , "kills"
        , "deaths"
        , "games_played"
        , "last_played"
    };

    /**
     * Default sort column number (first=0)
     */
    public static int DEFAULT_SORT_COLUMN = 2;
    
    /**
     * Default sort order
     */
    public static boolean DEFAULT_SORT_ASC = false;

    
    
    /**
     * Table columns
     */
    protected static final String URT_TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id            AS p_id "
          + "," + TABLE_ALIAS + ".name          AS p_name "
          + "," + TABLE_ALIAS + ".kills         AS p_kills "
          + "," + TABLE_ALIAS + ".deaths        AS p_deaths "
          + "," + TABLE_ALIAS + ".kd_ratio      AS p_kd_ratio "
          + "," + TABLE_ALIAS + ".avg_kd_ratio  AS p_avg_kd_ratio "
          + "," + TABLE_ALIAS + ".games_played  AS p_games_played "
          + "," + TABLE_ALIAS + ".last_played   AS p_last_played "
        ;
    
    /**
     * Player select query
     */
    protected static final String TABLE_SELECT = ""
            + "SELECT "
            + URT_TABLE_COLUMNS
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " ";
    
    /**
     * Player ID select query
     */
    protected static final String TABLE_ID_SELECT = ""
            + "SELECT "
            + TABLE_ALIAS + ".id            AS p_id "
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " ";
    
    /**
     * Player select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Player select query: name condition
     */
    protected static final String TABLE_WHERE_NAME = ""
            + "WHERE " + TABLE_ALIAS + ".name = :name ";
    
    /**
     * Player select query: alias condition
     */
    protected static final String TABLE_ALIAS_CONDITION= ""
            + "JOIN " + PLAYER_ALIASES_TABLE_NAME + " al "
            + "ON al.player_id = p.id "
            + "AND al.alias = :name "
            ;
    
    /**
     * Order by KD ratio
     */
    protected static final String TABLE_ORDER_BY_KD = ""
            + " ORDER BY " + TABLE_ALIAS + ".avg_kd_ratio DESC "
            + ", " + TABLE_ALIAS + ".kills, " + TABLE_ALIAS + ".deaths ";
    
    /**
     * Player insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  id "
            + ", name "
            + ", kills "
            + ", deaths "
            + ", kd_ratio "
            + ", avg_kd_ratio "
            + ", games_played "
            + ", last_played"
            + ")"
            + " VALUES ("
            + "  :id "
            + ", :name "
            + ", :kills "
            + ", :deaths "
            + ", :kd_ratio "
            + ", :kd_ratio "
            + ", :games_played "
            + ", :last_played"
            + ")"
          ;
    
    /**
     * Update total player stats with player match results
     */
    protected static final String UPDATE_PLAYER_STATS_QUERY = ""
            + "UPDATE " + TABLE_NAME + " "
            + "SET "
            + "  name = :name"
            + ", kills = :kills"
            + ", deaths = :deaths"
            + ", kd_ratio = :kd_ratio"
            + ", games_played = :games_played "
            + ", last_played =  "
            + "   ( "
            + "     SELECT MAX(m.start_time) "
            + "     FROM " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + "       JOIN " + UrTMatchJdbcDaoImpl.TABLE_NAME + " m "
            + "         ON m.id = pm.match_id "
            + "     WHERE player_id = :id "
            + "   ) "
            + ", avg_kd_ratio = "
            + "   ( "
            + "     SELECT AVG(kd_ratio) "
            + "     FROM " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME
            + "     WHERE player_id = :id AND team_id < 3 "
            + "   ) "
            + "WHERE id = :id";

    /**
     * Update player name
     */
    protected static final String UPDATE_PLAYER_NAME_QUERY = ""
            + "UPDATE " + TABLE_NAME + " "
            + "SET "
            + "  name = :name "
            + "WHERE id = :id";
    
    /**
     * Delete player by ID
     */
    protected static final String TABLE_DELETE_QUERY = ""
            + "DELETE FROM " + TABLE_NAME
            + " WHERE id = :id ";
    
    /**
     * Create player alias query
     */
    protected static final String CREATE_PLAYER_ALIAS = ""
            + "INSERT INTO " + PLAYER_ALIASES_TABLE_NAME + " ("
            + "  alias"
            + ", player_id"
            + ") VALUES ( "
            + "  :alias"
            + ", :player_id"
            + ")";
    
    /**
     * Delete player alias
     */
    protected static final String DELETE_PLAYER_ALIASES = ""
            + "DELETE FROM " + PLAYER_ALIASES_TABLE_NAME
            + " WHERE player_id = :player_id ";

    /**
     * UrTPlayerRowMapper instance
     */
    protected static UrTPlayerRowMapper playerRowMapper = new UrTPlayerRowMapper();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    public UrTPlayerStats getPlayerStatsById(long id) throws NoSuchObjectException, JdbcException {
        UrTPlayerStats playerStats = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(id)));
        try{
            playerStats = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, playerRowMapper);
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no player with ID " + id, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + id, e);
        }
        return playerStats;
    }
    
    public UrTPlayerStats getPlayerStatsByName(String name) throws NoSuchObjectException, JdbcException {
        UrTPlayerStats playerStats = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", QueryUtil.convertToQueryParam(name));
        try{
            playerStats = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_ALIAS_CONDITION, params, playerRowMapper);
        }  catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no player with name " + name, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with name " + name, e);
        }
        return playerStats;
    } 

    public Long getPlayerIdByName(String name) throws JdbcException{
        Long result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", QueryUtil.convertToQueryParam(name));
        try{
            long id = getJdbcTemplate().queryForLong(TABLE_ID_SELECT + TABLE_WHERE_NAME, params);
            result = Long.valueOf(id);
        } catch(EmptyResultDataAccessException e){
            return null;
        } catch(Exception e){
            throw new JdbcException("Failed to get record ID by name " + name, e);
        }
        return result;
    }
    
    public List<UrTPlayerStats> getAllPlayerStats(UrTSortHelper sorthelper) throws JdbcException {
        List<UrTPlayerStats> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        try{
            String orderClause = getOrderByClause(sorthelper);
            result = getJdbcTemplate().query(TABLE_SELECT + orderClause, params, playerRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTPlayerStats>();
        } catch(Exception e){
            throw new JdbcException("Failed to get all records", e);
        }
        return result;
    }

    public long createPlayerStats(UrTPlayerStats entity) throws JdbcException{
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        
        // get new ID from sequence
        long playerId = getNewLongId(SEQUENCE_NAME);
        entity.setId(Long.valueOf(playerId));
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("name", QueryUtil.convertToQueryParam(entity.getPlayerName()));
        params.addValue("kills", QueryUtil.convertToQueryParam(entity.getKills()));
        params.addValue("deaths", QueryUtil.convertToQueryParam(entity.getDeaths()));
        params.addValue("kd_ratio", QueryUtil.convertToQueryParam(Double.valueOf(entity.calculateTotalKDRatio())));
        params.addValue("games_played", QueryUtil.convertToQueryParam(entity.getGamesPlayed()));
        params.addValue("last_played", QueryUtil.convertToQueryParam(entity.getLastGameTime()));
        
        try{
            getJdbcTemplate().update(TABLE_INSERT_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create record: " + entity.toString(), e);
        }
        
        return playerId;
    }
    
    /**
     * Creates player alias
     * 
     * @param playerId long - player ID
     * @param alias {@link String} player alias
     */
    public void createPlayerAlias(long playerId, String alias) throws JdbcException {
        if(!StringUtil.hasText(alias)){
            throw new IllegalArgumentException("Player alias required");
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("alias", QueryUtil.convertToQueryParam(alias));
        params.addValue("player_id", QueryUtil.convertToQueryParam(playerId));
        try{
            getJdbcTemplate().update(CREATE_PLAYER_ALIAS, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create player alias: " + alias + " for player ID " + playerId, e);
        }
    }
    
    public void updatePlayerStats(UrTPlayerStats playerStats) throws JdbcException{
        if(playerStats.getId() == null){
            throw new IllegalArgumentException("Player ID is null.");
        }
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(playerStats.getId()));
        params.addValue("name", QueryUtil.convertToQueryParam(playerStats.getPlayerName()));
        params.addValue("kills", QueryUtil.convertToQueryParam(playerStats.getKills()));
        params.addValue("deaths", QueryUtil.convertToQueryParam(playerStats.getDeaths()));
        params.addValue("kd_ratio", QueryUtil.convertToQueryParam(playerStats.calculateTotalKDRatio()));
        params.addValue("games_played", QueryUtil.convertToQueryParam(playerStats.getGamesPlayed()));
        params.addValue("last_played", QueryUtil.convertToQueryParam(playerStats.getLastGameTime()));
        try{
            getJdbcTemplate().update(UPDATE_PLAYER_STATS_QUERY, params);
        } catch(Exception e){
            throw new JdbcException("Failed to update player stats by ID: " + playerStats.getId(), e);
        }
    }
    
    public void updatePlayerName(long playerId, String playerName) throws JdbcException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(playerId));
        params.addValue("name", QueryUtil.convertToQueryParam(playerName));
        try{
            getJdbcTemplate().update(UPDATE_PLAYER_NAME_QUERY, params);
        } catch(Exception e){
            throw new JdbcException("Failed to update player name by ID: " + playerId, e);
        }
    }
    
    public void deletePlayer(long playerId) throws NoSuchObjectException, JdbcException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(playerId));
        int rc = 0;
        try{
            rc = getJdbcTemplate().update(TABLE_DELETE_QUERY, params);
            logger.debug("Deleted record count: " + rc);
        } catch (Exception e){
            throw new JdbcException("Failed to delete player with ID: " + playerId, e);
        }
        if(rc == 0){
            throw new NoSuchObjectException("There is no player with ID " + playerId, null);
        }
    }
    
    public void deletePlayerAliases(long playerId) throws NoSuchObjectException, JdbcException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(playerId));
        int rc = 0;
        try{
            rc = getJdbcTemplate().update(DELETE_PLAYER_ALIASES, params);
            logger.debug("Deleted record count: " + rc);
        } catch (Exception e){
            throw new JdbcException("Failed to delete player aliases with ID: " + playerId, e);
        }
        if(rc == 0){
            throw new NoSuchObjectException("There is no player aliases with ID " + playerId, null);
        }
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
        result.append(TABLE_ALIAS);
        result.append(".");
        result.append(sortColName);
        if(!sortHelper.isAsc()){
            result.append(" DESC");
        }
        if(!"name".equals(sortColName)){
            result.append(", ");
            result.append(TABLE_ALIAS);
            result.append(".");
            result.append("name");
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
    
    /**
     * UrT Player row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerRowMapper implements RowMapper<UrTPlayerStats> {
        
        public UrTPlayerStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTPlayerStats result = null;
            result = new UrTPlayerStats(
                    rs.getLong("p_id")
                  , rs.getString("p_name")
                  , rs.getInt("p_kills")
                  , rs.getInt("p_deaths")
                  , rs.getDouble("p_kd_ratio")
                  , rs.getDouble("p_avg_kd_ratio")
                  , rs.getInt("p_games_played")
                  , rs.getTimestamp("p_last_played"));
            return result;
        }
    }
}
