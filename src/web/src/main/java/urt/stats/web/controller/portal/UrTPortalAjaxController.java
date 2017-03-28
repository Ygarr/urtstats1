package urt.stats.web.controller.portal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.service.UrTMatchManager;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTPager;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;
import urt.stats.core.vo.UrTSortHelper;
import urt.stats.web.controller.UrTController;
import urt.stats.web.controller.constants.UrTWebConstants;
import urt.stats.web.exception.UrTErrorDetails;

/**
 * Portal Ajax-processing request controller
 * 
 * @author ghost
 *
 */
@Controller
public class UrTPortalAjaxController extends UrTController {

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * Match manager
     */
    @Autowired
    private UrTMatchManager matchManager;

    /**
     * Player manager
     */
    @Autowired
    private UrTPlayerManager playerManager;
    
    /**
     * Player match hits Ajax request handler
     * 
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name for the ajax request
     */
    @RequestMapping(value = {"/portal/ajax/pmhits"})
    public String playerHitsRequest(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_AJAX_PLAYER_MATCH_HITS;  
        getLogger().debug("PlayerMatchHits Ajax request");
        long playerMatchId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID, -1);
        if(playerMatchId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player match id parameter.");
            return viewName;
        }
        try {
            List<UrTPlayerMatchHitStat> hitStatList = playerManager.getPlayerMatchHits(playerMatchId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_HIT_STAT_LIST, hitStatList);
        } catch(JdbcException e){
            getLogger().error("Failed to get hit stat list", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return viewName;
    }
    
    /**
     * Player match achiievements Ajax request handler
     * 
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name for the ajax request
     */
    @RequestMapping(value = {"/portal/ajax/pmachvs"})
    public String playerAchvsRequest(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_AJAX_PLAYER_MATCH_ACHVS;
        getLogger().debug("PlayerMatchAchvs Ajax request");
        long playerMatchId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID, -1);
        if(playerMatchId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player match id parameter.");
            return viewName;
        }
        try {
            List<UrTPlayerMatchCountAchv> achvsList = playerManager.getPlayerMatchAchvs(playerMatchId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ACHV_LIST, achvsList);
        } catch(JdbcException e){
            getLogger().error("Failed to get achv list", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return viewName;
    }

    /**
     * Player match history Ajax request handler
     * 
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name for the ajax request
     */
    @RequestMapping(value = {"/portal/ajax/pmhist"})
    public String playerMatchHistoryRequest(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_AJAX_PLAYER_MATCH_HISTORY;
        getLogger().debug("PlayerMatchAchvs Ajax request");
        long playerId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_PLAYER_ID, -1);
        if(playerId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_PLAYER_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player id parameter.");
            return viewName;
        }
        try{
            int matchCount = matchManager.getMatchCountByPlayerId(playerId);
            int page = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE, UrTPager.DEFAULT_PAGE_NUM);
            int pageSize = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE_SIZE, getUrtStatsConfig().getDefaultPageSize());
            getLogger().debug("Total match count: " + matchCount);
            getLogger().debug("page_num: " + page);
            getLogger().debug("pageSize: " + pageSize);
            UrTPager pager = new UrTPager(matchCount, page, pageSize);
            int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
            boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, false);
            UrTSortHelper sortHelper = matchManager.getSortHelper(sortCol, isAsc);
            List<UrTMatch> matchHistory = matchManager.getMatchPagedListByPlayerId(playerId, pager.getRecordOffset(), pager.getPageSize(), sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH_LIST, matchHistory);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PAGER, pager);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PLAYER_ID, playerId);
        } catch (JdbcException e){
            getLogger().error("Failed to get match history for player with ID: " + playerId, e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return viewName;
    }


}
