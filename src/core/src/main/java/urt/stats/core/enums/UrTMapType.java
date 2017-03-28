package urt.stats.core.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Map types (by map size)
 * @author ghost
 *
 */
@XmlType(name="mapType")
@XmlEnum
public enum UrTMapType {

    /**
     * Very small size
     */
    @XmlEnumValue("TINY")
    TINY
  ,
    /**
     * Small size
     */
    @XmlEnumValue("SMALL")
    SMALL
  ,
    /**
     * Medium size
     */
    @XmlEnumValue("MEDIUM")
    MEDIUM
  ,
    /**
     * Large size
     */
    @XmlEnumValue("LARGE")
    LARGE
}
