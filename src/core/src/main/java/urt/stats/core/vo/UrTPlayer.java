package urt.stats.core.vo;

/**
 * Player VO
 * 
 * @author ghost
 *
 */
public class UrTPlayer {

    /**
     * Player ID
     */
    private Long id = null;
    
    /**
     * Player name
     */
    private String playerName = null;

    /**
     * Default constructor
     */
    public UrTPlayer(){
        
    }
    
    /**
     * Constructor
     * 
     * @param playerName {@link String} player name
     */
    public UrTPlayer(String playerName){
        this.playerName = playerName;
    }

    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} unique player ID from DB
     * @param playerName {@link String} player name
     */
    public UrTPlayer(
            Long id
          , String playerName) {
        this.id = id;
        this.playerName = playerName;
    }
    
    /**
     * Returns player ID from DB
     * @return {@link Long} player ID from DB
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets player ID from DB
     * 
     * @param id {@link Long} player ID from DB
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns player name
     * 
     * @return {@link String} player name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Sets player name
     * 
     * @param playerName {@link String} player name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTPlayer other = (UrTPlayer) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!playerName.equals(other.playerName))
            return false;
        return true;
    }

    public String toString() {
        return "UrTPlayer [id=" + id + ", playerName=" + playerName + "]";
    }
    
}
