package urt.stats.core.service;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTMapImgContent;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT map manager interface
 * @author ghost
 *
 */
public interface UrTMapManager extends UrTManager {

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
     * Returns map by name
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
    
    /**
     * Saves map images
     * 
     * @param mapId long - map ID
     * @param entityList {@link List} of {@link UrTMapImgContent} map image content objects
     * 
     * @throws JdbcException on failure
     */
    public void saveImages(long mapId, List<UrTMapImgContent> entityList) throws JdbcException;
    
    /**
     * Returns list of map images <b>without content</b>
     * 
     * @param mapId {@link Long} map ID
     * 
     * @return {@link List} of {@link UrTMapImg} objects <b>without content</b>
     * @throws JdbcException on failure
     */
    public List<UrTMapImg> getMapImages(long mapId) throws JdbcException;
    
    /**
     * Returns image meta data
     * 
     * @param imgId long image ID
     * 
     * @return {@link UrTMapImg} map image
     * 
     * @throws NoSuchObjectException if there is no such image
     * @throws JdbcException on failure
     */
    public UrTMapImg getImage(long imgId) throws NoSuchObjectException, JdbcException;

    /**
     * Returns map image content
     * @param imgId long image ID
     * 
     * @return {@link UrTMapImg} map image
     * 
     * @throws NoSuchObjectException if there is no such image
     * @throws JdbcException on failure
     */
    public UrTMapImgContent getImageContent(long imgId) throws NoSuchObjectException, JdbcException;
    
    /**
     * Returns default sort helper
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper();
    
    /**
     * Returns sort helper with the specified sort column
     * 
     * @param column int - sort column number
     * @param isAsc boolean sort order
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper(int column, boolean isAsc);
}
