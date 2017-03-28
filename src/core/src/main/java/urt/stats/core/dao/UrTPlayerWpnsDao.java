package urt.stats.core.dao;

import java.util.List;
import java.util.Map;

import urt.stats.core.enums.UrTWeapon;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.vo.UrTPlayerWeaponStats;

/**
 * UrT player weapon stats DAO interface
 * @author ghost
 *
 */
public interface UrTPlayerWpnsDao extends UrTDao {

    /**
     * Returns player weapon stats based on weapon type
     * 
     * @param playerId player ID
     * 
     * @return {@link Map} with weapon type as key and list of weapon stats as value
     * 
     * @throws JdbcException on failure
     */
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerWpnStats(long playerId) throws JdbcException;

    /**
     * Returns player match weapon stats based on weapon type
     * 
     * @param playerMatchId player match ID
     * 
     * @return {@link Map} player weapon stats with weapon type as key and list of weapon stats as value
     * 
     * @throws JdbcException on failure
     */
    public Map<String, List<UrTPlayerWeaponStats>> getPlayerMatchWpnStats(long playerMatchId) throws JdbcException;

    /**
     * Stores player weapon stats to DB
     * 
     * @param playerWpns {@link Map} with weapon type as key and list of weapon stats as value
     * @param playerMatchId {@link Long} player match ID
     * 
     * @throws JdbcException on failure
     */
    public void savePlayerWpnStats(Map<UrTWeapon, UrTPlayerWeaponStats> playerWpns, Long playerMatchId) throws JdbcException;
    
    /**
     * Delete all player wpns by player ID
     * 
     * @param playerId long playerId
     * 
     * @throws JdbcException on failure
     */
    public void deleteAllWpnStats(long playerId) throws JdbcException;
}
