package urt.stats.core.enums;

/**
 * Game types (order corresponds to UrT constants)
 * 
 *  0=FreeForAll, 3=TeamDeathMatch, 4=Team Survivor, 5=Follow the Leader, 6=Capture and Hold, 7=Capture The Flag, 8=Bombmode.
 * 
 * @author ghost
 *
 */
public enum UrTGameType {

    /**
     * 0 - FreeForAll
     */
    FFA {
        
        public boolean isTeamBased(){
            return false;
        }
    }

    /**
     * 1 - Last man standing
     */
   , LMS {
       
       public boolean isTeamBased(){
           return false;
       }
   }

    /**
     * 2 - JUMP?
     */
   , JUMP {
       
       public boolean isTeamBased(){
           return false;
       }
   }

    /**
     * 3 - TeamDeathMatch
     */
   , TDM

    /**
     * 4 - Team Survivor
     */
   , TS

    /**
     * 5 - Follow the Leader (teams)
     */
   , FTL

    /**
     * 6 - Capture and Hold  
     */
   , CAH
   
    /**
     * 7 - Capture The Flag
     */
   , CTF

    /**
     * 8 - Bombmode
     */
  , BOMB

  
  ;
    
    /**
     * Is game type team based
     * 
     * @return boolean <code>true</code> - team based, or <code>false</code> otherwise
     */
    public boolean isTeamBased(){
        return true;
    }
}
