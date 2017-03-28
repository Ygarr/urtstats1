package urt.stats.core.enums;

import urt.stats.core.util.StringUtil;

/**
 * Hit target ENUM
 * 
 * @author ghost
 *
 */
public enum UrTHitTarget {

    /**
     * Head (or helmet) hit
     */
    HEAD {

        public String getAchievementName() {
            return "Max head hits";
        }
    }
   
    /**
     * Torso (or vest) hit
     */
  , TORSO {

        public String getAchievementName() {
            return "Max torso hits";
        }
    }

   
    /**
     * Butt hit
     */
  , BUTT {

        public String getAchievementName() {
            return "Max butt hits";
        }
    }


    /**
     * Groin hit
     */
  , GROIN {

        public String getAchievementName() {
            return "Max groin hits";
        }
    }


  /**
   * Any of legs (lower or upper)
   */
  , LEG {

        public String getAchievementName() {
            return "Max leg hits";
       }
  }


  /**
   * Any of arms
   */
  , ARM {

        public String getAchievementName() {
            return "Max arm hits";
        }
    }


    ;
    
    /**
     * Returns max hit achievement name (all values should override this method) 
     *
     * @return {@link String} achievement name
     */
    public String getAchievementName(){
        throw new RuntimeException("Undefined achievement name.");
    }
    
    /**
     * Converts hit target from string
     * @param target {@link String} target as String
     * 
     * @return {@link UrTHitTarget} value
     */
    public static UrTHitTarget convertFromString(String target){
        UrTHitTarget result = null;
        if(StringUtil.hasText(target)){
            if(target.endsWith(STR_HEAD) || target.endsWith(STR_HELMET)) {
                result = HEAD;
            } else if(target.endsWith(STR_TORSO) || target.endsWith(STR_VEST)){
                result = TORSO;
            } else if(target.endsWith(STR_BUTT)){
                result = BUTT;
            } else if(target.endsWith(STR_GROIN)){
                result = GROIN;
            } else if(target.endsWith(STR_ARM)){
                result = ARM;
            } else if(target.endsWith(STR_LEG) || target.endsWith(STR_FOOT)){
                result = LEG;
            }
        }
        
        return result;
    }

    /**
     * Head string constant
     */
    public static final String STR_HEAD = "Head";
    
    /**
     * Helmet string constant
     */
    public static final String STR_HELMET = "Helmet";
    
    /**
     * Torso string constant
     */
    public static final String STR_TORSO = "Torso";
    
    /**
     * Vest string constant
     */
    public static final String STR_VEST = "Vest";

    /**
     * Butt string constant
     */
    public static final String STR_BUTT = "Butt";

    /**
     * Groin string constant
     */
    public static final String STR_GROIN = "Groin";
    
    /**
     * Arm string constant
     */
    public static final String STR_ARM = "Arm";

    /**
     * Leg string constant
     */
    public static final String STR_LEG = "Leg";
    
    /**
     * Foot string constant
     */
    public static final String STR_FOOT = "Foot";

}
