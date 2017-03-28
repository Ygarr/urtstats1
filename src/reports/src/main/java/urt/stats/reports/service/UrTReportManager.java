package urt.stats.reports.service;

import java.util.Map;

import urt.stats.reports.enums.UrTReportFormat;
import urt.stats.reports.enums.UrTReportTemplate;
import urt.stats.reports.exception.ReportException;
import urt.stats.reports.vo.UrTReportHolder;


/**
 * Report manager interface
 * @author ghost
 *
 */
public interface UrTReportManager {

    /**
     * Report target file name parameter
     */
    public static final String REPORT_PARAM_TARGET_FILE_NAME = "p_target_file_name";
    
    /**
     * Report sheet title parameter
     */
    public static final String REPORT_PARAM_SHEET_TITLE = "SHEET_TITLE";
    
    /**
     * Generates report based on the given report template and format
     * 
     * @param reportTemplate {@link UrTReportTemplate} report template
     * @param format {@link UrTReportFormat} report format
     * @param params {@link Map} report parameters
     * 
     * @return {@link UrTReportHolder} report holder
     * 
     * @throws ReportException on failure
     */
    public UrTReportHolder generateReport(UrTReportTemplate reportTemplate, UrTReportFormat format, Map<String, Object> params) throws ReportException;
    
}
