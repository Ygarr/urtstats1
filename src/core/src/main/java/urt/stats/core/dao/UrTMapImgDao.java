package urt.stats.core.dao;

import java.util.List;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTMapImgContent;

/**
 * UrT map image DAO interface
 * 
 * @author ghost
 *
 */
public interface UrTMapImgDao extends UrTDao {

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

}
