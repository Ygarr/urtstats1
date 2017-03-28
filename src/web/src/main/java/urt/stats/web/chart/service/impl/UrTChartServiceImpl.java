package urt.stats.web.chart.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.core.vo.chart.UrTChartDataItem;
import urt.stats.core.vo.chart.UrTPlayerMatchChartDataItem;
import urt.stats.web.chart.exception.ChartException;
import urt.stats.web.chart.service.UrTChartService;
import urt.stats.web.chart.theme.UrTChartTheme;
import urt.stats.web.chart.theme.UrTXYChartTheme;
import urt.stats.web.chart.vo.UrTChartInfo;

/**
 * Chart service implementation
 * @author ghost
 */
@Component
public class UrTChartServiceImpl implements UrTChartService {

    /**
     * Player rate chart title 
     */
    protected static final String MSG_PLAYER_RATE_TITLE = "chart.player_rate.title";
    
    /**
     * Player rate chart serires label 
     */
    protected static final String MSG_PLAYER_RATE_SERIES = "chart.player_rate.series_name";

    /**
     * Player rate chart rate axis label 
     */
    protected static final String MSG_PLAYER_RATE_AXIS_RATE = "chart.player_rate.axis_rate";
    /**
     * Player rate chart date axis label 
     */
    protected static final String MSG_PLAYER_RATE_AXIS_DATE = "chart.player_rate.axis_date";
    
    /**
     * Chart Image type
     */
    protected static final String CHART_IMG_TYPE = "PNG";
    
    /**
     * Timeline chart height (px)
     */
    protected static final int TIMELINE_CHART_HEIGHT = 300;
    
    /**
     * Timeline chart DAY width (px)<br/>
     * Total width will be calculated as "dataset day count" * "day width" (px)
     */
    protected static final int TIMELINE_CHART_DAY_WIDTH = 100;

    /**
     * Milliseconds in day
     */
    protected static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
    
    /**
     * Logger
     */
    private Logger logger = Logger.getLogger(getClass());
    
    /**
     * Default XY chart theme
     */
    private UrTChartTheme xyChartTheme = new UrTXYChartTheme();

    /**
     * Date format for axis tick labels
     */
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    
    /**
     * Double value format for axis tick labels
     */
    private NumberFormat doubleFormat = new DecimalFormat("0.00");
    
    /**
     * Player manager
     */
    @Autowired
    private UrTPlayerManager playerManager;
    
    /**
     * Message source
     */
    @Autowired
    private MessageSource messageSource;
    
    public UrTChartInfo generatePlayerRateChart(UrTPlayerStats playerStats, File outFile) throws ChartException {
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        dataset.setDomainIsPointsInTime(true);
        UrTChartInfo chartInfo = null;
        try {
            List<UrTPlayerMatchChartDataItem> rs = playerManager.getPlayerMatchStatsByPlayerId(playerStats.getId());
            if(!rs.isEmpty()){
                String seriesName = getMessage(MSG_PLAYER_RATE_SERIES);
                for(UrTPlayerMatchChartDataItem pm : rs){
//                    Day period = new Day(pm.getMatch().getStartTime());
                    SimpleTimePeriod period = new SimpleTimePeriod(pm.getMatch().getStartTime(), pm.getMatch().getEndTime());
                    dataset.add(
                            period
                          , pm.getPlayerMatchStats().getKDRatio()
                          , seriesName);
                }
            }
            
            int days  = 1;
            Date minDate = rs.get(0).getMatch().getStartTime();
            Date maxDate = rs.get(rs.size()-1).getMatch().getStartTime();
            long deltaMillis = (maxDate.getTime() - minDate.getTime());
            if(deltaMillis > MILLIS_PER_DAY){
                days = (int) (deltaMillis / MILLIS_PER_DAY);
            }
            
            String chartTitle = getMessage(MSG_PLAYER_RATE_TITLE, new Object[]{playerStats.getPlayerName()});
            chartInfo = generateTimeLineChart(
                      dataset
                    , playerStats.getAvgKDRatio()
                    , days
                    , chartTitle
                    , getMessage(MSG_PLAYER_RATE_AXIS_DATE)
                    , getMessage(MSG_PLAYER_RATE_AXIS_RATE)
                    , outFile);
            EntityCollection entities = chartInfo.getRenderingInfo().getEntityCollection();
            
            List<UrTChartDataItem> chartDataList = new ArrayList<UrTChartDataItem>();
            int deltaX = chartInfo.getFixedAreaWidth();
            for(int i=0; i< entities.getEntityCount(); i++) {
                ChartEntity chartEntity = entities.getEntity(i);
                if(chartEntity instanceof XYItemEntity){
                    XYItemEntity xyEntity = (XYItemEntity)chartEntity;
                    int itemIndex = xyEntity.getItem();
                    if(rs.size() > itemIndex){
                        UrTChartDataItem chartDataItem = rs.get(itemIndex);
                        chartDataItem.setItemBounds(xyEntity.getArea().getBounds(), deltaX);
                        chartDataList.add(chartDataItem);
                    }
                }
            }
            chartInfo.setChartData(chartDataList);

        } catch (Exception e) {
            logger.error("Failed to enerate player rate chart.", e);
            throw new ChartException("Failed to enerate player rate chart.", e); 
        }
        return chartInfo;
    }
    
    /**
     * Generates timeline cart
     * 
     * @param dataset {@link XYDataset} dataset
     * @param avgValue {@link Double} average value (if null - will be ignored)
     * @param days int - day count
     * @param chartTitle {@link String} chart title
     * @param timeAxislabel {@link String} label text for the time axis
     * @param valueAxisLabel {@link String} label text for value axis
     * @param outFile {@link File} file for chart storing
     * 
     * @return {@link UrTChartInfo} chart info
     * 
     * @throws ChartException o failure
     */
    protected UrTChartInfo generateTimeLineChart(
            XYDataset dataset
          , Double avgValue
          , int days
          , String chartTitle
          , String timeAxislabel
          , String valueAxisLabel
          , File outFile) throws ChartException {
        UrTChartInfo chartInfo = null;
        try {
            logger.debug("Generating chart...");
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    chartTitle
                  , timeAxislabel
                  , valueAxisLabel
                  , dataset
                  , true
                  , true
                  , true);
            
            chart.setBackgroundImageAlpha(0f);

            XYPlot xyPlot = chart.getXYPlot();
            
            DateAxis dateAxis = (DateAxis)xyPlot.getDomainAxis();
            dateAxis.setDateFormatOverride(dateFormat);
//            dateAxis.setAutoTickUnitSelection(false);
//            dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 5)); 
            
            NumberAxis rangeAxis = (NumberAxis)xyPlot.getRangeAxis();
            rangeAxis.setNumberFormatOverride(doubleFormat);
            
            xyChartTheme.applyToChart(chart);
            
            chart.removeLegend();
//            xyPlot.getRangeAxis().setMinorTickCount(10);
//            xyPlot.getDomainAxis().setMinorTickCount(dates.length);
            
            if(avgValue != null){
                Marker marker = xyChartTheme.createValueMarker(avgValue.doubleValue());
                xyPlot.addRangeMarker(marker);
            }
            
            logger.debug("Exporting chart to image...");
            int chartWidth = TIMELINE_CHART_DAY_WIDTH * days + 70;
            ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
            BufferedImage img = chart.createBufferedImage(chartWidth, TIMELINE_CHART_HEIGHT, renderingInfo);
            ImageIO.write(img, CHART_IMG_TYPE, outFile);
            chartInfo = new UrTChartInfo(renderingInfo);
            logger.debug("Chart ready: '" + outFile.getAbsolutePath() + "'.");
        } catch (Exception e) {
            logger.error("Failed to generate time line chart.", e);
            throw new ChartException("Failed to generate time line chart.", e);
        } 
        return chartInfo;
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
