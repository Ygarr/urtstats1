package urt.stats.web.controller.constants;

/**
 * Web constants
 * 
 * @author ghost
 *
 */
public interface UrTWebConstants {


/* REQUEST PARAMS*/
    public static final String REQUEST_PARAM_ID                       = "id";
    public static final String REQUEST_PARAM_PLAYER_ID                = "player_id";
    public static final String REQUEST_PARAM_PAGE                     = "page";
    public static final String REQUEST_PARAM_PAGE_SIZE                = "page_size";
    public static final String REQUEST_PARAM_PLAYER_MATCH_ID          = "player_match_id";
    public static final String REQUEST_PARAM_SORT_BY                  = "sort_by";
    public static final String REQUEST_PARAM_SORT_ASC                 = "sort_asc";
    public static final String REQUEST_PARAM_CHART_TYPE               = "chart_type";
    public static final String REQUEST_PARAM_LANG                     = "lang";
    
    
/* MODEL PARAMS*/
    public static final String MODEL_ATTR_CONFIG                      = "urtStatsConfig";
    public static final String MODEL_ATTR_REQ_PARAMS                  = "reqParams";
    public static final String MODEL_ATTR_TIMESTAMP                   = "tstamp";
    public static final String MODEL_ATTR_MATCH_LIST                  = "matchList";
    public static final String MODEL_ATTR_PAGER                       = "pager";
    public static final String MODEL_ATTR_MATCH                       = "match";
    public static final String MODEL_ATTR_BEST_HITS                   = "bestHits";
    public static final String MODEL_ATTR_BEST_ACHVS                  = "bestAchvs";
    public static final String MODEL_ATTR_PLAYER_ID                   = "playerId";
    public static final String MODEL_ATTR_PLAYER_LIST                 = "playerList";
    public static final String MODEL_ATTR_PLAYER_STATS                = "playerStats";
    public static final String MODEL_ATTR_PLAYER_MATCH_STATS          = "playerMatchStats";
    public static final String MODEL_ATTR_HIT_STAT_LIST               = "hitStatList";
    public static final String MODEL_ATTR_ACHV_LIST                   = "achvList";
    public static final String MODEL_ATTR_MAP_LIST                    = "mapList";
    public static final String MODEL_ATTR_MAP                         = "map";
    public static final String MODEL_ATTR_SORT                        = "sort";
    public static final String MODEL_ATTR_CHART_TYPE                  = "chartType";
    public static final String MODEL_ATTR_CHART_INFO                  = "chartInfo";
    public static final String MODEL_ATTR_WEAPONS                     = "weapons";
    
    public static final String MODEL_ATTR_ERROR                       = "error";
    public static final String MODEL_ATTR_ERROR_MATCH                 = "matchError";
    public static final String MODEL_ATTR_ERROR_WPNS                  = "wpnsError";
    public static final String MODEL_ATTR_ERROR_BEST_HITS             = "bestHitsError";
    public static final String MODEL_ATTR_ERROR_BEST_ACHVS            = "bestAchvsError";
    
/* VIEWS */
    public static final String VIEW_PORTAL_MAIN                       = "/portal/main";
    public static final String VIEW_PORTAL_MATCHES                    = "/portal/matches";
    public static final String VIEW_PORTAL_MATCH_DETAILS              = "/portal/match_details";
    public static final String VIEW_PORTAL_PLAYERS                    = "/portal/players";
    public static final String VIEW_PORTAL_PLAYER_DETAILS             = "/portal/player_details";
    public static final String VIEW_PORTAL_PLAYER_MATCH_DETAILS       = "/portal/player_match_details";
    public static final String VIEW_PORTAL_MAPS                       = "/portal/maps";
    public static final String VIEW_PORTAL_MAP_DETAILS                = "/portal/map_details";
    
    public static final String VIEW_PORTAL_AJAX_PLAYER_MATCH_HITS     = "/portal/ajax/player_match_hits";
    public static final String VIEW_PORTAL_AJAX_PLAYER_MATCH_ACHVS    = "/portal/ajax/player_match_achvs";
    public static final String VIEW_PORTAL_AJAX_PLAYER_MATCH_HISTORY  = "/portal/ajax/player_match_history";
    
    public static final String VIEW_PORTAL_CHART_PLAYER_RATE          = "/portal/chart/player_rate";

    
/* ERROR CODES */
    
    public static final String ERROR_CODE_UNKNOWN_ERROR               = "error.unknown_error";
    public static final String ERROR_CODE_MISSING_PARAMETER           = "error.missing_required_param";
    public static final String ERROR_CODE_OBJECT_NOT_FOUND_BY_ID      = "error.oject_not_found_by_id";
    
    
    
}
