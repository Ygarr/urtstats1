package urt.stats.core.vo.chart;

import java.awt.Rectangle;

/**
 * Abstract chart data object
 * @author ghost
 *
 */
public abstract class UrTAbstractChartDataItem implements UrTChartDataItem { 
    /**
     * X coordinate of the data item
     */
    private int itemX = 0;
    
    /**
     * Y coordinate of the data item
     */
    private int itemY = 0;
    
    /**
     * width of the data item
     */
    private int itemWidth = 0;
    
    /**
     * height of the data item
     */
    private int itemHeight = 0;

    public void setItemBounds(Rectangle rect, int deltaX) {
        this.itemX = (int)rect.getX() - deltaX;
        this.itemY = (int)rect.getY();
        this.itemWidth = (int)rect.getWidth();
        this.itemHeight = (int)rect.getHeight();
    }
    
    /**
     * Returns X coordinate of the data item
     * @return int - X coordinate of the data item
     */
    public int getItemX() {
        return itemX;
    }

    /**
     * Returns Y coordinate of the data item
     * @return int - Y coordinate of the data item
     */
    public int getItemY() {
        return itemY;
    }

    /**
     * Returns width of the data item
     * @return int - width of the data item
     */
    public int getItemWidth() {
        return itemWidth;
    }

    /**
     * Returns height of the data item
     * @return int - height of the data item
     */
    public int getItemHeight() {
        return itemHeight;
    }

    /**
     * Sets height of the data item
     * @param itemHeight int - height of the data item
     */
    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }
    
    
}
