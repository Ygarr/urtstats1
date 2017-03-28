package urt.stats.web.chart.theme;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;

/**
 * XY chart theme interface
 * @author ghost
 *
 */
public interface UrTChartTheme {

    /**
     * Applies theme to the given chart
     * 
     * @param chart {@link JFreeChart} chart
     */
    public void applyToChart(JFreeChart chart);
    
    /**
     * Creates range marker with the specified value
     * @param value double - range value
     * @return {@link Marker} marker with the specified value
     */
    public Marker createValueMarker(double value);

}
