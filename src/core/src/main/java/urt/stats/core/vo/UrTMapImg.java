package urt.stats.core.vo;

import java.util.Date;

/**
 * UrT map image
 * 
 * @author ghost
 *
 */
public class UrTMapImg {

    /**
     * Image unique ID
     */
    private Long id = null;
    
    /**
     * Map ID ref
     */
    private Long mapId = null;
    
    /**
     * Image last modified date
     */
    private Date lastModified = null;
    
    /**
     * Default constructor
     */
    public UrTMapImg() {
        
    }
    
    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} image ID
     * @param mapId {@link Long} map ID ref
     * @param lastModified {@link Date} Image last modified date
     */
    public UrTMapImg(Long id, Long mapId, Date lastModified) {
        this.id = id;
        this.mapId = mapId;
        this.lastModified = lastModified;
    }

    /**
     * Returns image ID
     * 
     * @return {@link Long} image ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets image ID
     * @param id {@link Long} image ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns Map ID
     * 
     * @return {@link Long} map ID
     */
    public Long getMapId() {
        return mapId;
    }

    /**
     * Sets map ID
     * @param mapId {@link Long} map ID
     */
    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    /**
     * Returns Image last modified date
     * @return {@link Date} 
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * Sets Image last modified date
     * @param lastModified {@link Date} 
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((lastModified == null) ? 0 : lastModified.hashCode());
        result = prime * result + ((mapId == null) ? 0 : mapId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTMapImg other = (UrTMapImg) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lastModified == null) {
            if (other.lastModified != null)
                return false;
        } else if (!lastModified.equals(other.lastModified))
            return false;
        if (mapId == null) {
            if (other.mapId != null)
                return false;
        } else if (!mapId.equals(other.mapId))
            return false;
        return true;
    }

    public String toString() {
        return "UrTMapImg [id=" + id + ", mapId=" + mapId + ", lastModified="
                + lastModified + "]";
    }
    
}
