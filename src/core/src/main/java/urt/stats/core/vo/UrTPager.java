package urt.stats.core.vo;

/**
 * Pager VO
 * @author ghost
 *
 */
public class UrTPager {

    /**
     * Default page number
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    
    /**
     * Default page size
     */
    public static final int DEFAULT_PAGE_SIZE = 20; 
    
    /**
     * Record offset (how many record to skip)
     */
    private int recordOffset = 0;
    
    /**
     * Page size
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    /**
     * Page number (first page = 1)
     */
    private int pageNum = 1;

    /**
     * Total page count
     */
    private int pageCount = 0;
    
    /**
     * Record count
     */
    private int recordCount = 0;
    
    /**
     * Parameterized constructor
     * 
     * @param recordCount int - total record count
     * @param pageNum int - current page (first = 1)
     * @param pageSize int - page size (default={@link #DEFAULT_PAGE_SIZE})
     */
    public UrTPager(int recordCount, int pageNum, int pageSize){
        this.recordCount = recordCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        
        setPageCount(recordCount / pageSize);
        if((recordCount % pageSize) != 0){
            setPageCount(getPageCount() + 1);
        }
        if(pageNum == 1){
            setRecordOffset(0);
        } else {
            setRecordOffset((pageNum-1) * pageSize);
        }
    }
    
    /**
     * Returns offset
     * 
     * @return int - offset
     */
    public int getRecordOffset() {
        return recordOffset;
    }

    /**
     * Sets offset
     * @param offset int - offset
     */
    public void setRecordOffset(int offset) {
        this.recordOffset = offset;
    }

    /**
     * Returns page size
     * 
     * @return int - page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets page size
     * @param pageSize int - Page size)
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns page number (first page = 1)
     * 
     * @return int - page number
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * Sets page number (first page = 1)
     * 
     * @param pageNum int - page number
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * Returns total record count
     * 
     * @return int - total record count
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * Returns total page count
     * @return int - total page count
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Sets total page count
     * 
     * @param pageCount int - total page count
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * Sets total record count
     * @param recordCount int - total record count
     */
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + pageCount;
        result = prime * result + pageNum;
        result = prime * result + pageSize;
        result = prime * result + recordCount;
        result = prime * result + recordOffset;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPager other = (UrTPager) obj;
        if (pageCount != other.pageCount)
            return false;
        if (pageNum != other.pageNum)
            return false;
        if (pageSize != other.pageSize)
            return false;
        if (recordCount != other.recordCount)
            return false;
        if (recordOffset != other.recordOffset)
            return false;
        return true;
    }

    public String toString() {
        return "UrTPager [recordOffset=" + recordOffset + ", pageSize="
                + pageSize + ", pageNum=" + pageNum + ", pageCount="
                + pageCount + ", recordCount=" + recordCount + "]";
    }

}
