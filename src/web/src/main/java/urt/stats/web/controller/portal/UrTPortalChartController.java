package urt.stats.web.controller.portal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.web.chart.exception.ChartException;
import urt.stats.web.chart.service.UrTChartService;
import urt.stats.web.chart.vo.UrTChartInfo;
import urt.stats.web.controller.UrTController;
import urt.stats.web.controller.constants.UrTWebConstants;
import urt.stats.web.exception.UrTErrorDetails;

/**
 * Portal chart controller
 * 
 * @author ghost
 *
 */
@Controller
public class UrTPortalChartController extends UrTController {

    /**
     * chart storage dir name
     */
    protected static final String CHART_STORAGE_DIR_NAME = "chart_storage";

    /**
     * Chart image mime type
     */
    protected static final String CHART_IMAGE_MIME_TYPE = "image/png";
    
    /**
     * Chart img file extension
     */
    protected static final String CHART_IMG_FILE_EXT = ".png";

    
    /**
     * Player manager
     */
    @Autowired
    private UrTPlayerManager playerManager;
    
    /**
     * Chart service
     */
    @Autowired
    private UrTChartService chartService;
    
    /**
     * Servlet context
     */
    @Autowired
    private ServletContext servletContext;
    
    /**
     * Chart storage dir
     */
    protected File chartStorageDir = null;
    
    /**
     * Player rate chart handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/chart/player_rate"})
    public String chartPlayerRate(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_CHART_PLAYER_RATE;
        getLogger().debug("ChartPlayerRatePage");
        preRequest(model, request);
        
        long playerId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_ID, -1);
        if(playerId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player id parameter.");
            return viewName;
        }
        try{
            UrTPlayerStats playerStats = playerManager.getPlayerStatsById(playerId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PLAYER_STATS, playerStats);
            
            File outFile = getChartFile(UrTChartService.CHART_PLAYER_RATE, playerId);
            UrTChartInfo chartInfo = chartService.generatePlayerRateChart(playerStats, outFile);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_CHART_TYPE, UrTChartService.CHART_PLAYER_RATE);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_CHART_INFO, chartInfo);
        } catch (NoSuchObjectException e) {
            getLogger().error("Failed to get player", e);
            String idStr = ServletRequestUtils.getStringParameter(request, UrTWebConstants.REQUEST_PARAM_ID, "unknown");
            String msg = getMessage(UrTWebConstants.ERROR_CODE_OBJECT_NOT_FOUND_BY_ID, new Object[]{idStr});
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (JdbcException e){
            getLogger().error("Failed to get player stats for player with ID: " + playerId, e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (ChartException e) {
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return viewName;
    }
    
    /**
     * Sends chart image with response
     * 
     * @param req {@link HttpServletRequest} request
     * @param resp {@link HttpServletResponse} response
     */
    @RequestMapping(value = {"/portal/chart_img"})
    public void chartImage(HttpServletRequest req, HttpServletResponse resp){
        long chartId = ServletRequestUtils.getLongParameter(req, UrTWebConstants.REQUEST_PARAM_ID, -1);
        String chartType = ServletRequestUtils.getStringParameter(req, UrTWebConstants.REQUEST_PARAM_CHART_TYPE, null);
        if(chartId == -1){
            logger.error("Missing chart id parameter.");
            resp.setStatus(404);
            return;
        }
        if(chartType == null){
            logger.error("Missing chart type parameter.");
            resp.setStatus(404);
            return;
        }
        File imgFile = getChartFile(chartType, chartId);
        sendImageWithResponse(resp, imgFile);
    }

    
    /**
     * Returns chart file with name generated by params 
     * 
     * @param chartType {@link String} chart type
     * @param chartId long chart ID
     * 
     * @return {@link File}
     */
    protected File getChartFile(String chartType, long chartId) {
        File result = new File(chartStorageDir, chartType + "_" + chartId + CHART_IMG_FILE_EXT);
        return result;
    }
    
    /**
     * Send image with response
     * 
     * @param resp {@link HttpServletResponse} response
     * @param file {@link File} image file
     * 
     */
    protected void sendImageWithResponse(HttpServletResponse resp, File file) {
        FileInputStream fis = null;
        try {
            getLogger().debug("Sending image.");
            resp.setContentType(CHART_IMAGE_MIME_TYPE);
//            if(contentLength > 0){
//                resp.setContentLength(contentLength);
//            }
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, resp.getOutputStream());
            resp.setStatus(200);
            getLogger().debug("Image sent.");
        } catch (IOException e) {
            getLogger().error("Failed to send image with response", e);
            resp.setStatus(500);
            return;
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    getLogger().warn("Failed to close file input stream", e);
                }
            }
        }
    }

    
    /**
     * Context Init method
     */
    @PostConstruct
    protected void init() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        if(!tmpDirPath.endsWith(File.separator)){
            tmpDirPath+= File.separator;
        }
        
        tmpDirPath+= servletContext.getContextPath();
        tmpDirPath+= File.separator;
        
        tmpDirPath+= CHART_STORAGE_DIR_NAME;
        chartStorageDir = new File(tmpDirPath);
        if(!chartStorageDir.exists()){
            chartStorageDir.mkdirs();
        }
    }

}
