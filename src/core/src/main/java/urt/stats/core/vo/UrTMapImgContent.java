package urt.stats.core.vo;

import java.util.Arrays;
import java.util.Date;

/**
 * UrT map image content
 * 
 * @author ghost
 *
 */
public class UrTMapImgContent extends UrTMapImg {

    /**
     * Image content
     */
    private byte[] content = null;

    /**
     * Parameterized constructor
     * 
     * @param id {@link Long} image ID
     * @param mapId {@link Long} map ID
     * @param lastModified {@link Date} image last modified date
     * @param content byte[] - image content
     */
    public UrTMapImgContent(
            Long id
          , Long mapId
          , Date lastModified
          , byte[] content) {
        super(id, mapId, lastModified);
        this.content = content;
    }
    
    /**
     * Default constructor
     * 
     */
    public UrTMapImgContent() {
        super();
    }

    
    /**
     * Returns image content
     * 
     * @return byte[] image content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets image content
     * 
     * @param content byte[] image content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }


    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(content);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UrTMapImgContent other = (UrTMapImgContent) obj;
        if (!Arrays.equals(content, other.content))
            return false;
        return true;
    }

    public String toString() {
        return "UrTMapImgContent [toString()=" + super.toString() + "]";
    }
    
}
