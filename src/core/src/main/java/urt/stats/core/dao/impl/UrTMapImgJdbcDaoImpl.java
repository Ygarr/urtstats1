package urt.stats.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import urt.stats.core.dao.UrTMapImgDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTMapImgContent;

/**
 * UrT Map image JDBC DAO impl
 * 
 * @author ghost
 *
 */
@Repository
public class UrTMapImgJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTMapImgDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_map_img";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "mi";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_map_img";
    

    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id            AS mi_id "
          + "," + TABLE_ALIAS + ".map_id        AS mi_map_id "
          + "," + TABLE_ALIAS + ".last_modified AS mi_last_modified "
        ;

    /**
     * Table columns with "content" column
     */
    protected static final String TABLE_COLUMNS_WITH_CONTENT = 
            TABLE_COLUMNS
          + "," + TABLE_ALIAS + ".content       AS mi_content "
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
     * Table select with content query
     */
    protected static final String TABLE_SELECT_WITH_CONTENT = ""
            + "SELECT "
            + TABLE_COLUMNS_WITH_CONTENT
            + "FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " ";
    
    /**
     * Table select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Table select query: map ID condition
     */
    protected static final String TABLE_WHERE_MAP_ID = ""
            + " WHERE " + TABLE_ALIAS + ".map_id = :map_id ";
    
    
    /**
     * Order by clause: name
     */
    protected static final String TABLE_ORDER_BY_ID = ""
            + " ORDER BY " + TABLE_ALIAS + ".id ";
    
    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "    map_id "
            + "  , last_modified "
            + "  , content "
            + ")"
            + " VALUES ("
            + "    :map_id "
            + "  , :last_modified "
            + "  , :content "
            + ")"
          ;
    
    /**
     * Map img row mapper
     */
    protected static UrTMapImgRowMapper mapImgRowMapper = new UrTMapImgRowMapper();
    
    /**
     * Map img with content row mapper
     */
    protected static UrTMapImgContentRowMapper mapImgContentRowMapper = new UrTMapImgContentRowMapper();
    

    public void saveImages(long mapId, List<UrTMapImgContent> entityList) throws JdbcException {
        if(entityList == null || entityList.isEmpty()){
            throw new IllegalArgumentException("Entity list required");
        }
        
        MapSqlParameterSource[] batchParamArray = new MapSqlParameterSource[entityList.size()];
        for(int i=0; i< entityList.size(); i++){
            UrTMapImgContent img = entityList.get(i);
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue("map_id", QueryUtil.convertToQueryParam(mapId));
            paramMap.addValue("last_modified", QueryUtil.convertToQueryParam(new Date()));
            paramMap.addValue("content", QueryUtil.convertToQueryParam(img.getContent()));
            batchParamArray[i] = paramMap;
        }
        try{
            getJdbcTemplate().batchUpdate(TABLE_INSERT_QUERY, batchParamArray);
        } catch (Exception e){
            throw new JdbcException("Failed to save map img list.", e);
        }
        
    }

    public List<UrTMapImg> getMapImages(long mapId) throws JdbcException {
        List<UrTMapImg> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        try{
            result = getJdbcTemplate().query(TABLE_SELECT + TABLE_ORDER_BY_ID, params, mapImgRowMapper);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMapImg>();
        } catch(Exception e){
            throw new JdbcException("Failed to get all records", e);
        }
        return result;
    }

    public UrTMapImg getImage(long imgId) throws NoSuchObjectException, JdbcException {
        UrTMapImg result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(imgId)));
        try{
            result = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, mapImgRowMapper);
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no map with ID " + imgId, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + imgId, e);
        }
        return result;
    }
    
    public UrTMapImgContent getImageContent(long imgId) throws NoSuchObjectException, JdbcException {
        UrTMapImgContent result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(imgId)));
        try{
            result = getJdbcTemplate().queryForObject(TABLE_SELECT_WITH_CONTENT + TABLE_WHERE_ID, params, mapImgContentRowMapper);
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no map with ID " + imgId, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + imgId, e);
        }
        return result;
    }

    /**
     * UrT map image row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTMapImgRowMapper implements RowMapper<UrTMapImg> {
        
        public UrTMapImg mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTMapImg result = null;
            if(rs.getObject("mi_id") != null){
                result = new UrTMapImg(
                        rs.getLong("mi_id")
                      , rs.getLong("mi_map_id")
                      , rs.getTimestamp("mi_last_modified"));
            }
            return result;
        }
    }
    
    /**
     * UrT map image with content row mapper
     * 
     * @author ghost
     *
     */
    protected static class UrTMapImgContentRowMapper implements RowMapper<UrTMapImgContent> {
        
        public UrTMapImgContent mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTMapImgContent result = null;
            byte[] content = rs.getBytes("mi_content");
            result = new UrTMapImgContent(
                    rs.getLong("mi_id")
                  , rs.getLong("mi_map_id")
                  , rs.getTimestamp("mi_last_modified")
                  , content);
            return result;
        }
    }

}
