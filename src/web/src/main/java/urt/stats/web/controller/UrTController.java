package urt.stats.web.controller;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;

import urt.stats.core.util.StringUtil;
import urt.stats.web.UrTStatsConfig;
import urt.stats.web.controller.constants.UrTWebConstants;
import urt.stats.web.util.UrTWebUtil;

/**
 * Base controller class
 * 
 * @author ghost
 */
public class UrTController {

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * Message source
     */
    @Autowired
    private MessageSource messageSource;
    
    /**
     * UrT stats config
     */
    @Autowired
    private UrTStatsConfig urtStatsConfig;
    
    /**
     * Returns message source
     * 
     * @return {@link MessageSource} message source
     */
    protected MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * Returns UrT stats config
     * @return {@link UrTStatsConfig} config
     */
    public UrTStatsConfig getUrtStatsConfig() {
        return urtStatsConfig;
    }

    /**
     * Returns logger
     * 
     * @return {@link Logger} logger
     */
    protected Logger getLogger() {
        return logger;
    }
    
    /**
     * Preprocess request
     * 
     * @param model {@link Model} data model
     * @param request {@link HttpServletRequest} request
     */
    protected void preRequest(Model model, HttpServletRequest request) {
        String clientIp = UrTWebUtil.getClientIp(request);
        getLogger().debug("Client IP: " + clientIp);
        model.addAttribute(UrTWebConstants.MODEL_ATTR_TIMESTAMP, System.currentTimeMillis());
        model.addAttribute(UrTWebConstants.MODEL_ATTR_CONFIG, getUrtStatsConfig());
        Enumeration<String> paramNames = request.getParameterNames();
        StringBuilder reqParams = new StringBuilder("");
        if(paramNames != null && paramNames.hasMoreElements()){
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                if(!UrTWebConstants.REQUEST_PARAM_LANG.equals(paramName)){
                    String paramValue = request.getParameter(paramName);
                    if(!StringUtil.hasText(paramValue)){
                        paramValue = "";
                    }
                    reqParams.append(paramName);
                    reqParams.append("=");
                    reqParams.append(paramValue);
                }
            }
        }
        
        model.addAttribute(UrTWebConstants.MODEL_ATTR_REQ_PARAMS, reqParams.toString());
    }

    /**
     * Returns localized message text from resource bundle
     * 
     * @param messageCode {@link String}
     * 
     * @return {@link String} localized message text from resource bundle
     */
    protected String getMessage(String messageCode){
        return getMessage(messageCode, null);
    }
    
    /**
     * Returns localized message text from resource bundle
     * 
     * @param messageCode {@link String}
     * @param args - array of {@link Object} message arguments 
     * 
     * @return {@link String} localized message text from resource bundle
     */
    protected String getMessage(String messageCode, Object[] args){
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, args, messageCode, locale);
    }
    
}
