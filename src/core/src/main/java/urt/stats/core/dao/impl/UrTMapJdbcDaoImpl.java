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

import urt.stats.core.dao.UrTMapDao;
import urt.stats.core.enums.UrTMapType;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT map DAO implementation
 * 
 * @author ghost
 *
 */
@Repository
public class UrTMapJdbcDaoImpl extends UrTJdbcDaoImpl implements UrTMapDao {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "urt_map";
    
    /**
     * Table alias
     */
    public static final String TABLE_ALIAS = "map";
    
    /**
     * Sequence name
     */
    public static final String SEQUENCE_NAME = "seq_urt_map";
    
    /**
     * Default sort column number (first=0)
     */
    public static int DEFAULT_SORT_COLUMN = 1;
    
    /**
     * Default sort order
     */
    public static boolean DEFAULT_SORT_ASC = true;
    
    /**
     * Sort columns
     */
    protected static final String[] SORT_COLUMNS = new String[]{
          "id"
        , "name"
        , "file_name"
        , "map_type_id"
        , "match_count"
        , "has_bots"
    };

    /**
     * Table columns
     */
    protected static final String TABLE_COLUMNS = ""
          + " " + TABLE_ALIAS + ".id          AS map_id "
          + "," + TABLE_ALIAS + ".name        AS map_name "
          + "," + TABLE_ALIAS + ".file_name   AS map_file_name "
          + "," + TABLE_ALIAS + ".map_type_id AS map_type_id "
          + "," + TABLE_ALIAS + ".match_count AS map_match_count "
          + "," + TABLE_ALIAS + ".has_bots    AS map_has_bots "
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
     * Table select with images query
     */
    protected static final String TABLE_SELECT_WITH_IMAGES = ""
            + "SELECT "
            + TABLE_COLUMNS
            + ", "
            + UrTMapImgJdbcDaoImpl.TABLE_COLUMNS
            + " FROM "
            + TABLE_NAME + " " + TABLE_ALIAS + " "
            + "LEFT JOIN "
            +     UrTMapImgJdbcDaoImpl.TABLE_NAME + " AS " + UrTMapImgJdbcDaoImpl.TABLE_ALIAS
            + "   ON " + UrTMapImgJdbcDaoImpl.TABLE_ALIAS + ".map_id = map.id ";
    
    /**
     * Table select query: ID condition
     */
    protected static final String TABLE_WHERE_ID = ""
            + " WHERE " + TABLE_ALIAS + ".id = :id ";
    
    /**
     * Table select query: name condition
     */
    protected static final String TABLE_WHERE_FILE_NAME = ""
            + " WHERE " + TABLE_ALIAS + ".file_name = :file_name ";
    
    /**
     * Order by clause: name
     */
    protected static final String TABLE_ORDER_BY_NAME = ""
            + " ORDER BY " + TABLE_ALIAS + ".name, " + TABLE_ALIAS + ".id ";

    /**
     * Table insert query
     */
    protected static final String TABLE_INSERT_QUERY = ""
            + "INSERT INTO " + TABLE_NAME + "("
            + "    id "
            + "  , name "
            + "  , file_name "
            + "  , map_type_id "
            + "  , has_bots "
            + "  , match_count "
            + ")"
            + " VALUES ("
            + "    :id "
            + "  , :name "
            + "  , :file_name "
            + "  , :map_type_id "
            + "  , :has_bots "
            + "  , :match_count "
            + ")"
          ;

    /**
     * Map update query
     */
    protected static final String TABLE_UPDATE_QUERY = ""
            + "UPDATE " + TABLE_NAME
            + " SET "
            + "    name = :name "
            + "  , file_name = :file_name "
            + "  , map_type_id = :map_type_id "
            + "  , has_bots = :has_bots "
            + " WHERE id = :id "
            ;

    /**
     * Increment match count for map
     */
    protected static final String INCREMENT_MATCH_COUNT = ""
            + " UPDATE " + TABLE_NAME
            + " SET match_count = match_count + 1 "
            + "WHERE id = :id";
    
    /**
     * Map row mapper
     */
    protected static UrTMapRowMapper mapRowMapper = new UrTMapRowMapper();
    
    /**
     * Map with images extractor
     */
    protected static UrTMapExtractor mapExtractor = new UrTMapExtractor();
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());
    
    public long createMap(UrTMap entity) throws JdbcException {
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        // get new ID from sequence
        long entityId = getNewLongId(SEQUENCE_NAME);
        entity.setId(Long.valueOf(entityId));
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("name", QueryUtil.convertToQueryParam(entity.getName()));
        params.addValue("file_name", QueryUtil.convertToQueryParam(entity.getFileName()));
        params.addValue("map_type_id", QueryUtil.convertToQueryParam(entity.getMapType()));
        params.addValue("has_bots", QueryUtil.convertToQueryParam(entity.hasBots()));
        params.addValue("match_count", QueryUtil.convertToQueryParam(entity.getMatchCount()));

        try{
            getJdbcTemplate().update(TABLE_INSERT_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to create record: " + entity.toString(), e);
        }
        
        return entityId;
    }
    
    public void updateMap(UrTMap entity) throws NoSuchObjectException, JdbcException {
        if(entity == null){
            throw new IllegalArgumentException("Entity required");
        }
        if(entity.getId() == null){
            throw new IllegalArgumentException("Entity ID required");
        }
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(entity.getId()));
        params.addValue("name", QueryUtil.convertToQueryParam(entity.getName()));
        params.addValue("file_name", QueryUtil.convertToQueryParam(entity.getFileName()));
        params.addValue("map_type_id", QueryUtil.convertToQueryParam(entity.getMapType()));
        params.addValue("has_bots", QueryUtil.convertToQueryParam(entity.hasBots()));
        
        int affectedRecords = 0;
        try{
            affectedRecords = getJdbcTemplate().update(TABLE_UPDATE_QUERY, params);
        } catch (Exception e){
            throw new JdbcException("Failed to update record: " + entity.toString(), e);
        }
        if(affectedRecords == 0){
            throw new NoSuchObjectException("Map with ID " + entity.getId() + " not found", null);
        }
    }

    public void incrementMatchCount(long mapId) throws NoSuchObjectException, JdbcException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(mapId));
        int affectedRecords = 0;
        try{
            affectedRecords = getJdbcTemplate().update(INCREMENT_MATCH_COUNT, params);
        } catch (Exception e){
            throw new JdbcException("Failed to update match count for map with ID: " + mapId, e);
        }
        if(affectedRecords == 0){
            throw new NoSuchObjectException("Map with ID " + mapId + " not found", null);
        }
    }
    
    public UrTMap getMapById(long id) throws NoSuchObjectException, JdbcException{
        UrTMap result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(id));
        try{
            result = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_ID, params, mapRowMapper);
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no map with id " + id, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with id " + id, e);
        }
        return result;
    }
    
    public UrTMap getMapWithImagesById(long id) throws NoSuchObjectException, JdbcException {
        UrTMap result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", QueryUtil.convertToQueryParam(Long.valueOf(id)));
        try{
            List<UrTMap> mapList = getJdbcTemplate().query(TABLE_SELECT_WITH_IMAGES + TABLE_WHERE_ID, params, mapExtractor);
            if(mapList.isEmpty() || mapList.size() != 1){
                throw new EmptyResultDataAccessException("There is no map with ID " + id, 1);
            } else {
                result = mapList.get(0);
            }
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no map with ID " + id, e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with ID " + id, e);
        }
        return result;
    }
    
    public UrTMap getMapByFileName(String fileName) throws NoSuchObjectException, JdbcException{
        UrTMap result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("file_name", QueryUtil.convertToQueryParam(fileName));
        try{
            result = getJdbcTemplate().queryForObject(TABLE_SELECT + TABLE_WHERE_FILE_NAME, params, mapRowMapper);
        } catch(EmptyResultDataAccessException e){
            throw new NoSuchObjectException("There is no map with file name '" + fileName + "'", e);
        } catch(Exception e){
            throw new JdbcException("Failed to get record with file name '" + fileName + "'", e);
        }
        return result;
    }

    public List<UrTMap> getMapList(UrTSortHelper sortHelper) throws JdbcException{
        List<UrTMap> result = null;
        MapSqlParameterSource params = new MapSqlParameterSource();
        try{
            StringBuilder query = new StringBuilder();
            query.append(TABLE_SELECT_WITH_IMAGES);
            query.append(" ORDER BY ");
            query.append(TABLE_ALIAS);
            query.append(".");
            query.append(getSortColumn(sortHelper.getColumn()));
            if(!sortHelper.isAsc()){
                query.append(" DESC");
            }
            
            if(!"name".equals(getSortColumn(sortHelper.getColumn()))) {
                query.append(",");
                query.append(TABLE_ALIAS);
                query.append(".name ");
            }
            
            query.append(",");
            query.append(TABLE_ALIAS);
            query.append(".id ");
            
            query.append(",");
            query.append(UrTMapImgJdbcDaoImpl.TABLE_ALIAS);
            query.append(".id ");

            result = getJdbcTemplate().query(query.toString(), params, mapExtractor);
        } catch (EmptyResultDataAccessException e){
            result = new ArrayList<UrTMap>();
        } catch(Exception e){
            throw new JdbcException("Failed to get all records", e);
        }
        return result;
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
     * UrT map row mapper
     * 
     * @author ghost
     */
    protected static class UrTMapRowMapper implements RowMapper<UrTMap> {
        
        public UrTMap mapRow(ResultSet rs, int rowNum) throws SQLException {
            UrTMap result = null;
            UrTMapType mapType = null;
            if(rs.getObject("map_type_id") != null){
                mapType = UrTMapType.values()[rs.getInt("map_type_id")];
            }
            Boolean hasBots = null;
            if(rs.getObject("map_has_bots") != null){
                hasBots = rs.getBoolean("map_has_bots");
            }
            
            result = new UrTMap(
                    rs.getLong("map_id")
                  , rs.getString("map_name")
                  , rs.getString("map_file_name")
                  , mapType
                  , hasBots
                  , rs.getInt("map_match_count"));
            return result;
        }
    }
    
    /**
     * UrT map with images resultset extractor
     * 
     * @author ghost
     */
    protected static class UrTMapExtractor implements ResultSetExtractor<List<UrTMap>> {

        public List<UrTMap> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<UrTMap> result = new ArrayList<UrTMap>();
            UrTMap currentMap = null;
            int rowNum = 0; 
            while (rs.next()) {
                rowNum++;
                long mapId = rs.getLong("map_id");
                if(currentMap == null || mapId != currentMap.getId()){
                    // map changed
                    currentMap = mapRowMapper.mapRow(rs, rowNum);
                    result.add(currentMap);
                }
                
                UrTMapImg img = UrTMapImgJdbcDaoImpl.mapImgRowMapper.mapRow(rs, rowNum);
                if(img != null){
                    currentMap.getImages().add(img);
                }
            }
            
            return result;
        }
        
    }
}
