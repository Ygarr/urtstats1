package urt.stats.core.xmlimport.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Import map info container
 * @author ghost
 *
 */
@XmlRootElement(name="urt_map_import")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMapList {

    /**
     * Map list
     */
    @XmlElementWrapper(name="maps")
    @XmlElement(name="map")
    private List<XMap> maps;

    /**
     * Returns map list
     * @return {@link List} of {@link XMap} objects
     */
    public List<XMap> getMaps() {
        return maps;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maps == null) ? 0 : maps.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XMapList other = (XMapList) obj;
        if (maps == null) {
            if (other.maps != null)
                return false;
        } else if (!maps.equals(other.maps))
            return false;
        return true;
    }

    public String toString() {
        return "XMapImport [mapList=" + maps + "]";
    }
    
}
