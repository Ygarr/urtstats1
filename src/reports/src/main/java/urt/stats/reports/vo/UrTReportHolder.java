package urt.stats.reports.vo;

/**
 * Report result holder
 * @author ghost
 *
 */
public class UrTReportHolder {

        /**
         * target file name (name + extension, without path)
         */
        protected String targetFileName;

        /**
         * target file mime type
         */
        protected String targetFileMimeType;

        /**
         *  output file extension
         */
        protected String targetFileExt;
        
        /**
         * Target file display name (without extension)
         * May contain spaces and non-lating letters
         */
        protected String targetFileDisplayName;
        
        /**
         * Default constructor
         */
        public UrTReportHolder(){
            
        }
        
        /**
         * Returns target file name (name + extension, without path)
         * 
         * @return - {@link String} - target file name (name + extension, without path)
         */
        public String getTargetFileName() {
            return targetFileName;
        }
        
        /**
         * Sets target file name (name + extension, without path)
         * 
         * @param targetFileName - target file name (name + extension, without path)
         */
        public void setTargetFileName(String targetFileName) {
            this.targetFileName = targetFileName;
        }

        /**
         * Returns target file mime type
         * 
         * @return {@link String} target file mime type
         */
        public String getTargetFileMimeType() {
            return targetFileMimeType;
        }

        /**
         * Sets target file mime type
         * 
         * @param targetFileMimeType - target file mime type
         */
        public void setTargetFileMimeType(String targetFileMimeType) {
            this.targetFileMimeType = targetFileMimeType;
        }

        
        /**
         * Returns output file extension
         * @return {@link String} - Returns output file extension
         */
        public String getTargetFileExt() {
            return targetFileExt;
        }

        /**
         * Sets Returns output file extension
         * @param targetFileExt - Returns output file extension
         */
        public void setTargetFileExt(String targetFileExt) {
            this.targetFileExt = targetFileExt;
        }

        /**
         * Returns Target file display name (without extension)
         * May contain spaces and non-latin letters
         * 
         * @return {@link String} - Target file display name
         */
        public String getTargetFileDisplayName() {
            return targetFileDisplayName;
        }

        /**
         * Sets Target file display name (without extension)
         * May contain spaces and non-latin letters
         * 
         * @param targetFileDisplayName - Target file display name
         */
        public void setTargetFileDisplayName(String targetFileDisplayName) {
            this.targetFileDisplayName = targetFileDisplayName;
        }

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime
                    * result
                    + ((targetFileDisplayName == null) ? 0 : targetFileDisplayName
                            .hashCode());
            result = prime * result
                    + ((targetFileExt == null) ? 0 : targetFileExt.hashCode());
            result = prime
                    * result
                    + ((targetFileMimeType == null) ? 0 : targetFileMimeType
                            .hashCode());
            result = prime * result
                    + ((targetFileName == null) ? 0 : targetFileName.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            UrTReportHolder other = (UrTReportHolder) obj;
            if (targetFileDisplayName == null) {
                if (other.targetFileDisplayName != null)
                    return false;
            } else if (!targetFileDisplayName.equals(other.targetFileDisplayName))
                return false;
            if (targetFileExt == null) {
                if (other.targetFileExt != null)
                    return false;
            } else if (!targetFileExt.equals(other.targetFileExt))
                return false;
            if (targetFileMimeType == null) {
                if (other.targetFileMimeType != null)
                    return false;
            } else if (!targetFileMimeType.equals(other.targetFileMimeType))
                return false;
            if (targetFileName == null) {
                if (other.targetFileName != null)
                    return false;
            } else if (!targetFileName.equals(other.targetFileName))
                return false;
            return true;
        }

        public String toString() {
            return "ReportHolder [targetFileName=" + targetFileName
                    + ", targetFileMimeType=" + targetFileMimeType
                    + ", targetFileExt=" + targetFileExt
                    + ", targetFileDisplayName=" + targetFileDisplayName + "]";
        }

}
