package urt.stats.web.chart.service;

import java.io.File;

import urt.stats.core.vo.UrTPlayerStats;
import urt.stats.web.chart.exception.ChartException;
import urt.stats.web.chart.vo.UrTChartInfo;

/**
 * Chart Service interface
 * @author ghost
 *
 */
public interface UrTChartService {

    /**
     * Player rate chart name
     */
    public static final String CHART_PLAYER_RATE = "player_rate";
    
    /**
     * Generates player average rate chart over all player matches
     * 
     * @param playerStats {@link UrTPlayerStats} player stats
     * @param outFile {@link File} file for chart storing
     * 
     * @return {@link UrTChartInfo} chart info
     * 
     * @throws ChartException on failure
     */
    public UrTChartInfo generatePlayerRateChart(UrTPlayerStats playerStats, File outFile) throws ChartException;
    
}
