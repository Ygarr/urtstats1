package urt.stats.reports.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import urt.stats.reports.enums.UrTReportFormat;
import urt.stats.reports.enums.UrTReportTemplate;
import urt.stats.reports.exception.ReportException;
import urt.stats.reports.service.UrTReportManager;
import urt.stats.reports.vo.UrTReportHolder;

/**
 * Report manager implementation
 * @author ghost
 *
 */
@Component
public class UrTReportManagerImpl implements UrTReportManager {

    /**
     * Имя временной папки для генерации отчетов
     */
    protected static final String TMP_FOLDER_NAME = "report_swap";
    
    /**
     * Max inmemory page count.
     * If page count exceeds this value other pages will be swapped to the {@link #TMP_FOLDER_NAME} folder
     */
    protected static int INMEMORY_PAGE_LIMIT = 5;
    
    /**
     * Report exporters based on report format
     */
    private static Map<UrTReportFormat, Map<JRExporterParameter, Object>> exporterMap;

    /**
     * Временная папка для генерации отчетов с использованием файловой системы.
     */
    private static File tmpFolder = null;
    
    /**
     * Static инициализация
     */
    static{
        exporterMap = new HashMap<UrTReportFormat, Map<JRExporterParameter, Object>>();
        
        Map<JRExporterParameter, Object> excelExporterParams = new HashMap<JRExporterParameter, Object>();
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.FALSE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.FALSE);
        excelExporterParams.put(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporterMap.put(UrTReportFormat.XLS, excelExporterParams);
        
     // creating tmp folder
        String tmpFolderPath = System.getProperty("java.io.tmpdir");
        if(!tmpFolderPath.endsWith(File.separator)){
            tmpFolderPath += File.separator;
        }
        tmpFolderPath += TMP_FOLDER_NAME;
        tmpFolder = new File(tmpFolderPath);
        if(!tmpFolder.exists()){
            tmpFolder.mkdirs();
        }
    }
    
    /**
     * JDBC Data source
     */
    @Autowired
    private DataSource dataSource;
    
    /**
     * Date format
     */
    private DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss_ms");
    
    /**
     * Logger
     */
    private Logger logger = Logger.getLogger(getClass());
    
    
    public UrTReportHolder generateReport(UrTReportTemplate reportTemplate, UrTReportFormat format, Map<String, Object> params) throws ReportException {
        JRExporter exporter = createJRExporter(format, true);
        
        if (exporter == null) {
            throw new ReportException("No exporter defined for format " + format.name());
        }

        String sheetName = "";
        
        Connection conn = null;
        JasperPrint jp = null;
        try {
            conn = dataSource.getConnection();
            JasperReport report = getReport(reportTemplate);
            JRVirtualizer virtualizer = new JRSwapFileVirtualizer(INMEMORY_PAGE_LIMIT, createSwapFile(), true);
            params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
            sheetName = report.getName();
            jp = JasperFillManager.fillReport (report, params, conn);
        } catch (JRException e) {
            throw new ReportException("Jasper exception during report generation", e);
        } catch (ReportException e){
            throw e;
        } catch(Exception e){
            throw new ReportException("Unknown exception", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Failed t oclose connection", e);
                }
            }
        }
        
        if (jp.getPages().size() == 0) {
            return null;
        }

        if(params != null && params.containsKey(REPORT_PARAM_SHEET_TITLE)){
            sheetName = (String)params.get(REPORT_PARAM_SHEET_TITLE);
        }
        
        if(UrTReportFormat.XLS.equals(format)){
            // имя листа в xls файле
            String[] sheetNamesArray = new String[1];
            sheetNamesArray[0] = sheetName;
            exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNamesArray);
        }
        File outFile = createOutFile(reportTemplate, format);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, outFile);
        UrTReportHolder reportHolder = new UrTReportHolder(); 
        try {
            exporter.exportReport();
            reportHolder.setTargetFileExt(format.toString().toLowerCase());
            reportHolder.setTargetFileMimeType(format.getContentType());
            reportHolder.setTargetFileName(outFile.getName());
            if(params != null && params.containsKey(REPORT_PARAM_TARGET_FILE_NAME)){
                reportHolder.setTargetFileDisplayName((String)params.get(REPORT_PARAM_TARGET_FILE_NAME));
            }else{
                reportHolder.setTargetFileDisplayName(outFile.getName());
            }
        } catch (JRException e) {
            throw new ReportException("Jasper exception during report export", e);
        } catch(Exception e){
            throw new ReportException("Unknown exception during report export", e);
        }

        return reportHolder;
    }
    
    /**
     * Returns new instance of {@link JRExporter} for the given report format
     *  
     * @param format - {@link UrTReportFormat} report format
     * @param withDefaultParams - <code>true</code> - add default parameters
     *  
     * @return - {@link JRExporter} - exporter instance
     */
    protected JRExporter createJRExporter(UrTReportFormat format, boolean withDefaultParams){
        JRExporter exporter = null;
        switch(format){
            case XLS :
                exporter = new JExcelApiExporter();
            break;
        }

        if(exporter != null && withDefaultParams){
            Map<JRExporterParameter, Object> defaultParams = exporterMap.get(format);
            if(defaultParams != null && !defaultParams.isEmpty()){
                Map<JRExporterParameter, Object> exporterParams = new HashMap<JRExporterParameter, Object>();
                exporterParams.putAll(defaultParams);
                exporter.setParameters(exporterParams);
            }
        }
        return exporter;
    }
    
    /**
     * Returns compiled report tamplate as {@link InputStream}
     * 
     * @param templatePath - template file path
     * 
     * @return {@link InputStream} - compiled report tamplate
     * 
     * @throws ReportException - if there is no such report template
     */
    private InputStream getReportAsInputStream(String templatePath) throws ReportException {
        InputStream result = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
        
        if(result == null){
            throw new ReportException("Failed to load report template: '" + templatePath + "'");
        }
        
        return result;
    }
    

    /**
     * Returns compiled report template as object of type {@link JasperReport}
     * 
     * @param templatePath - template file path
     * 
     * @return {@link InputStream} - compiled report tamplate as object
     * 
     * @throws ReportException on failure
     */
    private JasperReport getReport(String templatePath) throws ReportException{
        JasperReport jasperReport = null;
        try {
            InputStream is = getReportAsInputStream(templatePath);
            ObjectInputStream ois;
            ois = new ObjectInputStream(is);
            jasperReport = (JasperReport) ois.readObject();
        } catch (IOException e) {
            throw new ReportException("I/O exception during de-serialization", e);
        } catch (ClassNotFoundException e) {
            throw new ReportException("Class not found exception during de-serialization", e);
        } catch (ReportException e){
            throw e;
        }catch(Exception e){
            throw new ReportException("Unknown exception", e);
        }
        return jasperReport;
    }
    
    /**
     * Returns compiled report template as object of type {@link JasperReport}
     * 
     * @param reportTemplate - report template
     * 
     * @return {@link InputStream} - compiled report template as object
     * 
     * @throws ReportException on failure
     */
    private JasperReport getReport(UrTReportTemplate reportTemplate) throws ReportException{
        return getReport(reportTemplate.getTemplatePath());
    }

    /**
     * Returns target folder path for generated reports
     * 
     * @return {@link String} - target folder path for generated reports
     */
    public static String getTargetDirPath(){
        String result = System.getProperty("java.io.tmpdir");
        if ( !result.endsWith(File.separator)){
            result = result + File.separator;
        }
        return result;
    }
    
    /**
     * Creates output file
     * 
     * @param targetFileName {@link String} target file name prefix
     * @param reportFormat {@link UrTReportFormat} report format
     * 
     * @return {@link File} output file
     */
    private File createOutFile(String targetFileName, UrTReportFormat reportFormat){
        StringBuffer fileName = new StringBuffer(targetFileName);
        String randomPart =  new BigInteger(4, new Random()).toString();
        
        fileName.append("_").append(dateFormat.format(new Date()));
        fileName.append("_").append(randomPart);
        fileName.append(".").append(reportFormat.toString().toLowerCase());
        File file = new File(getTargetDirPath() + fileName.toString());
        
        return file;
    }
    
    /**
     * Creates output file
     * 
     * @param reportTemplate - report template
     * @param reportFormat - report format
     * 
     * @return {@link File} - output file
     */
    private File createOutFile(UrTReportTemplate reportTemplate, UrTReportFormat reportFormat){
        return createOutFile(reportTemplate.getTargetFileName(), reportFormat);
    }
    
    /**
     * Creates temporary swap file {@link JasperPrint}.
     * 
     * @return {@link JRSwapFile} - swap file
     * 
     * @throws IOException on failure 
     */
    private JRSwapFile createSwapFile() throws IOException {
        JRSwapFile swapFile = new JRSwapFile(tmpFolder.getAbsolutePath(), 2048, 1024);
        
        return swapFile;
    }
}
