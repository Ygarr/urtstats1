package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT map DAO interface
 * @author ghost
 *
 */
public interface UrTMapDao extends UrTDao {

    /**
     * Creates map record
     * 
     * @param entity {@link UrTMap} entity
     * 
     * @return new map ID
     * 
     * @throws JdbcException on failure
     */
    public long createMap(UrTMap entity) throws JdbcException;
    
    /**
     * Updates map
     * 
     * @param entity {@link UrTMap} map
     * 
     * @throws NoSuchObjectException if there is no such map
     * @throws JdbcException on failure
     */
    public void updateMap(UrTMap entity) throws NoSuchObjectException, JdbcException;
    
    /**
     * Increments match count for the specified map by 1
     * 
     * @param mapId long - map ID
     * 
     * @throws NoSuchObjectException if there is no such map
     * @throws JdbcException on failure
     */
    public void incrementMatchCount(long mapId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns map by ID
     * 
     * @param id map id
     * 
     * @return {@link UrTMap} map
     * 
     * @throws NoSuchObjectException if there is no such map
     * @throws JdbcException on failure
     */
    public UrTMap getMapById(long id) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns map with images by ID
     * 
     * @param id map id
     * 
     * @return {@link UrTMap} map
     * 
     * @throws NoSuchObjectException if there is no such map
     * @throws JdbcException on failure
     */
    public UrTMap getMapWithImagesById(long id) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns map with images by name
     * 
     * @param fileName {@link String} map file name 
     * 
     * @return {@link UrTMap} map
     * 
     * @throws NoSuchObjectException if there is no such map
     * @throws JdbcException on failure
     */
    public UrTMap getMapByFileName(String fileName) throws NoSuchObjectException, JdbcException;

    /**
     * Returns list of all maps with images
     * 
     * @param sortHelper {@link UrTSortHelper} sort helper
     * 
     * @return {@link List} of {@link UrTMap} objects
     * @throws JdbcException on failure
     */
    public List<UrTMap> getMapList(UrTSortHelper sortHelper) throws JdbcException;

}
