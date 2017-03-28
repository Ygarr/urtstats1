package urt.stats.web.chart.theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

/**
 * XY defaut chart theme
 * 
 * @author ghost
 *
 */
public class UrTXYChartTheme implements UrTChartTheme {
    
    /**
     * Theme
     */
    private StandardChartTheme standardTheme = null;
    
    /**
     * Default constructor
     */
    public UrTXYChartTheme(){
        standardTheme = (StandardChartTheme)StandardChartTheme.createJFreeTheme();
        standardTheme.setTitlePaint( Color.decode( "#333333" ) );
//      standardTheme.setExtraLargeFont( new Font(fontName,Font.PLAIN, 16) ); //title
//      standardTheme.setLargeFont( new Font(fontName,Font.BOLD, 15)); //axis-title
//      standardTheme.setRegularFont( new Font(fontName,Font.PLAIN, 11));
        standardTheme.setRangeGridlinePaint( Color.decode("#C0C0C0"));
        standardTheme.setDomainGridlinePaint( Color.decode("#C0C0C0"));
        standardTheme.setPlotBackgroundPaint( Color.white  );
        Color backColor = new Color(192, 192, 192);
      
        standardTheme.setChartBackgroundPaint(backColor);
//      standardTheme.setGridBandPaint( Color.red );
        standardTheme.setAxisOffset( new RectangleInsets(0,0,0,0) );
//      standardTheme.setBarPainter(new StandardBarPainter());
        standardTheme.setAxisLabelPaint( Color.decode("#333333")  );
    }

    public void applyToChart(JFreeChart chart) {
      standardTheme.apply( chart );
      
      XYPlot plot = chart.getXYPlot();
      
      plot.setOutlineVisible( false );
      plot.setRangeGridlineStroke( new BasicStroke() );
      plot.setRangeZeroBaselinePaint(Color.black);
      plot.setDomainCrosshairVisible(true); 
      plot.setRangeCrosshairVisible(true); 
      
      Font ticklabelFont = new Font(Font.MONOSPACED, Font.PLAIN, 10);
      
      ValueAxis yAxis = plot.getRangeAxis();
      
      yAxis.setAxisLineVisible( true );
      yAxis.setTickMarksVisible( true );
      yAxis.setTickLabelPaint( Color.decode("#333333") );
      yAxis.setTickLabelFont(ticklabelFont);

      plot.setDomainGridlineStroke(new BasicStroke());
      plot.setDomainZeroBaselinePaint(Color.black);
      plot.setDomainZeroBaselineVisible(true);
      plot.setDomainZeroBaselineStroke(new BasicStroke());
      plot.setRangeZeroBaselinePaint(Color.black);
      
      ValueAxis xAxis = plot.getDomainAxis();
      xAxis.setTickLabelPaint( Color.decode("#333333") );
      xAxis.setTickLabelFont(ticklabelFont);
      
      chart.setTextAntiAlias( true );
      chart.setAntiAlias( true );
      
      plot.getRenderer().setSeriesPaint( 0, Color.decode( "#000099" ));
      
      XYItemRenderer rend = plot.getRenderer();
      if(plot.getRenderer() instanceof XYLineAndShapeRenderer){
          XYLineAndShapeRenderer lineAndShapeRenderer = (XYLineAndShapeRenderer)rend; 
          lineAndShapeRenderer.setSeriesShapesVisible(0, true);
      }
      Shape mark = ShapeUtilities.createDiamond(2f);
      plot.getRenderer().setSeriesShape(0, mark);
    }
    
    /**
     * Creates range marker with the specified value
     * @param value double - range value
     * @return {@link Marker} marker with the specified value
     */
    public Marker createValueMarker(double value) {
        Paint markerPaint = Color.decode("#00AA00");
        Stroke markerStroke = new BasicStroke();
        Marker marker = new ValueMarker(value, markerPaint, markerStroke);
        return marker;
    }
}
