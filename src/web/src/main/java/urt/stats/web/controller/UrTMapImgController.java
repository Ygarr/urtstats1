package urt.stats.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTMapManager;
import urt.stats.core.vo.UrTMapImg;
import urt.stats.core.vo.UrTMapImgContent;
import urt.stats.web.controller.constants.UrTWebConstants;

/**
 * Map image servlet
 * 
 * @author ghost
 *
 */
@Controller
public class UrTMapImgController extends UrTController {

    /**
     * Default image extension
     */
    protected static final String DEFAULT_IMAGE_EXT = ".png";
    
    /**
     * Default image mime type
     */
    protected static final String DEFAULT_IMAGE_MIME_TYPE = "image/png";
    
    /**
     * Map image cache dir name
     */
    protected static final String MAP_IMG_CACHE_DIR_NAME = "map_img_cache";

    /**
     * Cache dir size (max files count per single dir)
     */
    protected static final long CACHE_DIR_SIZE = 50;
    
    /**
     * HTTP request header: If-Modified-Since
     */
    protected static final String HTTP_REQ_HEADER_IF_MODIFIED = "If-Modified-Since";

    /**
     * HTTP response header: Last-Modified
     */
    protected static final String HTTP_REQ_HEADER_LAST_MODIFIED = "Last-Modified";
    
    /**
     * HTTP response header: Cache-control
     */
    protected static final String HTTP_REQ_HEADER_CACHE_CONTROL = "Cache-Control";
    
    /**
     * HTTP response header: default value for 30 days in seconds
     */
    protected static final String HTTP_REQ_HEADER_CACHE_CONTROL_DEFAULT_VALUE = "max-age=2592000";
    
    /**
     * Servlet context
     */
    @Autowired
    private ServletContext servletContext;
    
    /**
     * Image cache content dir
     */
    protected File mapImgCacheDir = null;
    
    /**
     * Map manager
     */
    @Autowired
    private UrTMapManager mapManager;

    
    /**
     * Get map image content request
     * 
     * @param req {@link HttpServletRequest} request
     * @param resp {@link HttpServletResponse} response
     */
    @RequestMapping(value="/map_img")
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        long imgId = ServletRequestUtils.getLongParameter(req, UrTWebConstants.REQUEST_PARAM_ID, -1);
        if(getLogger().isDebugEnabled()){
            getLogger().debug("Get map image by id: " + imgId);
        }
        if(imgId == -1){
            logger.error("Missing image id parameter.");
            resp.setStatus(500);
            return;
        }
        UrTMapImg img = null;
        try {
            img = mapManager.getImage(imgId);
            Date imgLastModified = img.getLastModified();
            long ifModifiedMillis = req.getDateHeader(HTTP_REQ_HEADER_IF_MODIFIED);
            boolean loadImageFromDB = false;
            if(ifModifiedMillis != -1){
                // checking if-modified
                Date ifModified = new Date(ifModifiedMillis);
                getLogger().debug(HTTP_REQ_HEADER_IF_MODIFIED + ": " + ifModified);
                if(imgLastModified.after(ifModified)){
                    // image is outdated, reload needed
                    loadImageFromDB = true;
                } else {
                    // image has no changes, returning
                    // sending not-modified
                    resp.setStatus(304);
                    return;
                }
            } else {
                // unknown last-modified, searching in cache
                getLogger().debug("No " + HTTP_REQ_HEADER_IF_MODIFIED + " header, searching image in cache");
                File cachedImg = getCachedImg(imgId);
                if(cachedImg != null && cachedImg.exists()){
                    getLogger().debug("Found image in cache.");
                    // image found in cache
                    if(cachedImg.lastModified() <= img.getLastModified().getTime()){
                        // image in cache is outdated, reload needed
                        getLogger().debug("Cached image is outdated.");
                        loadImageFromDB = true;
                    } else {
                        // return image from cache
                        FileInputStream is = null;
                        try {
                            is = new FileInputStream(cachedImg);
                            sendImageWithResponse(resp, is, img.getLastModified().getTime(), -1);
                            return;
                        } catch (FileNotFoundException e) {
                            getLogger().warn("Cached image not found: '" + cachedImg.getAbsolutePath() + "', will be reloaded.");
                            loadImageFromDB = true;
                        } finally {
                            if(is != null){
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    getLogger().warn("Failed to close input stream", e);
                                }
                            }
                        }
                    }
                } else {
                    // no image in cache, load needed
                    loadImageFromDB = true;
                    getLogger().debug("No image in cache.");
                }
            }
            if(loadImageFromDB){
                getLogger().debug("Loading image from DB.");
                UrTMapImgContent imgContent = mapManager.getImageContent(imgId);
                putImgToCache(imgContent);
                // sending image with response
                ByteArrayInputStream is = new ByteArrayInputStream(imgContent.getContent());
                sendImageWithResponse(resp, is, imgContent.getLastModified().getTime(), imgContent.getContent().length);
                return;
            }
        } catch (NoSuchObjectException e) {
            logger.error("Image with id " + imgId + " not found.", e);
            resp.setStatus(404);
            return;
        } catch (JdbcException e) {
            logger.error("failed to get image with ID " + imgId, e);
            resp.setStatus(500);
            return;
        }
        
        getLogger().error("Something went so wrong!");
        resp.setStatus(500);
    }
    
    /**
     * Send image with response
     * 
     * @param resp {@link HttpServletResponse} response
     * @param is {@link InputStream} image data input stream
     * @param lastModified long - image last modified millis
     * @param contentLength int - image content length
     * 
     */
    protected void sendImageWithResponse(HttpServletResponse resp, InputStream is, long lastModified, int contentLength) {
        try {
            getLogger().debug("Sending image.");
            resp.setContentType(DEFAULT_IMAGE_MIME_TYPE);
            if(contentLength > 0){
                resp.setContentLength(contentLength);
            }
            FileCopyUtils.copy(is, resp.getOutputStream());
            resp.setStatus(200);
            resp.setDateHeader(HTTP_REQ_HEADER_LAST_MODIFIED, lastModified);
            resp.setHeader(HTTP_REQ_HEADER_CACHE_CONTROL, HTTP_REQ_HEADER_CACHE_CONTROL_DEFAULT_VALUE);
            getLogger().debug("Image sent.");
        } catch (IOException e) {
            getLogger().error("Failed to send image with response", e);
            resp.setStatus(500);
            return;
        }
    }
    
    /**
     * Returns cached image file by ID, or <code>null</code> if there is no image in cache
     * 
     * @param imgId long - image ID
     * 
     * @return {@link File} cached image file by ID, or <code>null</code> if there is no image in cache
     */
    protected File getCachedImg(long imgId) {
        File result = null;
        File dir = getCachedImageDir(imgId);
        result = new File(dir, getCachedImageFileName(imgId));
        return result;
    }
    
    /**
     * Returns cached image file by ID, or <code>null</code> if there is no image in cache
     * 
     * @param imgContent {@link UrTMapImgContent} image with content
     */
    protected void putImgToCache(UrTMapImgContent imgContent) {
        File dir = getCachedImageDir(imgContent.getId());
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir, getCachedImageFileName(imgContent.getId()));
        ByteArrayInputStream is = null;
        OutputStream os = null;
        is = new ByteArrayInputStream(imgContent.getContent());
        try {
            os = new FileOutputStream(file);
            FileCopyUtils.copy(is, os);
        } catch (FileNotFoundException e) {
            getLogger().error("Failed to put image to cache, file not found.", e);
        } catch (IOException e) {
            getLogger().error("Failed to put image to cache.", e);
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    getLogger().warn("failed to close input stream", e);
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    getLogger().warn("failed to close output stream", e);
                }
            }
        }
    }

    /**
     * Returns cache dir for image ID
     * 
     * @param id image id
     * 
     * @return cache dir for image by ID
     */
    protected File getCachedImageDir(long id){
        File dir = null;
        long base = id / CACHE_DIR_SIZE;
        long dirNum = base * CACHE_DIR_SIZE;
        dir = new File(mapImgCacheDir, String.valueOf(dirNum));
        return dir;
    }
    
    /**
     * Returns cached image file name by image id 
     * @param id long - image ID
     * @return {@link String} cached image file name by image id
     */
    protected String getCachedImageFileName(long id){
        StringBuilder result = new StringBuilder(String.valueOf(id));
        result.append(DEFAULT_IMAGE_EXT);
        return result.toString();
    }
    
    /**
     * Context Init method
     */
    @PostConstruct
    protected void init() {
        String imgTmpDirPath = System.getProperty("java.io.tmpdir");
        if(!imgTmpDirPath.endsWith(File.separator)){
            imgTmpDirPath+= File.separator;
        }
        imgTmpDirPath+= servletContext.getContextPath();
        imgTmpDirPath+= File.separator;
        
        imgTmpDirPath+= MAP_IMG_CACHE_DIR_NAME;
        mapImgCacheDir = new File(imgTmpDirPath);
    }
}
