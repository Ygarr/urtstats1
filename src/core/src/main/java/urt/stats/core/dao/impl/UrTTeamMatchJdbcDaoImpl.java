package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTTeamMatchDao;
import urt.stats.core.enums.UrTTeam;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTTeamMatchStats;

/**
 * UrT Team match DAO implementation
 * @author ghost
 *
 */
@Repository
public class UrTTeamMatchJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTTeamMatchDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_team_match";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "tm";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_team_match";
    

    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id               AS tm_id "
          + "," + TABLE_ALIAS + ".match_id         AS tm_match_id "
          + "," + TABLE_ALIAS + ".team_id          AS tm_team_id "
          + "," + TABLE_ALIAS + ".team_kills       AS tm_team_kills "
          + "," + TABLE_ALIAS + ".team_deaths      AS tm_team_deaths "
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
     * Table select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Table select query: Match ID condition
     */
    protected static final String TABLE_WHERE_MATCH_ID = ""
            + " WHERE " + TABLE_ALIAS + ".match_id = :match_id ";
    
    /**
     * Order by clause: kills desc, deaths desc
     */
    protected static final String TABLE_ORDER_KD = ""
            + " ORDER BY " + TABLE_ALIAS + ".team_kills DESC, " + TABLE_ALIAS + ".team_deaths DESC ";

    
    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "  id "
            + ", match_id "
            + ", team_id "
            + ", team_kills "
            + ", team_deaths "
            + ")"
            + " VALUES ("
            + "  :id "
            + ", :match_id "
            + ", :team_id "
            + ", :team_kills "
            + ", :team_deaths "
            + ")"
          ;
    
    /**
     * UrTTeamMatchRowMapper instance
     */
    protected static UrTTeamMatchRowMapper teamMatchRowMapper = new UrTTeamMatchRowMapper();

    public UrTTeamMatchStats getTeamMatchStatsById(long id) throws JdbcException {
        UrTTeamMatchStats result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(id)));
        try{
            result = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, teamMatchRowMapper);
        }catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + id, e);
        }
        return result;
    }

    public List<UrTTeamMatchStats> getTeamMatchStatsByMatchId(long matchId) throws JdbcException{
        List<UrTTeamMatchStats> result = null;
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("match_id", QueryUtil.convertToQueryParam(Long.valueOf(matchId)));
        
        try{
            result = getJdbcTemplate().query(
                    TABLE_SELECT + TABLE_WHERE_MATCH_ID + TABLE_ORDER_KD
                  , params
                  , teamMatchRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTTeamMatchStats>();
        } catch(Exception e){
            throw new JdbcException("Failed to get team match list by match ID " + matchId, e);
        }

        return result;
    }
    
    public long createTeamMatchStats(UrTTeamMatchStats entity) throws JdbcException{
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        if(entity.getMatchId() == null){
            throw new IllegalArgumentException("Match ID is null");
        }
        
        // get new ID from sequence
        long entityId = getNewLongId(SEQUENCE_NAME);
        entity.setId(Long.valueOf(entityId));
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("match_id", QueryUtil.convertToQueryParam(entity.getMatchId()));
        params.addValue("team_id", QueryUtil.convertToQueryParam(entity.getTeamType()));
        params.addValue("team_kills", QueryUtil.convertToQueryParam(entity.getKills()));
        params.addValue("team_deaths", QueryUtil.convertToQueryParam(entity.getDeaths()));

        try{
            getJdbcTemplate().update(TABLE_INSERT_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create record: " + entity.toString(), e);
        }
        
        return entityId;
    }
    
    /**
     * UrT team match row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTTeamMatchRowMapper implements RowMapper<UrTTeamMatchStats> {
        
        public UrTTeamMatchStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTTeamMatchStats result = null;
            UrTTeam teamType = null;
            if(rs.getObject("tm_team_id") != null){
                teamType = UrTTeam.values()[rs.getInt("tm_team_id")];
            }
            
            result = new UrTTeamMatchStats(
                    rs.getLong("tm_id")
                  , rs.getLong("tm_match_id")
                  , teamType
                  , rs.getInt("tm_team_kills")
                  , rs.getInt("tm_team_deaths"));
            return result;
        }
    }

}
