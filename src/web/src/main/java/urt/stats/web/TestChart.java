package urt.stats.web;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.ui.RectangleInsets;

public class TestChart {

    private Logger logger = Logger.getLogger(getClass());
    
    private DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    public static void main(String[] args) {
        TestChart test = new TestChart();
        test.doChart();
    }
    
    
    public void doChart(){
        logger.info("Building chart...");
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        dataset.setDomainIsPointsInTime(true);
        String dates[] = new String[]{
            "01.03.2014 11:15"
          , "01.03.2014 12:35"
          , "01.03.2014 15:58"
          , "02.03.2014 10:25"
          , "02.03.2014 16:21"
          , "04.03.2014 11:27"
          , "04.03.2014 12:42"
          , "05.03.2014 14:38"
          , "06.03.2014 14:53"
          , "07.03.2014 10:03"
          , "10.03.2014 17:30"
          , "11.03.2014 11:26"
        };
        
        String series1 = "series_name_1";
        
        Date startDate = null;
        Random random = new Random();
        for(String date : dates){
            Date endDate = null;
            try {
                endDate = df.parse(date);
            } catch (ParseException e) {
               logger.error("Failed to parse date: '" + date + "'", e);
               continue;
            }
            if(startDate == null){
                startDate = endDate;
//                continue;
            }
//            TimePeriod period = new SimpleTimePeriod(startDate, endDate);
            Day period = new Day(endDate);
//            Hour period = new Hour(endDate);
            double val = random.nextDouble();
            dataset.add(period, val, series1);
            startDate = endDate;
        }
        
        try {
            
            
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "" //"Test chart title"
                  , "Time axis label"
                  , "Value axis label"
                  , dataset
                  , true
                  , true
                  , true);
            
            chart.setBackgroundImageAlpha(0f);

            DateAxis domainAxis = (DateAxis)chart.getXYPlot().getDomainAxis();
            domainAxis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yy"));
            
            applyTheme(chart);
            
            chart.removeLegend();
//            chart.getXYPlot().getRangeAxis().setMinorTickCount(10);
//            chart.getXYPlot().getDomainAxis().setMinorTickCount(dates.length);
            
            BufferedImage img = chart.createBufferedImage(1000, 250);
            File outFile = new File("/tmp/chart_" + System.currentTimeMillis() + ".png");
            ImageIO.write(img, "PNG", outFile);
            logger.info("Chart ready: '" + outFile.getAbsolutePath() + "'.");
        } catch (IOException e) {
            logger.error("Failure!", e);
        } 
        
    }
    
    
    public void applyTheme (JFreeChart chart) {
        
        StandardChartTheme theme = (StandardChartTheme)StandardChartTheme.createJFreeTheme();

        theme.setTitlePaint( Color.decode( "#333333" ) );
//        theme.setExtraLargeFont( new Font(fontName,Font.PLAIN, 16) ); //title
//        theme.setLargeFont( new Font(fontName,Font.BOLD, 15)); //axis-title
//        theme.setRegularFont( new Font(fontName,Font.PLAIN, 11));
        theme.setRangeGridlinePaint( Color.decode("#C0C0C0"));
        theme.setDomainGridlinePaint( Color.decode("#C0C0C0"));
        theme.setPlotBackgroundPaint( Color.white  );
        Color backColor = new Color(192, 192, 192);
        
        theme.setChartBackgroundPaint(backColor);
//        theme.setGridBandPaint( Color.red );
        theme.setAxisOffset( new RectangleInsets(0,0,0,0) );
//        theme.setBarPainter(new StandardBarPainter());
        theme.setAxisLabelPaint( Color.decode("#333333")  );
        theme.apply( chart );
        
        chart.getXYPlot().setOutlineVisible( false );
        
        chart.getXYPlot().getRangeAxis().setAxisLineVisible( true );
        chart.getXYPlot().getRangeAxis().setTickMarksVisible( true );
        chart.getXYPlot().setRangeGridlineStroke( new BasicStroke() );
        chart.getXYPlot().setRangeZeroBaselinePaint(Color.black);
        chart.getXYPlot().getRangeAxis().setTickLabelPaint( Color.decode("#333333") );
        
        chart.getXYPlot().setDomainGridlineStroke(new BasicStroke());
        chart.getXYPlot().getDomainAxis().setTickLabelPaint( Color.decode("#333333") );
//        chart.getXYPlot().setDomainZeroBaselinePaint(Color.black);
        
        chart.getXYPlot().setDomainZeroBaselineVisible(true);
        chart.getXYPlot().setDomainZeroBaselinePaint(Color.red);
        chart.getXYPlot().setDomainZeroBaselineStroke(new BasicStroke(3));
        
        
        chart.setTextAntiAlias( true );
        chart.setAntiAlias( true );
        
        chart.getXYPlot().getRenderer().setSeriesPaint( 0, Color.decode( "#000099" ));
//        Shape mark = ShapeUtilities.createDiagonalCross(15f, 15f);
//        Shape mark = new Ellipse2D.Double(0d, 0d, 15d, 15d); 
        Shape mark = new Rectangle(5, 5); 
        chart.getXYPlot().getRenderer().setSeriesShape(0, mark);
        
        
    }
}
