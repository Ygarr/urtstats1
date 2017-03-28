package urt.stats.core.vo;

/**
 * Sort helper
 * 
 * @author ghost
 *
 */
public class UrTSortHelper {

    /**
     * Sort column number
     */
    private int column = -1;
    
    /**
     * Sort direction
     */
    private boolean asc = true;
    
    /**
     * Column count
     */
    private int columnCount = -1;

    /**
     * Parameterized constructor
     * 
     * @param column int Sort column number
     * @param asc boolean Sort direction
     * @param columnCount int Column count
     */
    public UrTSortHelper(int column, boolean asc, int columnCount) {
        this.column = column;
        this.asc = asc;
        this.columnCount = columnCount;
    }

    /**
     * Returns sort column number
     * 
     * @return int - sort column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sort direction
     * 
     * @return boolean Sort direction
     */
    public boolean isAsc() {
        return asc;
    }

    /**
     * Column count
     * @return int - Column count
     */
    public int getColumnCount() {
        return columnCount;
    }

    public String toString() {
        return "UrTSortHelper [column=" + column + ", isAsc=" + asc
                + ", columnCount=" + columnCount + "]";
    }
    
}
