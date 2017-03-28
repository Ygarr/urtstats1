package urt.stats.core.enums;

/**
 * Quantitative Achievements
 * 
 * @author ghost
 *
 */
public enum UrTCountAchvType {

    /**
     * Falling death
     */
    FALL{
        
        public boolean isPositive(){
            return false;
        }
    }
    
    /**
     * Drown death
     */
    , DROWN{
        public boolean isPositive(){
            return false;
        }
    }
    
    /**
     * Self exploded
     */
    , SELF_SPLODE{
        public boolean isPositive(){
            return false;
        }
    }
    
    /**
     * Killed teammate
     */
    , TM_KILL{
        public boolean isPositive(){
            return false;
        }
    }

    /**
     * Bleeded to death
     */
    , BLEED{
        public boolean isPositive(){
            return true;
        }
    }

    /**
     * Killed foe with HE-grenade
     */
    , HE_GRENADE_KILL{
        public boolean isPositive(){
            return true;
        }
    }
    
    /**
     * Killed foe with kick
     */
    , KICK_KILL{
        public boolean isPositive(){
            return true;
        }
    }

    /**
     * Killed foe with knife
     */
    , KNIFE_KILL{
        public boolean isPositive(){
            return true;
        }
    }
    
    /**
     * Killed foe with knife-thrown
     */
    , KNIFE_THROWN_KILL{
        public boolean isPositive(){
            return true;
        }
    }
    
    /**
     * Killed foe by stomp
     */
    , STOMP_KILL{
        public boolean isPositive(){
            return true;
        }
    }
    ;
    
    /**
     * Returns achievement positive sign (<code>true</code> - positive achievement, <code>false</code> - negative) 
     *
     * @return boolean achievement positive sign
     */
    public boolean isPositive(){
        throw new RuntimeException("Unsupported achievement");
    }

    /**
     * Converts weapon from weapon
     * @param weapon {@link UrTWeapon} weapon
     * 
     * @return {@link UrTCountAchvType} value
     */
    public static UrTCountAchvType convertFromWeapon(UrTWeapon weapon){
        UrTCountAchvType result = null;
        if(weapon != null){
            if(UrTWeapon.FALLING.equals(weapon)) {
                result = FALL;
            } else if(weapon.equals(UrTWeapon.DROWNED)){
                result = DROWN;
            } else if(weapon.equals(UrTWeapon.BLEED)){
                result = BLEED;
            } else if(weapon.equals(UrTWeapon.SPLODED)){
                result = SELF_SPLODE;
            } else if(weapon.equals(UrTWeapon.HE_GRENADE)){
                result = HE_GRENADE_KILL;
            } else if(weapon.equals(UrTWeapon.KICK)){
                result = KICK_KILL;
            } else if(weapon.equals(UrTWeapon.KNIFE)){
                result = KNIFE_KILL;
            }  else if(weapon.equals(UrTWeapon.KNIFE_THROWN)){
                result = KNIFE_KILL;
            } else if(weapon.equals(UrTWeapon.STOMPED)){
                result = STOMP_KILL;
            }
        }
        
        return result;
    }
    
}
