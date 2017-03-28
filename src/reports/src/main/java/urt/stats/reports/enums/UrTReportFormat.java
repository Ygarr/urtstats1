package urt.stats.reports.enums;

/**
 * Report formats
 * @author ghost
 *
 */
public enum UrTReportFormat {

    /**
     * Adobe PDF
     */
    PDF {
        public String getContentType() {
            return "application/pdf";
        }
     }
    /**
     * HTML
     */
    ,HTML {
        public String getContentType() {
            return "text/html";
        }
    }
    /**
     * Microsoft Excel
     */
    ,XLS {
        public String getContentType() {
            return "application/xls";
        }
    }
    /**
     * Comma separated values
     */
    ,CSV {
        public String getContentType() {
            return "text/plain";
        }
    };
    
    /**Returns format's content type
     * 
     * @return {@link String} - mime type (e.g.: "application/xls")
     */
    public String getContentType() {
        return "application/octet-stream";
    }
    
}
