package urt.stats.web.chart.vo;

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jfree.chart.ChartRenderingInfo;

import urt.stats.core.vo.chart.UrTChartDataItem;

public class UrTChartInfo {

    /**
     * Chart rendering info
     */
    private ChartRenderingInfo renderingInfo = null;
    
    /**
     * Chart data
     */
    private List<UrTChartDataItem> chartData = null;
    
    /**
     * Total chart image size
     */
    private Rectangle2D chartArea = null;
    
    /**
     * Chart data area
     */
    private Rectangle2D dataArea = null;
    
    /**
     * Fixed left area with Y axis
     */
    private Rectangle2D fixedArea = null;
    
    private int fixedAreaX = 0;
    private int fixedAreaY = 0;
    private int fixedAreaWidth = 0;
    private int fixedAreaHeight = 0;
    
    private int dataAreaX = 0;
    private int dataAreaY = 0;
    private int dataAreaWidth = 0;
    private int dataAreaHeight = 0;
    
    
    /**
     * Constructor
     * 
     * @param renderingInfo {@link ChartRenderingInfo} rendering info
     */
    public UrTChartInfo(ChartRenderingInfo renderingInfo) {
        this.chartArea = renderingInfo.getChartArea();
        this.dataArea = renderingInfo.getPlotInfo().getDataArea();
        int deltaX = (int)renderingInfo.getPlotInfo().getPlotArea().getX() - 1;
        double fixedAreaX = 0;
        double fixedAreaY = 0;
        double fixedAreaWidth = chartArea.getWidth() - dataArea.getWidth();
        double fixedAreaHeight = chartArea.getHeight();
        this.fixedArea = new Rectangle2D.Double(
                  fixedAreaX
                , fixedAreaY
                , fixedAreaWidth - deltaX
                , fixedAreaHeight
                );
        this.fixedAreaX = (int)this.fixedArea.getX();
        this.fixedAreaY = (int)this.fixedArea.getY();
        this.fixedAreaWidth = (int)this.fixedArea.getWidth();
        this.fixedAreaHeight = (int)this.fixedArea.getHeight();
        
        this.dataAreaX = (int)this.dataArea.getX();
        this.dataAreaY = (int)this.dataArea.getY();
        this.dataAreaWidth = (int)this.dataArea.getWidth();
        this.dataAreaHeight = (int)this.dataArea.getHeight();
        this.renderingInfo = renderingInfo;
    }
    
    /**
     * Returns chart rendering info
     * 
     * @return {@link ChartRenderingInfo} chart rendering info
     */
    public ChartRenderingInfo getRenderingInfo() {
        return this.renderingInfo;
    }
    
    /**
     * Returns chart data
     * @return {@link UrTChartDataItem} chart data
     */
    public List<UrTChartDataItem> getChartData() {
        return chartData;
    }

    /**
     * Sets chart data
     * @param chartData chart data
     */
    public void setChartData(List<UrTChartDataItem> chartData) {
        this.chartData = chartData;
    }

    /**
     * Returns chart data(scrollable) area X coord.
     * 
     * @return int chart data(scrollable) area X coord.
     */
    public int getDataAreaX() {
        return this.dataAreaX;
    }

    /**
     * Returns chart data(scrollable) area Y coord.
     * 
     * @return int chart data(scrollable) area Y coord.
     */
    public int getDataAreaY() {
        return this.dataAreaY;
    }

    /**
     * Returns chart data(scrollable) area width
     * 
     * @return int chart data(scrollable) area width
     */
    public int getDataAreaWidth() {
        return this.dataAreaWidth;
    }

    /**
     * Returns chart data(scrollable) area height
     * 
     * @return int chart data(scrollable) area height
     */
    public int getDataAreaHeight() {
        return this.dataAreaHeight;
    }

    /**
     * Returns chart left(fixed) area X coord.
     * 
     * @return int chart left(fixed) area X coord.
     */
    public int getFixedAreaX() {
        return this.fixedAreaX;
    }

    /**
     * Returns chart left(fixed) area Y coord.
     * 
     * @return int chart left(fixed) area Y coord.
     */
    public int getFixedAreaY() {
        return this.fixedAreaY;
    }

    /**
     * Returns chart left(fixed) area width
     * 
     * @return int chart left(fixed) area width
     */
    public int getFixedAreaWidth() {
        return this.fixedAreaWidth;
    }

    /**
     * Returns chart left(fixed) area width
     * 
     * @return int chart left(fixed) area width
     */
    public int getFixedAreaHeight() {
        return this.fixedAreaHeight;
    }
    
}
