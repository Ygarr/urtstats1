package urt.stats.core.xmlimport.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import urt.stats.core.vo.UrTMap;
import urt.stats.core.xmlimport.exception.XImportException;

/**
 * Map info import from XML manager implementation
 * 
 * @author ghost
 *
 */
public interface XMapImportManager {

    /**
     * Processes XML file with map info.
     * 
     * @param file {@link File} XML file
     * 
     * @return {@link List} of {@link UrTMap} objects
     * 
     * @throws FileNotFoundException if there is no such file 
     * @throws XImportException on import failure
     */
    public List<UrTMap> processXMLFile(File file) throws FileNotFoundException, XImportException;

}
