package urt.stats.core.vo.chart;

import java.awt.Rectangle;

/**
 * Chart data interface
 * @author ghost
 *
 */
public interface UrTChartDataItem {

    /**
     * Returns X coordinate of the data item
     * @return int - X coordinate of the data item
     */
    public int getItemX();
    
    /**
     * Returns Y coordinate of the data item
     * @return int - Y coordinate of the data item
     */
    public int getItemY();
    
    /**
     * Returns width of the data item
     * @return int - width of the data item
     */
    public int getItemWidth();
    
    /**
     * Returns height of the data item
     * @return int - height of the data item
     */
    public int getItemHeight();
    
    /**
     * Sets item bounds
     * 
     * @param rect {@link Rectangle} rectangular bounds
     * @param deltaX int - delta X value
     */
    public void setItemBounds(Rectangle rect, int deltaX);
}
