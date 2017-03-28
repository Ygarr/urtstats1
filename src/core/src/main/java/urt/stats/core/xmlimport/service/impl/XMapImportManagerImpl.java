package urt.stats.core.xmlimport.service.impl;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import urt.stats.core.dao.UrTMapDao;
import urt.stats.core.dao.UrTMapImgDao;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.util.ImageUtil;
import urt.stats.core.util.StringUtil;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMapImgContent;
import urt.stats.core.xmlimport.JaxbHandler;
import urt.stats.core.xmlimport.exception.XImportException;
import urt.stats.core.xmlimport.service.XMapImportManager;
import urt.stats.core.xmlimport.vo.XMap;
import urt.stats.core.xmlimport.vo.XMapImg;
import urt.stats.core.xmlimport.vo.XMapList;

/**
 * Map info import from XML manager implementation
 * 
 * @author ghost
 *
 */
@Service("xmapImportManager")
public class XMapImportManagerImpl implements XMapImportManager {

    /**
     * Default img width
     */
    protected static final int DEFAULT_IMG_WIDTH = 640;

    /**
     * Default img height
     */
    protected static final int DEFAULT_IMG_HEIGHT = 480;
    
    
    /**
     * URL prefix "file:"
     */
    protected static final String URL_PREFIX_FILE = "file:";
    
    /**
     * Logger
     */
    private Logger logger = Logger.getLogger(getClass());

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
    
    /**
     * XSD schema
     */
    @Value("classpath:/xml/import_maps.xsd")
    private URL schemaURL;
    
    /**
     * Image temp directory
     */
    private File imgTmpDir = null; 
    
    /**
     * Jaxb util
     */
    private JaxbHandler<XMapList> jaxbHandler;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<UrTMap> processXMLFile(File file) throws FileNotFoundException, XImportException {
        List<UrTMap> mapList = new ArrayList<UrTMap>();
        logger.info("Processing XML file '" + file.getAbsolutePath() + "'");
        InputStream is = null;
        try{
            is = new FileInputStream(file);
            XMapList xmapData = jaxbHandler.unmarshallXML(is, XMapList.class);
            if(!jaxbHandler.getValidator().isValid()){
                jaxbHandler.getValidator().printErrors(logger);
                throw new XImportException("Import failed due to validation errors", null);
            }
            if(xmapData.getMaps() != null && !xmapData.getMaps().isEmpty()){
                int cnt = 0;
                for(XMap xmap : xmapData.getMaps()){
                    cnt++;
                    if(logger.isDebugEnabled()){
                        logger.debug("Processing map " + cnt + " / " + xmapData.getMaps().size() + "\n:" + xmap.toString());
                    }
                    UrTMap map = convertToUrTMap(xmap);
                    long mapId;
                    UrTMap existingMap = null;
                    try{
                        existingMap = mapDao.getMapByFileName(map.getFileName());
                        logger.debug("Map already exists, checking for changes...");
                        mapId = existingMap.getId();
                        if(map.differsFrom(existingMap)){
                            map.setId(mapId);
                            logger.debug("Map differs, updating.");
                            mapDao.updateMap(map);
                        }else{
                            logger.debug("No need to update.");
                        }
                    } catch(NoSuchObjectException e){
                        logger.debug("Map does not exist, creating...");
                        mapId = mapDao.createMap(map);
                    }
                    if(xmap.getImages() != null && !xmap.getImages().isEmpty()){
                        logger.debug("Downloading and converting images...");
                        List<UrTMapImgContent> images = convertToImages(xmap.getImages());
                        if(!images.isEmpty()){
                            logger.debug("Saving images...");
                            mapImgDao.saveImages(mapId, images);
                        }
                    } else {
                        logger.debug("No images.");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to process XML map info", e);
            throw new XImportException("Failed to process Map info", e);
        } finally {
            if(is !=null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.warn("Failed to close input stream", e);
                }
            }
        } 
        
        return mapList;
    }
    
    /**
     * Init method
     */
    @PostConstruct
    protected void init() {
        jaxbHandler = new JaxbHandler<XMapList>(schemaURL);
        String imgTmpDirPath = System.getProperty("java.io.tmpdir");
        imgTmpDir = new File(imgTmpDirPath);
    }
    
    
    /**
     * Converts {@link XMapList} object to list of {@link UrTMap} objects
     * 
     * @param importData {@link XMapList} object
     * 
     * @return {@link List} of {@link UrTMap} objects
     */
    protected List<UrTMap> convertToMapList(XMapList importData) {
        List<UrTMap> result = new ArrayList<UrTMap>();
        if(importData.getMaps() != null && !importData.getMaps().isEmpty()){
            for(XMap xmap : importData.getMaps()){
                UrTMap map = convertToUrTMap(xmap);
                result.add(map);
            }
        }
        return result;
    }
    
    /**
     * Converts {@link XMap} object to {@link UrTMap} object
     * 
     * @param map {@link XMap} object
     * 
     * @return {@link UrTMap} object
     */
    protected UrTMap convertToUrTMap(XMap map) {
        UrTMap result = new UrTMap(map.getFileName(), map.getMapType(), map.hasBots());
        
        return result;
    }
    
    /**
     * Converts {@link XMapImg} object to {@link UrTMapImgContent} object
     * 
     * @param xmapImages {@link List} of @{link XMapImg} objects
     * 
     * @return {@link List} of {@link UrTMapImgContent} objects
     */
    protected List<UrTMapImgContent> convertToImages(List<XMapImg> xmapImages) {
        List<UrTMapImgContent> result = new ArrayList<UrTMapImgContent>();
        for(XMapImg ximg : xmapImages){
            UrTMapImgContent img = new UrTMapImgContent();
            if(StringUtil.hasText(ximg.getContentURL())){
                try {
                    byte[] content = downloadContent(ximg.getContentURL());
                    if(content == null){
                        continue;
                    }
                    img.setContent(content);
                    result.add(img);
                } catch (Exception e) {
                    logger.error("Failed to get content from URL '" + ximg.getContentURL() + "'", e);
                }
            }
        }
        return result;
    }

    /**
     * Downloads file from given URL
     * 
     * @param source {@link String}
     * 
     * @return file content as bytes
     * 
     * @throws IOException on failure
     * @throws Exception on failure
     */
    protected byte[] downloadContent(String source) throws IOException, Exception {
        byte[] result = null;
        File imgFile = null;
        boolean isTmpFile = false;
        if(source.startsWith(URL_PREFIX_FILE)){
            URI uri = new URI(source);
            imgFile = new File(uri);
            if(!imgFile.exists()){
                throw new IOException("File not found: '" + source + "'");
            }
        } else {
            InputStream is = null;
            OutputStream os = null;
            try{
                URL url = new URL(source);
                is = url.openStream();
                imgFile = File.createTempFile("tmp_img_", "", imgTmpDir);
                os = new FileOutputStream(imgFile);
                IOUtils.copy(is, os);
                isTmpFile = true;
            } finally {
                if(is != null){
                    is.close();
                }
                if(os != null){
                    os.close();
                }
            }
        }
        InputStream is = null;
        ByteArrayOutputStream os = null;
        
        try{
            File resizedImgFile = ImageUtil.getResizedImage(imgFile, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, true, true, isTmpFile);
            is = new FileInputStream(resizedImgFile);
            os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            result = os.toByteArray();
        } finally{
            if(is != null){
                is.close();
            }
            if(os != null){
                os.close();
            }
        }
        return result;
    }
    
}
