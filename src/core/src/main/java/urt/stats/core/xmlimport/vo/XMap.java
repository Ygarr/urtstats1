package urt.stats.core.xmlimport.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import urt.stats.core.enums.UrTMapType;


/**
 * Import map info
 * @author ghost
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMap {

    /**
     * Map file name
     */
    @XmlElement(name="fileName")
    private String fileName = null;
    
    /**
     * Map type
     */
    @XmlElement(name="type")
    private UrTMapType mapType = null;
    
    /**
     * Has bots flag
     */
    @XmlElement(name="hasBots")
    private boolean hasBots = false;

    /**
     * Map images metadata
     */
    @XmlElementWrapper(name="images")
    @XmlElement(name="image")
    private List<XMapImg> images = new ArrayList<XMapImg>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UrTMapType getMapType() {
        return mapType;
    }

    public void setMapType(UrTMapType mapType) {
        this.mapType = mapType;
    }

    public boolean hasBots() {
        return hasBots;
    }

    public void setHasBots(boolean hasBots) {
        this.hasBots = hasBots;
    }

    public List<XMapImg> getImages() {
        return images;
    }

    public void setImages(List<XMapImg> images) {
        this.images = images;
    }
    
}
