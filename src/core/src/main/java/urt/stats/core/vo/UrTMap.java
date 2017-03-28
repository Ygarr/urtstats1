package urt.stats.core.vo;

import java.util.ArrayList;
import java.util.List;

import urt.stats.core.enums.UrTMapType;
import urt.stats.core.util.StringUtil;

/**
 * UrT Map description
 * @author ghost
 *
 */
public class UrTMap {

    /**
     * File name prefix "ut_"
     */
    protected static final String FILE_NAME_PREFIX_UT = "ut_";
    
    /**
     * File name prefix "ut4_"
     */
    protected static final String FILE_NAME_PREFIX_UT4 = "ut4_";
    
    /**
     * File name prefix "wop_"
     */
    protected static final String FILE_NAME_PREFIX_WOP = "wop_";
    
    /**
     * File name prefixes
     */
    protected static final String[] FILE_NAME_PREFIXES = new String[]{
        FILE_NAME_PREFIX_UT
      , FILE_NAME_PREFIX_UT4
      , FILE_NAME_PREFIX_WOP
    };
    
    /**
     * File name suffix "_bots"
     */
    protected static final String FILE_NAME_SUFFIX_BOTS = "_bots";
    
    
    /**
     * Map unique ID
     */
    private Long id = null;
    
    /**
     * Map name
     */
    private String name = null;
    
    /**
     * Map file name
     */
    private String fileName = null;
    
    /**
     * Map type
     */
    private UrTMapType mapType = null;
    
    /**
     * Has bots flag
     */
    private boolean hasBots = false;

    /**
     * Match count on this map
     */
    private int matchCount = 0;
    
    /**
     * Map images metadata
     */
    private List<UrTMapImg> images = new ArrayList<UrTMapImg>();
    
    /**
     * Parameterized constructor with all fields
     * 
     * @param id {@link Long} map unique ID
     * @param name {@link String} map name
     * @param fileName {@link String} map file name
     * @param mapType {@link UrTMapType} map type
     * @param hasBots boolean - has bots flag
     * @param matchCount int - match count on this map
     */
    public UrTMap(Long id, String name, String fileName, UrTMapType mapType, boolean hasBots, int matchCount) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.mapType = mapType;
        this.hasBots = hasBots;
        this.matchCount = matchCount;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param fileName {@link String} map file name
     * @param mapType {@link UrTMapType} map type
     * @param hasBots boolean - has bots flag
     */
    public UrTMap(String fileName, UrTMapType mapType, boolean hasBots) {
        this.fileName = fileName;
        this.mapType = mapType;
        this.hasBots = hasBots;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param fileName {@link String} map file name
     */
    public UrTMap(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns map unique ID
     * @return {@link Long} map unique ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets map unique ID
     * @param id {@link Long} map unique ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns map name
     * 
     * @return {@link String} map name
     */
    public String getName() {
        if(!StringUtil.hasText(this.name)){
            for(String prefix : FILE_NAME_PREFIXES){
                if(fileName.startsWith(prefix)){
                    name = fileName.substring(prefix.length());
                }
            }
            if(!StringUtil.hasText(name)){
                name = fileName;
            }
            if(fileName.endsWith(FILE_NAME_SUFFIX_BOTS)){
                setHasBots(true);
            }
        }
        return name;
    }

    /**
     * Sets map name
     * @param name {@link String} map name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns map file name
     * 
     * @return {@link String} map file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets map file name
     * 
     * @param fileName {@link String} map file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns map type
     * @return {@link UrTMapType} map type
     */
    public UrTMapType getMapType() {
        return mapType;
    }

    /**
     * Sets map type
     * @param mapType {@link UrTMapType} map type
     */
    public void setMapType(UrTMapType mapType) {
        this.mapType = mapType;
    }

    /**
     * Returns has bots flag
     * @return boolean - has bots flag
     */
    public boolean hasBots() {
        return hasBots;
    }
    
    /**
     * Returns has bots flag
     * @return boolean - has bots flag
     */
    public boolean bots() {
        return hasBots;
    }
    
    /**
     * Returns has bots flag
     * @return boolean - has bots flag
     */
    public boolean getHasBots() {
        return hasBots;
    }

    /**
     * Sets has bots flag
     * @param hasBots boolean - has bots flag
     */
    public void setHasBots(boolean hasBots) {
        this.hasBots = hasBots;
    }

    /**
     * Returns match count on this map
     * @return int - match count on this map
     */
    public int getMatchCount() {
        return matchCount;
    }

    /**
     * Sets match count on this map
     * @param matchCount int match count on this map
     */
    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    /**
     * Returns map images metadata
     * 
     * @return {@link List} of {@link UrTMapImg} objects
     */
    public List<UrTMapImg> getImages() {
        return images;
    }

    /**
     * Sets map images metadata
     * 
     * @param images {@link List} of {@link UrTMapImg} objects
     */
    public void setImages(List<UrTMapImg> images) {
        this.images = images;
    }

    /**
     * Compares changeable attributes
     * 
     * @param other {@link UrTMap} other map
     * 
     * @return boolean <code>true</code> objects differ or <code>false</code> otherwise
     */
    public boolean differsFrom(UrTMap other) {
        if (fileName == null) {
            if (other.fileName != null){
                return true;
            }
        } else if (!fileName.equals(other.fileName)){
            return true;
        }
        if (!hasBots == other.hasBots){
            return true;
        }
        if (mapType == null) {
            if (other.mapType != null){
                return true;
            }
        } else if (!mapType.equals(other.mapType)){
            return true;
        }        
        if (name == null) {
            if (other.name != null){
                return true;
            }
        } else if (!name.equals(other.name)){
            return true;
        }
        return false;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fileName == null) ? 0 : fileName.hashCode());
//        result = prime * result + hasBots.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((mapType == null) ? 0 : mapType.hashCode());
        result = prime * result + matchCount;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTMap other = (UrTMap) obj;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (!hasBots == other.hasBots){
            return false;
        }
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (mapType != other.mapType)
            return false;
        if (matchCount != other.matchCount)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public String toString() {
        return "UrTMap [id=" + id + ", name=" + name + ", fileName=" + fileName
                + ", mapType=" + mapType + ", hasBots=" + hasBots
                + ", matchCount=" + matchCount + "]";
    }
    
}
