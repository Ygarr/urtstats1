package urt.stats.core.service;

import urt.stats.core.exception.EmptyLogFileException;
import urt.stats.core.exception.ImportFailedException;
import urt.stats.core.vo.UrTMatchStats;

/**
 * Stats import manager interface.
 * Calculates parsed stats and stores to DB
 * 
 * @author ghost
 *
 */
public interface UrTStatsImportManager extends UrTManager {

    /**
     * Stores match stats to DB
     * 
     * @param matchStats {@link UrTMatchStats} match stats
     * 
     * @throws EmptyLogFileException if log file is empty or invalid
     * @throws ImportFailedException on failure
     */
    public void importMatchStats(UrTMatchStats matchStats) throws EmptyLogFileException, ImportFailedException;
    
}
