package urt.stats.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.dao.UrTMapDao;
import urt.stats.core.dao.UrTMapImgDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTMapManager;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTMapImgContent;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT map manager implementation
 * @author ghost
 *
 */
@Service
@Transactional(readOnly = true)
public class UrTMapManagerImpl extends UrTAbstractManager implements UrTMapManager {

    /**
     * Map DAO
     */
    @Autowired
    private UrTMapDao mapDao;
    
    /**
     * Map image DAO
     */
    @Autowired
    private UrTMapImgDao mapImgDao;
    
    protected UrTDao getDao() {
        return this.mapDao;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public long createMap(UrTMap entity) throws JdbcException {
        long mapId = mapDao.createMap(entity);
        if(entity == null){
            throw new IllegalArgumentException("entity is NULL");
        }
        return mapId;
    }

    public UrTMap getMapById(long id) throws NoSuchObjectException, JdbcException {
        return mapDao.getMapById(id);
    }
    
    public UrTMap getMapWithImagesById(long id) throws NoSuchObjectException, JdbcException {
        return mapDao.getMapWithImagesById(id);
    }
    
    public UrTMap getMapByFileName(String fileName) throws NoSuchObjectException, JdbcException {
        return mapDao.getMapByFileName(fileName);
    }

    public List<UrTMap> getMapList(UrTSortHelper sortHelper) throws JdbcException{
        return mapDao.getMapList(sortHelper);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveImages(long mapId, List<UrTMapImgContent> entityList) throws JdbcException {
        mapImgDao.saveImages(mapId, entityList);
    }

    public List<UrTMapImg> getMapImages(long mapId) throws JdbcException {
        return mapImgDao.getMapImages(mapId);
    }

    public UrTMapImg getImage(long imgId) throws NoSuchObjectException, JdbcException {
        return mapImgDao.getImage(imgId);
    }
    
    public UrTMapImgContent getImageContent(long imgId) throws NoSuchObjectException, JdbcException {
        return mapImgDao.getImageContent(imgId);
    }

    public UrTSortHelper getSortHelper(){
        return mapDao.getSortHelper();
    }
    
    public UrTSortHelper getSortHelper(int column, boolean isAsc) {
        return mapDao.getSortHelper(column, isAsc);
    }
    
}
