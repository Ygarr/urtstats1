package urt.stats.core.vo;

import urt.stats.core.enums.UrTWeapon;

/**
 * Player weapon stats
 * @author ghost
 *
 */
public class UrTPlayerWeaponStats {

    /**
     * Weapon
     */
    private UrTWeapon weapon;
    
    /**
     * Kill count with weapon
     */
    private int kills = 0;

    
    /**
     * Parameterized constructor
     * @param weapon {@link UrTWeapon} weapon
     */
    public UrTPlayerWeaponStats(UrTWeapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Parameterized constructor
     * @param weapon {@link UrTWeapon} weapon
     * @param kills int - kill count
     */
    public UrTPlayerWeaponStats(UrTWeapon weapon, int kills) {
        this.weapon = weapon;
        this.kills = kills;
    }

    /**
     * Returns weapon
     * @return {@link UrTWeapon} weapon
     */
    public UrTWeapon getWeapon() {
        return weapon;
    }

    /**
     * Sets weapon
     * @param weapon {@link UrTWeapon} weapon
     */
    public void setWeapon(UrTWeapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Returns kill count
     * @return int - kill count
     */
    public int getKills() {
        return kills;
    }

    /**
     * Sets kill count
     * @param kills int - kill count
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * Increments kill count
     */
    public void incrementKills() {
        this.kills ++;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + kills;
        result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayerWeaponStats other = (UrTPlayerWeaponStats) obj;
        if (kills != other.kills)
            return false;
        if (weapon != other.weapon)
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayerWeaponStats [weapon=" + weapon + ", kills=" + kills
                + "]";
    }
    
}
