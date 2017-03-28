package urt.stats.core.xmlimport.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Import map image info
 * @author ghost
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XMapImg {

    /**
     * Content URL
     */
    @XmlElement(name="contentURL")
    private String contentURL = null;

    /**
     * Returns content URL as string
     * 
     * @return {@link String} content URL
     */
    public String getContentURL() {
        return contentURL;
    }
    
}
