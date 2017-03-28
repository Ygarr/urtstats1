package urt.stats.reports.enums;

/**
 * Report templates
 * @author ghost
 *
 */
public enum UrTReportTemplate {

    /**
     * Player rate for all the time
     */
    PLAYER_RATE {
        public String getTemplateRelativeDir(){
            return "player/";
        }
        public String getTemplateName(){
            return "player_rate.jasper";
        }
        public String getTargetFileName(){
            return "player_rate";
        }
    },
    
    ;

    /**
     * Returns template path starting with template base dir
     * 
     * @return template path starting with template base dir
     */
    public String getTemplatePath() {
        return getTemplateBaseDir() + getTemplateRelativeDir() + getTemplateName();
    }
    
    /**
     * Returns template file name
     * 
     * @return template file name
     */
    public String getTemplateName(){
        return "undefined";
    }
    
    /**
     * Returns generated report file name (without extension).
     * 
     * @return generated report file name
     */
    public String getTargetFileName(){
        return "undefined";
    }
    
    /**
     * Returns template relative dir path
     * 
     * @return template relative dir path
     */
    public String getTemplateRelativeDir(){
        return "undefined";
    }

    /**
     * Returns template base dir
     * 
     * @return template base dir.
     */
    public String getTemplateBaseDir(){
        return "compiled_reports/";
    }

}
