package urt.stats.core.enums;

import urt.stats.core.util.StringUtil;

/**
 * Weapon constants
 * 
 * @author ghost
 *
 */
public enum UrTWeapon {

      /**
       * Change team
       */
      CHANGE_TEAM {
          public UrTWeaponType getWeaponType() {
              return UrTWeaponType.FAKE;
          }
      }
    
      /**
       * "Lemming thing"
       */
    , FALLING {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.FAKE;
        }
    }
      
    /**
     * "Drowned"
     */
    , DROWNED {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.FAKE;
        }
    }
    
    
      /**
       * Self-explode
       */
    , SPLODED {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.FAKE;
        }
    }
    
    /**
     * "Bleed to death"
     */
    , BLEED {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * HE-gren
     */
    , HE_GRENADE {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * Kick
     */
    , KICK {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * Knife
     */
    , KNIFE {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * Knife thrown
     */
    , KNIFE_THROWN {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * "Stomped" - kicked from high
     */
    , STOMPED {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * "Stomped" - jump smashed
     */
    , TELEFRAG {
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.MISC;
        }
    }
    
    /**
     * H&K G36
     */
    , G36{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * AK 103
     */
    , AK103{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * LR300
     */
    , LR300{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * HK69
     */
    , HK69{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * HK69 grenade hit
     */
    , HK69_HIT{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * PSG1
     */
    , PSG1{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * SR8
     */
    , SR8{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * NEGEV
     */
    , NEGEV{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
    /**
     * M4
     */
    , M4{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PRIMARY;
        }
    }
    
// SECONDARY
    /**
     * SPAS shotgun
     */
    , SPAS{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.SECONDARY;
        }
    }
    
    /**
     * MP5
     */
    , MP5{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.SECONDARY;
        }
    }
    
    /**
     * UMP45
     */
    , UMP45{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.SECONDARY;
        }
    }
    
    /**
     * MAC11
     */
    , MAC11{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.SECONDARY;
        }
    }

// PISTOLS
    /**
     * Beretta
     */
    , BERETTA{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PISTOL;
        }
    }
    
    /**
     * Desert eagle 
     */
    , DEAGLE{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PISTOL;
        }
    }
    
    /**
     * Glock
     */
    , GLOCK{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PISTOL;
        }
    }
    
    /**
     * Colt 1911
     */
    , COLT{
        public UrTWeaponType getWeaponType() {
            return UrTWeaponType.PISTOL;
        }
    }
    ;
    
    
    /**
     * Returns weapon type
     * @return {@link UrTWeaponType} weapon type
     */
    public UrTWeaponType getWeaponType() {
        return null;
    }
    
    
    /**
     * Converts weapon from string
     * @param weapon {@link String} weapon as String
     * 
     * @return {@link UrTWeapon} value
     */
    public static UrTWeapon convertFromString(String weapon){
        UrTWeapon result = null;
        if(StringUtil.hasText(weapon)){
            if(weapon.endsWith(STR_CHANGE_TEAM)) {
                result = CHANGE_TEAM;
            } else if(weapon.endsWith(STR_FALLING)){
                result = FALLING;
            } else if(weapon.endsWith(STR_DROWNED)){
                result = DROWNED;
            } else if(weapon.endsWith(STR_BLEED)){
                result = BLEED;
            } else if(weapon.endsWith(STR_SPLODED)){
                result = SPLODED;
            } else if(weapon.endsWith(STR_HE_GRENADE)){
                result = HE_GRENADE;
            } else if(weapon.endsWith(STR_KICK)){
                result = KICK;
            } else if(weapon.endsWith(STR_KNIFE_THROWN)){
                result = KNIFE_THROWN;
            } else if(weapon.endsWith(STR_KNIFE)){
                result = KNIFE;
            } else if(weapon.endsWith(STR_STOMPED)){
                result = STOMPED;
            } else if(weapon.endsWith(STR_TELEFRAG)){
                result = TELEFRAG;
            } else if(weapon.endsWith(STR_G36)){
                result = G36;
            } else if(weapon.endsWith(STR_AK103)){
                result = AK103;
            } else if(weapon.endsWith(STR_LR300)){
                result = LR300;
            } else if(weapon.endsWith(STR_HK69_HIT)){
                result = HK69_HIT;
            } else if(weapon.endsWith(STR_HK69)){
                result = HK69;
            } else if(weapon.endsWith(STR_PSG1)){
                result = PSG1;
            } else if(weapon.endsWith(STR_SR8)){
                result = SR8;
            } else if(weapon.endsWith(STR_NEGEV)){
                result = NEGEV;
            } else if(weapon.endsWith(STR_M4)){
                result = M4;
            } else if(weapon.endsWith(STR_SPAS)){
                result = SPAS;
            } else if(weapon.endsWith(STR_MP5)){
                result = MP5;
            } else if(weapon.endsWith(STR_UMP45)){
                result = UMP45;
            } else if(weapon.endsWith(STR_MAC11)){
                result = MAC11;
            } else if(weapon.endsWith(STR_BERETTA)){
                result = BERETTA;
            } else if(weapon.endsWith(STR_DEAGLE)){
                result = DEAGLE;
            } else if(weapon.endsWith(STR_GLOCK)){
                result = GLOCK;
            } else if(weapon.endsWith(STR_COLT)){
                result = COLT;
            }
        }
        
        return result;
    }
    
    /**
     * Change team fake weapon constant
     */
    public static final String STR_CHANGE_TEAM = "MOD_CHANGE_TEAM";
    
    /**
     * "Lemming thing" fake weapon constant
     */
    public static final String STR_FALLING = "MOD_FALLING";
    
    /**
     * "Drawned" fake weapon constant
     */
    public static final String STR_DROWNED = "MOD_WATER";
    
    /**
     * "Bleed to death" fake weapon constant
     */
    public static final String STR_BLEED = "UT_MOD_BLED";
    
    /**
     * "Self-explode" fake weapon constant
     */
    public static final String STR_SPLODED = "UT_MOD_SPLODED";
    
    /**
     * Telefrag
     */
    public static final String STR_TELEFRAG = "MOD_TELEFRAG";
    
    /**
     * HE-grenade weapon constant
     */
    public static final String STR_HE_GRENADE = "UT_MOD_HEGRENADE";
    
    /**
     * Kick weapon constant
     */
    public static final String STR_KICK = "UT_MOD_KICKED";

    /**
     * Knife weapon constant
     */
    public static final String STR_KNIFE = "UT_MOD_KNIFE";
    
    /**
     * Knife-thrown weapon constant
     */
    public static final String STR_KNIFE_THROWN = "UT_MOD_KNIFE_THROWN";
    
    /**
     * Kicked from high
     */
    public static final String STR_STOMPED = "UT_MOD_GOOMBA";

// PRIMARY
    /**
     * H&K G36
     */
    public static final String STR_G36      = "UT_MOD_G36";
    
    /**
     * AK 103
     */
    public static final String STR_AK103    = "UT_MOD_AK103";
    
    /**
     * LR300
     */
    public static final String STR_LR300    = "UT_MOD_LR300";
    
    /**
     * HK69
     */
    public static final String STR_HK69     = "UT_MOD_HK69";
    
    /**
     * HK69 grenade hit
     */
    public static final String STR_HK69_HIT = "UT_MOD_HK69_HIT";
    
    /**
     * PSG1
     */
    public static final String STR_PSG1     = "UT_MOD_PSG1";
    
    /**
     * SR8
     */
    public static final String STR_SR8      = "UT_MOD_SR8";
    
    /**
     * NEGEV
     */
    public static final String STR_NEGEV    = "UT_MOD_NEGEV";
    
    /**
     * M4
     */
    public static final String STR_M4       = "UT_MOD_M4";
    
// SECONDARY
    /**
     * SPAS shotgun
     */
    public static final String STR_SPAS     = "UT_MOD_SPAS";
    
    /**
     * MP5
     */
    public static final String STR_MP5      = "UT_MOD_MP5K";
    
    /**
     * UMP45
     */
    public static final String STR_UMP45    = "UT_MOD_UMP45";
    
    /**
     * MAC11
     */
    public static final String STR_MAC11    = "UT_MOD_MAC11";

// PISTOLS
    /**
     * Beretta
     */
    public static final String STR_BERETTA  = "UT_MOD_BERETTA";
    
    /**
     * Desert eagle 
     */
    public static final String STR_DEAGLE   = "UT_MOD_DEAGLE";
    
    /**
     * Glock
     */
    public static final String STR_GLOCK    = "UT_MOD_GLOCK";
    
    /**
     * Colt 1911
     */
    public static final String STR_COLT     = "UT_MOD_COLT1911";
    
    
    // UT_MOD_SMITED = '33'  ???????????????????????????

    
}
