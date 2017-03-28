package urt.stats.core;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import urt.stats.core.exception.EmptyLogFileException;
import urt.stats.core.exception.ImportFailedException;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.exception.LogParseException;
import urt.stats.core.exception.NoSuchObjectException;
import urt.stats.core.service.UrTPlayerManager;
import urt.stats.core.service.UrTStatsImportManager;
import urt.stats.core.util.StringUtil;
import urt.stats.core.util.TimingAnalyzer;
import urt.stats.core.util.UrTLogParser;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.xmlimport.service.XMapImportManager;

/**
 * Main class
 * @author ghost
 *
 */
public class UrTStatsAdmin {

    /**
     * Import action name
     */
    public static final String ACTION_IMPORT = "import";
    
    /**
     * Batch import action name
     */
    public static final String ACTION_BATCH_IMPORT = "batchimport";
    
    /**
     * Change player name action name
     */
    public static final String ACTION_CHANGE_NAME = "changename";
    
    /**
     * Delete player action name
     */
    public static final String ACTION_DELETE_PLAYER = "delplayer";
    
    /**
     * Import map info action name
     */
    public static final String ACTION_IMPORT_MAPS = "importmaps";
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());
    
    /**
     * Stats import manager
     */
    @Autowired
    private UrTStatsImportManager statsImportManager;
    
    /**
     * Player manager
     */
    @Autowired
    private UrTPlayerManager playerManager;
    
    /**
     * Map import manager
     */
    @Autowired
    private XMapImportManager mapImportManager;
    
    /**
     * Main method
     * 
     * @param args arguments
     */
    public static void main(String[] args) {
        UrTStatsAdmin statsAdmin = new UrTStatsAdmin();
        statsAdmin.launch(args);
    }
    
    /**
     * Initialization
     */
    public UrTStatsAdmin(){
        ApplicationContext context = new ClassPathXmlApplicationContext("/core-context.xml");
        statsImportManager = (UrTStatsImportManager)context.getBean("statsImportManager");
        playerManager = (UrTPlayerManager)context.getBean("playerManager");
        mapImportManager = (XMapImportManager)context.getBean("xmapImportManager");
    }
    
    /**
     * Launch stats admin
     * 
     * @param args {@link String}[] arguments
     */
    public void launch(String[] args){
        String action = "";
        if(args != null && args.length >= 1){
            action = args[0];
            if(!ACTION_IMPORT.equalsIgnoreCase(action)
                    && !ACTION_DELETE_PLAYER.equalsIgnoreCase(action)
                    && !ACTION_CHANGE_NAME.equalsIgnoreCase(action)
                    && !ACTION_BATCH_IMPORT.equalsIgnoreCase(action)
                    && !ACTION_IMPORT_MAPS.equalsIgnoreCase(action)){
                logger.error("Unsupported action: '" + action + "'");
                printUsage();
                System.exit(-1);
            }
        } else {
            printUsage();
            System.exit(0);
        }
        
        if(ACTION_IMPORT.equalsIgnoreCase(action)){
            if(args.length >= 2){
                String logFilePath = args[1];
                processImportAction(logFilePath);
            } else {
                logger.error("Action " + ACTION_IMPORT + ": log file path required.");
                printUsage();
                System.exit(-1);
            }
        } else if(ACTION_BATCH_IMPORT.equalsIgnoreCase(action)){
            if(args.length >= 2){
                String logDirPath = args[1];
                processBatchImportAction(logDirPath);
            } else {
                logger.error("Action " + ACTION_BATCH_IMPORT + ": log dir path required.");
                printUsage();
                System.exit(-1);
            }
        } else if(ACTION_CHANGE_NAME.equalsIgnoreCase(action)){
            if(args.length >= 3){
                String playerIdStr = args[1];
                long playerId = -1;
                try{
                    playerId = Long.parseLong(playerIdStr);
                } catch (NumberFormatException e){
                    logger.error("Invalid player ID: '" + playerIdStr + "'", e);
                    System.exit(-1);
                }
                String playerAlias = args[2];
                if(!StringUtil.hasText(playerAlias)){
                    logger.error("Missing new player alias arg.");
                    System.exit(-1);
                }
                playerAlias = playerAlias.replace(" ", "");
                processChangeNameAction(playerId, playerAlias);
            } else {
                logger.error("Action " + ACTION_CHANGE_NAME + ": log file path required.");
                printUsage();
                System.exit(-1);
            }
        } else if(ACTION_DELETE_PLAYER.equalsIgnoreCase(action)){
            if(args.length >= 2){
                String playerIdStr = args[1];
                try{
                    Long playerId = Long.valueOf(playerIdStr);
                    if(playerId.longValue() < 0){
                        logger.error("Invalid player ID: '" + playerIdStr + "'");
                        System.exit(-1);
                    }
                    processDeletePlayerAction(playerId.longValue());
                } catch(NumberFormatException e){
                    logger.error("Invalid player ID: '" + playerIdStr + "'");
                    System.exit(-1);
                }
            } else {
                logger.error("Action " + ACTION_DELETE_PLAYER + ": player ID required.");
                printUsage();
                System.exit(-1);
            }
        } else if(ACTION_IMPORT_MAPS.equalsIgnoreCase(action)){
            if(args.length >= 2){
                String xmlFiePath = args[1];
                processMapImportAction(xmlFiePath);
            } else {
                logger.error("Action " + ACTION_IMPORT_MAPS + ": XML file path required.");
                printUsage();
                System.exit(-1);
            }
        } 
    }
    
    /**
     * Processes import from the log file action
     * 
     * @param logFilePath {@link String} full path to the log file
     */
    public void processImportAction(String logFilePath){
        File file = new File(logFilePath);
        if(!file.exists()){
            logger.error("File not found: '" + logFilePath + "'");
            System.exit(-1);
        }
        if(file.isDirectory()){
            logger.error("File expected, but got directory instead: '" + logFilePath + "'");
            System.exit(-1);
        }
        UrTLogParser parser = new UrTLogParser(file);
        
        try {
            UrTMatchStats matchStats = parser.parseLog();
            statsImportManager.importMatchStats(matchStats);
        } catch (EmptyLogFileException e){
            logger.error("Stats import action failed: empty or invalid log file.", e);
            System.exit(-1);
        } catch (LogParseException e){
            logger.error("Stats import action failed.", e);
            System.exit(-1);
        } catch (ImportFailedException e) {
            logger.error("Stats import action failed.", e);
            System.exit(-1);
        }
    }
    
    /**
     * Processes batch import from the log file action
     * 
     * @param logDirPath {@link String} full path to the directory with log files
     */
    public void processBatchImportAction(String logDirPath){
        File dir = new File(logDirPath);
        if(!dir.exists()){
            logger.error("Dir not found: '" + logDirPath + "'");
            System.exit(-1);
        }
        if(!dir.isDirectory()){
            logger.error("Directory expected, but got file instead: '" + logDirPath + "'");
            System.exit(-1);
        }
        File[] files = dir.listFiles();
        if(files == null || files.length == 0){
            logger.error("Directory contains no files: '" + logDirPath + "'");
            System.exit(-1);
        }
        Arrays.sort(files);
        int currentIdx = 1;
        logger.info("Batch import started.");
        TimingAnalyzer timer = new TimingAnalyzer(logger);
        timer.start();
        for(File file : files){
            logger.info("Processing file " + currentIdx + " / " + files.length);
            if(file.isDirectory()){
                logger.info(file.getAbsoluteFile() + " is a directory, skipping...");
                continue;
            }
            UrTLogParser parser = new UrTLogParser(file);
            try {
                UrTMatchStats matchStats = parser.parseLog();
                statsImportManager.importMatchStats(matchStats);
            } catch (EmptyLogFileException e){
                logger.error("Stats import action failed: empty or invalid log file. Ignoring.");
            } catch (LogParseException e){
                logger.error("Stats import action failed", e);
                System.exit(-1);
            } catch (ImportFailedException e) {
                logger.error("Stats import action failed", e);
                System.exit(-1);
            }
            
            currentIdx++;
        }
        StringBuffer sb = new StringBuffer("Batch import finished. ");
        timer.stop(sb, Priority.INFO);
    }
    
    /**
     * Processes add player alias
     * 
     * @param playerId long - player ID
     * @param playerAlias - new player alias
     */
    public void processChangeNameAction(long playerId, String playerAlias){
        TimingAnalyzer timer = new TimingAnalyzer(logger);
        timer.start();
        logger.info("Map import started");
        try{
            playerManager.createPlayerAlias(playerId, playerAlias);
            logger.info("Action processed: '" + playerAlias + "' for player with ID: " + playerId);
        } catch(NoSuchObjectException e) {
            logger.error("There is no player with id " + playerId, e);
            System.exit(-1);
        } catch(JdbcException e){
            logger.error("Action failed.", e);
            System.exit(-1);
        }
        StringBuffer sb = new StringBuffer("Map import finished. ");
        timer.stop(sb, Priority.INFO);
    }
    
    
    /**
     * Processes delete player action
     * 
     * @param playerId long - player ID
     */
    public void processDeletePlayerAction(long playerId){
        try{
            playerManager.deletePlayer(playerId);
        } catch(NoSuchObjectException e) {
            logger.error("Delete player action failed: there is no player with id " + playerId, e);
            System.exit(-1);
        } catch(JdbcException e){
            logger.error("Delete player action failed.", e);
            System.exit(-1);
        }
    }
    
    /**
     * Processes import map info action
     * 
     * @param xmlFilePath {@link String} XML file path
     */
    public void processMapImportAction(String xmlFilePath){
        try{
            File file = new File(xmlFilePath);
            if(!file.exists()){
                logger.error("File not found: '" + xmlFilePath + "'");
                System.exit(-1);
            }
            mapImportManager.processXMLFile(file);
        } catch(Exception e) {
            logger.error("Map infor import action failed.", e);
            System.exit(-1);
        }
    }
    
    /**
     * Prints class usage
     */
    public void printUsage(){
        StringBuilder usage = new StringBuilder();
        usage.append("Urban Terror statisitics administration.\n Usage:\n");
        usage.append("urt.stats.core.UrTStatsAdmin ACTION ARGUMENT");
        usage.append("\n\n");
        usage.append("Actions:\n");

        usage.append("  " + ACTION_IMPORT + " - import stats from the specified log file\n");
        usage.append("           1 argument: full path to the log file.\n");
        
        usage.append("  " + ACTION_BATCH_IMPORT + " - import stats from the specified directory\n");
        usage.append("           1 argument: full path to the directory with log files.\n");
        
        usage.append("  " + ACTION_CHANGE_NAME + " - add player alias.\n");
        usage.append("           2 arguments: 1: player ID, 2: new player alias.\n");
        
        usage.append("  " + ACTION_DELETE_PLAYER + " - delete player with the specified ID\n");
        usage.append("           1 argument: player ID.\n");
        
        usage.append("  " + ACTION_IMPORT_MAPS + " - import map info from XML file\n");
        usage.append("           1 argument: path to XML file.\n");

        logger.info(usage.toString());
    }
}
