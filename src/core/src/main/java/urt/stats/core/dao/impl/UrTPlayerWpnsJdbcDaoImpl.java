package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTPlayerWpnsDao;
import urt.stats.core.enums.UrTWeapon;
import urt.stats.core.enums.UrTWeaponType;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTPlayerWeaponStats;

/**
 * UrT player weapon stats DAO implementation
 * @author ghost
 *
 */
@Repository
public class UrTPlayerWpnsJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTPlayerWpnsDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_player_wpns";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "pw";
    
    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".player_match_id   AS pw_player_match_id "
          + ", " + TABLE_ALIAS + ".weapon_id        AS pw_weapon_id "
          + ", " + TABLE_ALIAS + ".kills            AS pw_kills "
        ;

    /**
     * Table select single record query
     */
    protected static final String TABLE_SELECT_SINGLE = ""
            + "SELECT "
            + TABLE_COLUMNS
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            ;
    
    /**
     * Table select query
     */
    protected static final String TABLE_SELECT = ""
            + "SELECT "
            + TABLE_COLUMNS
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + " JOIN urt_weapon w "
            + "   ON w.id = " + TABLE_ALIAS + ".weapon_id "
            ;

    /**
     * Table select query
     */
    protected static final String TABLE_SELECT_BY_PLAYER_ID = ""
            + "SELECT "
            +        TABLE_ALIAS + ".weapon_id        AS pw_weapon_id "
            + ", SUM(" + TABLE_ALIAS + ".kills)       AS pw_kills "
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + " JOIN urt_player_match pm "
            + "   ON pm.id = " + TABLE_ALIAS + ".player_match_id "
            + " JOIN urt_player p "
            + "   ON p.id = pm.player_id "
            + "  AND p.id = :player_id "
            + " JOIN urt_weapon w "
            + "   ON w.id = " + TABLE_ALIAS + ".weapon_id "
            + " GROUP BY w.type_id, " + TABLE_ALIAS + ".weapon_id "
            + " ORDER BY w.type_id, pw_kills DESC "
            ;

    /**
     * Table select query: player match ID condition
     */
    protected static final String TABLE_WHERE_PLAYER_MATCH_ID = ""
            + " WHERE " + TABLE_ALIAS + ".player_match_id = :player_match_id ";
    
    /**
     * Table order by target ID
     */
    protected static final String TABLE_ORDER_BY_KILLS = ""
            + " ORDER BY w.type_id "
            + " , " + TABLE_ALIAS + ".kills DESC ";
    
    /**
     * Delete all hits for the specified player
     */
    protected static final String TABLE_DELETE_QUERY = ""
            + "DELETE FROM " + TABLE_NAME + " pw "
            + " WHERE EXISTS ("
            + "SELECT 1 "
            + "FROM " + UrTPlayerMatchJdbcDaoImpl.TABLE_NAME + " pm "
            + " WHERE pw.player_match_id = pm.id AND pm.player_id = :player_id) ";
   
    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  player_match_id "
            + ", weapon_id "
            + ", kills "
            + ")"
            + " VALUES ("
            + "  :player_match_id "
            + ", :weapon_id "
            + ", :kills "
            + ")"
          ;
    
    /**
     * Player weapon stats row mapper
     */
    protected static UrTPlayerWpnStatsRowMapper playerWpnStatsRowMapper = new UrTPlayerWpnStatsRowMapper();
    
    /**
     * Player weapon stats data extractor
     */
    protected static UrTPlayerWpnStatsExtractor playerWpnStatsExtractor = new UrTPlayerWpnStatsExtractor();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    
    
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerWpnStats(long playerId) throws JdbcException {
        Map<String, List<UrTPlayerWeaponStats>> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(Long.valueOf(playerId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT_BY_PLAYER_ID, params, playerWpnStatsExtractor);
        } catch (EmptyResultDataAccessException e){
            result = new LinkedHashMap<String, List<UrTPlayerWeaponStats>>();
        } catch (Exception e){
            throw new JdbcException("Failed to get player weapon stats by player ID: " + playerId, e);
        }
        return result;

    }

    public Map<String, List<UrTPlayerWeaponStats>> getPlayerMatchWpnStats(long playerMatchId) throws JdbcException {
        Map<String, List<UrTPlayerWeaponStats>> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
        try{
            result = getJdbcTemplate().query(TABLE_SELECT + TABLE_WHERE_PLAYER_MATCH_ID + TABLE_ORDER_BY_KILLS, params, playerWpnStatsExtractor);
        } catch (EmptyResultDataAccessException e){
            result = new LinkedHashMap<String, List<UrTPlayerWeaponStats>>();
        } catch (Exception e){
            throw new JdbcException("Failed to get player weapon stats by player match ID: " + playerMatchId, e);
        }
        return result;

    }
    
    public void savePlayerWpnStats(Map<UrTWeapon, UrTPlayerWeaponStats> playerWpns, Long playerMatchId) throws JdbcException {

        int recordCount = playerWpns.keySet().size();
        MapSqlParameterSource[] batchParamArray = new MapSqlParameterSource[recordCount];
        int i = 0;
        for(UrTPlayerWeaponStats wpnStat : playerWpns.values()){
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("player_match_id", QueryUtil.convertToQueryParam(Long.valueOf(playerMatchId)));
            paramMap.addValue("weapon_id", QueryUtil.convertToQueryParam(wpnStat.getWeapon()));
            paramMap.addValue("kills", QueryUtil.convertToQueryParam(Integer.valueOf(wpnStat.getKills())));
            batchParamArray[i] = paramMap;
            
            i++;
        }
        
        try{
            getJdbcTemplate().batchUpdate(TABLE_INSERT_QUERY, batchParamArray);
        } catch (Exception e){
            throw new JdbcException("Failed to save player match hit list.", e);
        }
    }

    public void deleteAllWpnStats(long playerId) throws JdbcException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("player_id", QueryUtil.convertToQueryParam(playerId));
        try{
            int rc = getJdbcTemplate().update(TABLE_DELETE_QUERY, params);
            logger.debug("Deleted record count: " + rc);
        } catch (Exception e){
            throw new JdbcException("Failed to delete all player weapon stats with playerId: " + playerId, e);
        }
    }

    /**
     * Player weapon stats row mapper
     * @author ghost
     *
     */
    protected static class UrTPlayerWpnStatsRowMapper implements RowMapper<UrTPlayerWeaponStats> {

        public UrTPlayerWeaponStats mapRow(ResultSet rs, int arg1) throws SQLException {
            UrTPlayerWeaponStats result = null;
            UrTWeapon weapon = null;
            if(rs.getObject("pw_weapon_id") != null){
                weapon = UrTWeapon.values()[rs.getInt("pw_weapon_id")];
            }
            result = new UrTPlayerWeaponStats(
                    weapon 
                  , rs.getInt("pw_kills")
               );
            return result;
        }
        
    }
    
    /**
     * Player weapon stats data extractor
     * 
     * @author ghost
     *
     */
    protected static class UrTPlayerWpnStatsExtractor implements ResultSetExtractor<Map<String, List<UrTPlayerWeaponStats>>> {
        
        /**
         * Logger
         */
        protected Logger logger = Logger.getLogger(getClass());

        public Map<String, List<UrTPlayerWeaponStats>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, List<UrTPlayerWeaponStats>> result = new LinkedHashMap<String, List<UrTPlayerWeaponStats>>();
            int cnt = 0;
            while (rs.next()) {
                cnt++;
                UrTPlayerWeaponStats wpnStats = playerWpnStatsRowMapper.mapRow(rs, cnt);
                UrTWeaponType weaponType = wpnStats.getWeapon().getWeaponType();
                String key = weaponType.toString();
                if(result.containsKey(key)){
                    result.get(key).add(wpnStats);
                } else {
                    List<UrTPlayerWeaponStats> list = new ArrayList<UrTPlayerWeaponStats>();
                    list.add(wpnStats);
                    result.put(key, list);
                }
            }
            
            return result;
        }
    }

}
