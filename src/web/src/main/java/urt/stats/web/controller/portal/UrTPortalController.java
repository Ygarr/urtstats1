package urt.stats.web.controller.portal;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTMapManager;
import urt.stats.core.service.UrTMatchManager;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMatch;
import urt.stats.core.vo.UrTMatchCountAchv;
import urt.stats.core.vo.UrTMatchHitStat;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTPager;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.UrTPlayerWeaponStats;
import urt.stats.core.vo.UrTSortHelper;
import urt.stats.web.controller.UrTController;
import urt.stats.web.controller.constants.UrTWebConstants;
import urt.stats.web.exception.UrTErrorDetails;

/**
 * Portal controller
 * 
 * @author ghost
 *
 */
@Controller
public class UrTPortalController extends UrTController {

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
     * Map manager
     */
    @Autowired
    private UrTMapManager mapManager;
    
    /**
     * Main page handler
     * 
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name for main page
     */
    @RequestMapping(value = {"", "/", "/main", "/portal"})
    public String mainPage(Model model, HttpServletRequest request){
        getLogger().debug("MainPage");
        preRequest(model, request);
        try {
            int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
            boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, false);
            UrTSortHelper sortHelper = matchManager.getSortHelper(sortCol, isAsc);
            List<UrTMatch> matchList = matchManager.getMatchListForCurrentDay(sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH_LIST, matchList);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
        } catch (JdbcException e) {
            getLogger().error("Failed to get match list", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return UrTWebConstants.VIEW_PORTAL_MAIN;
    }
    
    /**
     * Match list page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/matches"})
    public String matchListPage(Model model, HttpServletRequest request){
        getLogger().debug("MatchListPage");
        preRequest(model, request);
        int page = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE, UrTPager.DEFAULT_PAGE_NUM);
        int pageSize = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE_SIZE, getUrtStatsConfig().getDefaultPageSize());
        if(page <= 0){
            getLogger().warn("Invalid page value: '" + page + "'");
        }
        if(pageSize <= 0 || pageSize > 100){
            getLogger().warn("Invalid page size value: '" + pageSize + "' (supported values: 1..100)");
        }
        try {
            int matchCount = matchManager.getMatchCount();
            getLogger().debug("Total match count: " + matchCount);
            getLogger().debug("page_num: " + page);
            getLogger().debug("pageSize: " + pageSize);
            UrTPager pager = new UrTPager(matchCount, page, pageSize);
            int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
            boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, false);
            UrTSortHelper sortHelper = matchManager.getSortHelper(sortCol, isAsc);
            List<UrTMatch> matchList = matchManager.getMatchPagedList(pager.getRecordOffset(), pager.getPageSize(), sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PAGER, pager);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH_LIST, matchList);
        } catch (JdbcException e) {
            getLogger().error("Failed to get match list", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return UrTWebConstants.VIEW_PORTAL_MATCHES;
    }
    
    /**
     * Match details page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/match_details"})
    public String matchDetailsPage(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_MATCH_DETAILS;
        getLogger().debug("MatchDetailsPage");
        preRequest(model, request);
        long matchId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_ID, -1);
        if(matchId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing match id parameter.");
            return viewName;
        }
        try {
            UrTMatchStats match = matchManager.getMatchDetails(matchId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH, match);
            
            try{
                List<UrTMatchHitStat> bestHits = matchManager.getMatchBestHits(matchId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_BEST_HITS, bestHits);
            } catch (JdbcException e) {
                getLogger().error("Failed to get match best hits", e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_HITS, new UrTErrorDetails(msg, e));
            }
            try{
                List<UrTMatchCountAchv> bestAchievs = matchManager.getMatchBestAchievs(matchId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_BEST_ACHVS, bestAchievs);
            } catch (JdbcException e) {
                getLogger().error("Failed to get match best achievs", e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_ACHVS, new UrTErrorDetails(msg, e));
            }
        } catch (NoSuchObjectException e) {
            getLogger().error("Failed to get match details", e);
            String idStr = ServletRequestUtils.getStringParameter(request, UrTWebConstants.REQUEST_PARAM_ID, "unknown");
            String msg = getMessage(UrTWebConstants.ERROR_CODE_OBJECT_NOT_FOUND_BY_ID, new Object[]{idStr});
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (JdbcException e) {
            getLogger().error("Failed to get match details", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } 
        
        return viewName;
    }
    
    
    /**
     * Player list page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/players"})
    public String playerListPage(Model model, HttpServletRequest request){
        getLogger().debug("PlayerListPage");
        preRequest(model, request);
        try {
            int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
            boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, false);
            UrTSortHelper sortHelper = playerManager.getSortHelper(sortCol, isAsc);
            List<UrTPlayerStats> playerList = playerManager.getAllPlayerStats(sortHelper);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PLAYER_LIST, playerList);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
        } catch (JdbcException e) {
            getLogger().error("Failed to get all players", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        return UrTWebConstants.VIEW_PORTAL_PLAYERS;
    }
    
    /**
     * Player details page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/player_details"})
    public String playerDetailsPage(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_PLAYER_DETAILS;
        getLogger().debug("PlayerDetailsPage");
        preRequest(model, request);
        long playerId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_ID, -1);
        if(playerId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player id parameter.");
            return viewName;
        }
        try {
            UrTPlayerStats playerStats = playerManager.getPlayerStatsById(playerId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PLAYER_STATS, playerStats);
            try{
                int matchCount = matchManager.getMatchCountByPlayerId(playerId);
                int page = UrTPager.DEFAULT_PAGE_NUM;
                int pageSize = getUrtStatsConfig().getDefaultPageSize();
                getLogger().debug("Total match count: " + matchCount);
                getLogger().debug("page_num: " + page);
                getLogger().debug("pageSize: " + pageSize);
                UrTPager pager = new UrTPager(matchCount, page, pageSize);
                int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
                boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, false);
                UrTSortHelper sortHelper = matchManager.getSortHelper(sortCol, isAsc);
                List<UrTMatch> matchHistory = matchManager.getMatchPagedListByPlayerId(playerStats.getId(), pager.getRecordOffset(), pager.getPageSize(), sortHelper);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH_LIST, matchHistory);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_PAGER, pager);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
            } catch (JdbcException e){
                getLogger().error("Failed to get match history for player with ID: " + playerId, e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_MATCH, new UrTErrorDetails(msg, e));
            }
            try{
                Map<String, List<UrTPlayerWeaponStats>> wpnStats = playerManager.getPlayerWpnStats(playerId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_WEAPONS, wpnStats);
            } catch (JdbcException e){
                getLogger().error("Failed to get weapon stats for player with ID: " + playerId, e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_WPNS, new UrTErrorDetails(msg, e));
            }
            try{
                List<UrTPlayerMatchHitStat> hitStats = playerManager.getAllPlayerHits(playerId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_HIT_STAT_LIST, hitStats);
            } catch (JdbcException e){
                getLogger().error("Failed to get hit stats for player with ID: " + playerId, e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_HITS, new UrTErrorDetails(msg, e));
            }
            
            try{
                List<UrTPlayerMatchCountAchv> achvList = playerManager.getAllPlayerAchvs(playerId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ACHV_LIST, achvList);
            } catch (JdbcException e){
                getLogger().error("Failed to get achvs for player with ID: " + playerId, e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_ACHVS, new UrTErrorDetails(msg, e));
            }
        } catch (NoSuchObjectException e) {
            getLogger().error("Failed to get player", e);
            String idStr = ServletRequestUtils.getStringParameter(request, UrTWebConstants.REQUEST_PARAM_ID, "unknown");
            String msg = getMessage(UrTWebConstants.ERROR_CODE_OBJECT_NOT_FOUND_BY_ID, new Object[]{idStr});
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (JdbcException e) {
            getLogger().error("Failed to get player", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } 
        return viewName;
    }
    
    /**
     * Player match details page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/player_match_details"})
    public String playerMatchDetailsPage(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_PLAYER_MATCH_DETAILS;
        getLogger().debug("PlayerMatchDetailsPage");
        preRequest(model, request);
        long playerMatchId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID, -1);
        if(playerMatchId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_PLAYER_MATCH_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing player match id parameter.");
            return viewName;
        }
        try {
            UrTPlayerMatchStats playerMatchStats = playerManager.getPlayerMatchStatsById(playerMatchId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_PLAYER_MATCH_STATS, playerMatchStats);
            try{
                UrTMatch match = matchManager.getMatch(playerMatchStats.getMatchId());
                model.addAttribute(UrTWebConstants.MODEL_ATTR_MATCH, match);
            } catch (JdbcException e){
                getLogger().error("Failed to get match details by ID: " + playerMatchStats.getMatchId(), e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_MATCH, new UrTErrorDetails(msg, e));
            }
            try{
                Map<String, List<UrTPlayerWeaponStats>> wpnStats = playerManager.getPlayerMatchWpnStats(playerMatchId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_WEAPONS, wpnStats);
            } catch (JdbcException e){
                getLogger().error("Failed to get weapon stats for player match with ID: " + playerMatchId, e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_WPNS, new UrTErrorDetails(msg, e));
            }
            try {
                List<UrTPlayerMatchHitStat> hitStatList = playerManager.getPlayerMatchHits(playerMatchId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_HIT_STAT_LIST, hitStatList);
            } catch(JdbcException e){
                getLogger().error("Failed to get hit stat list", e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_HITS, new UrTErrorDetails(msg, e));
            }
            try {
                List<UrTPlayerMatchCountAchv> achvsList = playerManager.getPlayerMatchAchvs(playerMatchId);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ACHV_LIST, achvsList);
            } catch(JdbcException e){
                getLogger().error("Failed to get achv list", e);
                String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
                model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR_BEST_ACHVS, new UrTErrorDetails(msg, e));
            }
        } catch (NoSuchObjectException e) {
            getLogger().error("Failed to get player match stats", e);
            String idStr = ServletRequestUtils.getStringParameter(request, UrTWebConstants.REQUEST_PARAM_ID, "unknown");
            String msg = getMessage(UrTWebConstants.ERROR_CODE_OBJECT_NOT_FOUND_BY_ID, new Object[]{idStr});
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (JdbcException e) {
            getLogger().error("Failed to get player match stats", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } 
        return viewName;
    }
    
    /**
     * Map list page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/maps"})
    public String mapListPage(Model model, HttpServletRequest request){
        getLogger().debug("MapListPage");
        preRequest(model, request);
//        int page = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE, UrTPager.DEFAULT_PAGE_NUM);
//        int pageSize = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_PAGE_SIZE, getUrtStatsConfig().getDefaultPageSize());
//        if(page <= 0){
//            getLogger().warn("Invalid page value: '" + page + "'");
//        }
//        if(pageSize <= 0 || pageSize > 100){
//            getLogger().warn("Invalid page size value: '" + pageSize + "' (supported values: 1..100)");
//        }
        try {
//            int mapCount = mapManager.getMapCount();
//            if(getLogger().isDebugEnabled()){
//                getLogger().debug("Total map count: " + mapCount);
//                getLogger().debug("page_num: " + page);
//                getLogger().debug("pageSize: " + pageSize);
//            }
//            UrTPager pager = new UrTPager(mapCount, page, pageSize);
            
            int sortCol = ServletRequestUtils.getIntParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_BY, -1);
            boolean isAsc = ServletRequestUtils.getBooleanParameter(request, UrTWebConstants.REQUEST_PARAM_SORT_ASC, true);
            UrTSortHelper sortHelper = mapManager.getSortHelper(sortCol, isAsc);
            List<UrTMap> mapList = mapManager.getMapList(sortHelper);
//            model.addAttribute(UrTWebConstants.MODEL_ATTR_PAGER, pager);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MAP_LIST, mapList);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_SORT, sortHelper);
        } catch (JdbcException e) {
            getLogger().error("Failed to get match list", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        }
        
        return UrTWebConstants.VIEW_PORTAL_MAPS;
    }
    
    /**
     * Map details page handler
     *  
     * @param model {@link Model} model
     * @param request {@link HttpServletRequest} servlet request
     * 
     * @return {@link String} view name
     */
    @RequestMapping(value = {"/portal/map_details"})
    public String mapDetailsPage(Model model, HttpServletRequest request){
        String viewName = UrTWebConstants.VIEW_PORTAL_MAP_DETAILS;
        getLogger().debug("MapDetailsPage");
        preRequest(model, request);
        long mapId = ServletRequestUtils.getLongParameter(request, UrTWebConstants.REQUEST_PARAM_ID, -1);
        if(mapId == -1){
            String errMsg = getMessage(UrTWebConstants.ERROR_CODE_MISSING_PARAMETER, new Object[]{UrTWebConstants.REQUEST_PARAM_ID});
            UrTErrorDetails error = new UrTErrorDetails(errMsg, null);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, error);
            logger.error("Missing map id parameter.");
            return viewName;
        }
        try {
            UrTMap map = mapManager.getMapWithImagesById(mapId);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_MAP, map);
        } catch (NoSuchObjectException e) {
            getLogger().error("Failed to get map", e);
            String idStr = ServletRequestUtils.getStringParameter(request, UrTWebConstants.REQUEST_PARAM_ID, "unknown");
            String msg = getMessage(UrTWebConstants.ERROR_CODE_OBJECT_NOT_FOUND_BY_ID, new Object[]{idStr});
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } catch (JdbcException e) {
            getLogger().error("Failed to get map", e);
            String msg = getMessage(UrTWebConstants.ERROR_CODE_UNKNOWN_ERROR);
            model.addAttribute(UrTWebConstants.MODEL_ATTR_ERROR, new UrTErrorDetails(msg, e));
        } 
        return viewName;
    }


}
