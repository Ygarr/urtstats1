package urt.stats.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import urt.stats.core.enums.UrTGameType;
import urt.stats.core.enums.UrTHitTarget;
import urt.stats.core.enums.UrTCountAchvType;
import urt.stats.core.enums.UrTTeam;
import urt.stats.core.enums.UrTWeapon;
import urt.stats.core.enums.UrTWeaponType;
import urt.stats.core.exception.LogParseException;
import urt.stats.core.vo.UrTMap;
import urt.stats.core.vo.UrTMatchStats;
import urt.stats.core.vo.UrTPlayerMatchCountAchv;
import urt.stats.core.vo.UrTPlayerMatchHitStat;
import urt.stats.core.vo.UrTPlayerMatchStats;
import urt.stats.core.vo.UrTPlayerWeaponStats;
import urt.stats.core.vo.UrTTeamMatchStats;

/**
 * Log file parser
 * 
 * @author ghost
 *
 */
public class UrTLogParser {

    /**
     * Space delimiter
     */
    public static final String DELIM_SPACE = " ";
    
    /**
     * Back slash delimiter
     */
    public static final String DELIM_BACK_SLASH = "\\";
    
    /**
     * Colon delimiter
     */
    public static final String DELIM_COLON_SPACE = ": ";
    
    /**
     * Action: init game
     */
    public static final String ACTION_INIT_GAME = "InitGame";
    
    /**
     * Action: user info
     */
    public static final String ACTION_USER_INFO = "ClientUserinfo";
    
    /**
     * Action: change user info
     */
    public static final String ACTION_CHANGE_USER_INFO = "ClientUserinfoChanged";
    
    /**
     * Action: hit
     */
    public static final String ACTION_HIT = "Hit";
    
    /**
     * Action: kill
     */
    public static final String ACTION_KILL = "Kill";
    
    /**
     * Killed word
     */
    public static final String WORD_KILLED = "killed";
    
    /**
     * Hit word
     */
    public static final String WORD_HIT = "hit";
    
    /**
     * Map name String constant
     */
    public static final String STR_MAP_NAME = "mapname\\";
    
    /**
     * Game type String constant
     */
    public static final String STR_GAME_TYPE = "g_gametype\\";
    
    /**
     * N String constant
     */
    public static final String STR_N = "n\\";
    
    /**
     * Name String constant
     */
    public static final String STR_NAME = "\\name\\";
    
    /**
     * Unnamed player String constant
     */
    public static final String STR_UNNAMED_PLAYER = "UnnamedPlayer";
    
    /**
     * Client UID String constant
     */
    public static final String STR_CL_GUID = "\\cl_guid\\";
    
    /**
     * T String constant
     */
    public static final String STR_T = "\\t\\";
    
    /**
     * Non client player
     */
    public static final String NPC_NON_CLIENT = "<non-client>";
    
    /**
     * Non client player
     */
    public static final String NPC_WORLD = "<world>";
    
    /**
     * Log file
     */
    private File logFile = null;
    
    /**
     * Current line number
     */
    private int currentLine = 0;
    
    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger(getClass());
    
    /**
     * Match stats
     */
    private UrTMatchStats matchStats = null;
    
    /**
     * Player stats map by player name
     */
    private Map<String, UrTPlayerMatchStats> playersByName = new HashMap<String, UrTPlayerMatchStats>();
    
    /**
     * Player stats map by player ingame ID
     */
    private Map<Integer, UrTPlayerMatchStats> playersByIngameId = new HashMap<Integer, UrTPlayerMatchStats>();
    
    /**
     * Player stats map by player client UID
     */
    private Map<String, UrTPlayerMatchStats> playersByClientUID = new HashMap<String, UrTPlayerMatchStats>();
    
    /**
     * Constructor
     * 
     * @param logFile log file
     */
    public UrTLogParser(File logFile){
        if(logFile == null){
            throw new IllegalArgumentException("File required!");
        }
        this.logFile = logFile;
    }

    /**
     * Parses log file
     * @return {@link UrTMatchStats} game stats
     * 
     * @throws LogParseException on parse failure
     */
    public UrTMatchStats parseLog() throws LogParseException {
        logger.info("Parsing log file: '" + logFile.getPath() + "' started.");
        TimingAnalyzer timer = new TimingAnalyzer(logger);
        timer.start();
        BufferedReader reader = null;
        long endTimeMillis = logFile.lastModified();
        try{
            matchStats = new UrTMatchStats();
            matchStats.setLogFileName(logFile.getName());
            String line;
            reader = new BufferedReader(new FileReader(logFile));
            String timeStr = "";
            while ((line = reader.readLine()) != null) {
                currentLine++;
                try{
                    if(StringUtil.hasText(line)){
                        StringBuilder sb = new StringBuilder(line.trim());
                        timeStr = getNextWord(sb, DELIM_SPACE);
                        String actionStr = getNextWord(sb, DELIM_COLON_SPACE);
                        if(StringUtil.hasText(actionStr)){
                            if(ACTION_INIT_GAME.equals(actionStr)){
                                if(!parseActionInitGame(sb)){
                                    continue;
                                }
                            } else if(ACTION_USER_INFO.equals(actionStr)){
                                if(!parseActionUserInfo(sb)){
                                    continue;
                                }
                            } else if(ACTION_CHANGE_USER_INFO.equals(actionStr)){
                                if(!parseActionChangeUserInfo(sb)){
                                    continue;
                                }
                            } else if(ACTION_HIT.equals(actionStr)){
                                if(!parseActionHit(sb)){
                                    continue;
                                }
                            } else if(ACTION_KILL.equals(actionStr)){
                                if(!parseActionKill(sb)){
                                    continue;
                                }
                            }
                        }
                    }
                } catch (Throwable e){
                    logger.error("Failed to parse line " + currentLine + ":\n----\n" + line + "\n----\n", e);
                }
            }
            if(StringUtil.hasText(timeStr)){
                logger.debug("Geting time...");
                String[] parts = timeStr.split(":");
                String minStr = parts[0];
                String secStr = parts[1];
                int hour = 0;
                int min = Integer.parseInt(minStr);
                int sec = Integer.parseInt(secStr);
                if(min > 60){
                    hour = min / 60;
                    min = min - hour * 60;
                }
                Calendar endTimeC = Calendar.getInstance();
                endTimeC.setTimeInMillis(endTimeMillis);
                Date endTime = endTimeC.getTime();
                endTimeC.set(Calendar.HOUR_OF_DAY, endTimeC.get(Calendar.HOUR_OF_DAY) - hour);
                endTimeC.set(Calendar.MINUTE, endTimeC.get(Calendar.MINUTE) - min);
                endTimeC.set(Calendar.SECOND, endTimeC.get(Calendar.SECOND) - sec);
                Date startTime = endTimeC.getTime();
                matchStats.setStartTime(startTime);
                matchStats.setEndTime(endTime);
                logger.debug("Done");
            }
            StringBuffer sb = new StringBuffer("Parsing log file finished. ");
            timer.stop(sb, Priority.INFO);
            logger.info("");
        } catch(Exception e){
            logger.error("Failed to parse line: " + currentLine, e);
            throw new LogParseException("Failed to parse log file", e);
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.warn("Failed to close file reader.", e);
                }
            }
        }
        
        return matchStats;
    }
    
    /**
     * Parses "init game" action
     * 
     * @param sb {@link StringBuilder} acion description line from log
     * 
     * @return boolean <code>true</code> - successfull, or <code>false</code> otherwise
     */
    protected boolean parseActionInitGame(StringBuilder sb){
        boolean result = true;
        // 0:00 InitGame: \sv_allowdownload\0\g_matchmode\0\g_gametype\3\sv_maxclients\12\sv_floodprotect\0\capturelimit\0\sv_hostname\ghost\g_followstrict\0\fraglimit\0\timelimit\0\g_swaproles\1\g_roundtime\3\g_bombexplodetime\40\g_bombdefusetime\10\g_hotpotato\2\g_waverespawns\1\g_redwave\5\g_bluewave\5\g_respawndelay\8\g_suddendeath\1\g_maxrounds\0\g_friendlyfire\1\g_allowvote\536871040\g_armbands\1\dmflags\0\sv_minRate\0\sv_maxRate\0\sv_minPing\0\sv_maxPing\0\sv_dlURL\192.168.4.209\g_maxGameClients\0\g_gear\0\g_deadchat\2\g_walljumps\3\g_stamina\0\g_skins\1\g_funstuff\1\version\ioQ3 1.35 urt 4.2.018 linux-i386 Jan 16 2014\protocol\68\mapname\village_bots\sv_privateClients\0\bot_minplayers\0\gamename\q3urt42\g_needpass\0\g_enableDust\0\g_enableBreath\0\g_antilagvis\0\g_survivor\0\g_enablePrecip\0\auth\0\auth_status\off (local)\g_modversion\4.2.018\g_doWarmup\0
        String mapName = "";
        int idx = sb.indexOf(STR_MAP_NAME);
        if(idx != -1){
            int nextDelim = sb.indexOf(DELIM_BACK_SLASH, idx + STR_MAP_NAME.length());
            if(nextDelim != -1){
                mapName = sb.substring(idx + STR_MAP_NAME.length(), nextDelim);
            }
        }
        mapName = mapName.toLowerCase();
        UrTMap map = new UrTMap(mapName);
        matchStats.setMap(map);
        idx = sb.indexOf(STR_GAME_TYPE);
        String gameTypeStr = "";
        if(idx != -1){
            int nextDelim = sb.indexOf(DELIM_BACK_SLASH, idx + STR_GAME_TYPE.length());
            if(nextDelim != -1){
                gameTypeStr = sb.substring(idx + STR_GAME_TYPE.length(), nextDelim);
                int gameTypeNum = Integer.parseInt(gameTypeStr);
                UrTGameType gameType = UrTGameType.values()[gameTypeNum];
                matchStats.setGameType(gameType);
            }
        }
        return result;
    }

    /**
     * Parses "init game" action
     * 
     * @param sb {@link StringBuilder} acion description line from log
     * 
     * @return boolean <code>true</code> - successfull, or <code>false</code> otherwise
     */
    protected boolean parseActionUserInfo(StringBuilder sb){
        boolean result = true;
        // 11:53 ClientUserinfo: 4 \ip\192.168.4.128:27960\challenge\1175641150\qport\745\protocol\68\name\player1\rate\16000\sex\male\handicap\100\color2\5\color1\4\snaps\20\authc\0\cl_guid\A189F16AC4B715A33B68BA8DE7B6BB57
        String innerIdStr = getNextWord(sb, DELIM_SPACE).trim();
        int innerId = Integer.parseInt(innerIdStr);
        String playerName = "";
        int idx = sb.indexOf(STR_NAME);
        int slashIdx = sb.indexOf(DELIM_BACK_SLASH, idx+STR_NAME.length());
        if(slashIdx == -1) {
            slashIdx = sb.length();
        }
        if(idx != -1){
            playerName = sb.substring(idx + STR_NAME.length(), slashIdx);
        } else {
            logger.warn("Failed to parse player name. Line: " + currentLine);
            playerName = STR_UNNAMED_PLAYER;
//            return false;
        }
        idx = sb.indexOf(STR_CL_GUID);
        String playerClientUID = null;
        if(idx != -1){
            int nextDelimIdx = sb.indexOf(DELIM_BACK_SLASH, idx+STR_CL_GUID.length());
            if(nextDelimIdx == -1){
                nextDelimIdx = sb.length();
            }
            playerClientUID = sb.substring(idx + STR_CL_GUID.length(), nextDelimIdx);
        } else {
            logger.warn("Failed to parse player client UID. Line: " + currentLine);
            playerClientUID = playerName;
//            return false;
        }
        
        UrTPlayerMatchStats player = getPlayerByClientUID(playerClientUID, playerName);
        playersByIngameId.put(innerId, player);
        player.setPlayerIngameId(innerId);
        
        return result;
    }
    
    /**
     * Parses "init game" action
     * 
     * @param sb {@link StringBuilder} acion description line from log
     * 
     * @return boolean <code>true</code> - successfull, or <code>false</code> otherwise
     */
    protected boolean parseActionChangeUserInfo(StringBuilder sb){
        boolean result = true;
        // 0:00 ClientUserinfoChanged: 3 n\player1\t\3\r\3\tl\0\f0\diablo\f1\\f2\\a0\255\a1\0\a2\0
        String innerIdStr = getNextWord(sb, DELIM_SPACE).trim();
        int innerId = Integer.parseInt(innerIdStr);
        String playerName = "";
        int idx = sb.indexOf(STR_N);
        if(idx != -1){
            playerName = sb.substring(idx + STR_N.length(), sb.indexOf(DELIM_BACK_SLASH, idx+STR_N.length()));
        } else {
            logger.warn("Failed to parse player name. Line: " + currentLine);
            return false;
        }
        UrTTeam playerTeam = null;
        idx = sb.indexOf(STR_T);
        if(idx != -1){
            String playerTeamStr = "";
            playerTeamStr = sb.substring(idx + STR_T.length(), sb.indexOf(DELIM_BACK_SLASH, idx+STR_T.length()));
            int playerTeamNum = Integer.parseInt(playerTeamStr);
            playerTeam = UrTTeam.values()[playerTeamNum];
        } else {
            logger.warn("Failed to parse player team. Line: " + currentLine);
            return false;
        }
        UrTPlayerMatchStats player = getPlayerByIngameId(innerId);
        if(!playersByName.containsKey(playerName)){
            playersByName.put(playerName, player);
        }
        player.setPlayerName(playerName);
        if(player.getPlayerTeam() == null || !UrTTeam.SPECTATOR.equals(playerTeam)){
            player.setPlayerTeam(playerTeam);
        }
        
        return result;
    }
    
    
    /**
     * Parses "hit" action
     * 
     * @param sb {@link StringBuilder} acion description line from log
     * 
     * @return boolean <code>true</code> - successfull, or <code>false</code> otherwise
     */
    protected boolean parseActionHit(StringBuilder sb){
        boolean result = true;
        // 3:52 Hit: 2 0 6 15: player1 hit player2 in the Right Arm
        // getting numeric constants
        getNextWord(sb, DELIM_COLON_SPACE);
        String attackPlayerName = getNextWord(sb, DELIM_SPACE);
        String hitWord = getNextWord(sb, DELIM_SPACE);
        if(!StringUtil.hasText(hitWord) || !WORD_HIT.equalsIgnoreCase(hitWord.toLowerCase()) ){
            logger.warn("Failed to calc hit. Line: " + currentLine);
            return false;
        }
        String defendPlayerName = getNextWord(sb, DELIM_SPACE);
        UrTHitTarget hitTarget = UrTHitTarget.convertFromString(sb.toString());
        
        UrTPlayerMatchStats attacker = getPlayerByName(attackPlayerName);
        if(attacker == null){
            logger.warn("Attacker is undefined, skipping hit. Line: " + currentLine);
            return false;
        }
        UrTPlayerMatchStats defender = getPlayerByName(defendPlayerName);
        if(matchStats.isTeamBasedMatch()){
            if(attacker.getPlayerTeam() != null 
                    && defender.getPlayerTeam() != null
                    && attacker.getPlayerTeam().equals(defender.getPlayerTeam())){
                // reject teammate hits
                return true;
            }
        }
        
        statHit(attackPlayerName, hitTarget);
        return result;
    }

    /**
     * Parses "hit" action
     * 
     * @param sb {@link StringBuilder} acion description line from log
     * 
     * @return boolean <code>true</code> - successfull, or <code>false</code> otherwise
     */
    protected boolean parseActionKill(StringBuilder sb){
        boolean result = true;
        //   3:52 Kill: 2 3 25: player1 killed player2 by UT_MOD_HEGRENADE
        // getting numeric constants
        getNextWord(sb, DELIM_COLON_SPACE);

        String attackPlayerName = getNextWord(sb, DELIM_SPACE);
        // getting "killed" word
        String killWord = getNextWord(sb, DELIM_SPACE);
        if(!StringUtil.hasText(killWord) || !WORD_KILLED.equalsIgnoreCase(killWord.toLowerCase()) ){
            logger.warn("Failed to calc kill. Line: " + currentLine);
            return false;
        }
        String defendPlayerName = getNextWord(sb, DELIM_SPACE);
        UrTPlayerMatchStats attacker = null;
        if(!NPC_NON_CLIENT.equals(attackPlayerName) 
                && !NPC_WORLD.equals(attackPlayerName)){
            attacker = getPlayerByName(attackPlayerName);
        }
        UrTPlayerMatchStats defender = getPlayerByName(defendPlayerName);
        UrTWeapon weapon = UrTWeapon.convertFromString(sb.toString());
        if(weapon == null){
            logger.warn("Unknown weapon: '" + sb.toString() + "'");
        }
        UrTCountAchvType achvType = null;
        if(weapon != null){
            achvType = UrTCountAchvType.convertFromWeapon(weapon);
        }
        if(NPC_NON_CLIENT.equals(attackPlayerName) || NPC_WORLD.equals(attackPlayerName)){
            // self-kill ("by world")
            statDeath(defendPlayerName);
            revokeKill(defendPlayerName);
            if(achvType != null){
                addPlayerCountAchv(defender, achvType);
            }
            
            // uncomment for personal self-kill counts 
            // defender.addKillByPlayer(defender);
            statTeamDeath(defender.getPlayerTeam());
            revokeTeamKill(defender.getPlayerTeam());
        } else if(attackPlayerName.equals(defendPlayerName)) {
            // self-kill by weapon
            if(weapon != null && UrTWeapon.CHANGE_TEAM.equals(weapon)){
                // ignoring change team
                return false;
            }
            if(achvType != null && UrTCountAchvType.HE_GRENADE_KILL.equals(achvType)){
                achvType = UrTCountAchvType.SELF_SPLODE;
            }
            if(achvType != null){
                addPlayerCountAchv(attacker, achvType);
            }

            statDeath(attackPlayerName);
            revokeKill(attackPlayerName);
            
            // uncomment for personal self-kill counts 
            // attacker.addKillByPlayer(attacker);
            
            statTeamDeath(attacker.getPlayerTeam());
            revokeTeamKill(attacker.getPlayerTeam());
        } else {
            // other player kill
            if(matchStats.isTeamBasedMatch()){
                if(attacker.getPlayerTeam() != null 
                        && defender.getPlayerTeam() != null
                        && attacker.getPlayerTeam().equals(defender.getPlayerTeam())){
                    // team-mate kill
                    revokeKill(attackPlayerName);
                    statDeath(defendPlayerName);
                    addPlayerCountAchv(attacker, UrTCountAchvType.TM_KILL);
                    // uncomment for personal teammate kill counts 
                    // attacker.addKillByPlayer(defender);
                    
                    statTeamDeath(attacker.getPlayerTeam());
                    revokeTeamKill(attacker.getPlayerTeam());
                    return true;
                }
            }
            // other team player kill
            if(achvType != null){
                addPlayerCountAchv(attacker, achvType);
            }
            statKill(attackPlayerName);
            incrementWpnKills(attacker, weapon);
            statDeath(defendPlayerName);
            
            attacker.addKillByPlayer(defender);

            statTeamKill(attacker.getPlayerTeam());
            statTeamDeath(defender.getPlayerTeam());

        }
        return result;
    }
    
    
    
    /**
     * Returns next word from StringBuilder and removes it.
     * @param sb {@link StringBuilder} string builder
     * @param delimiter {@link String} delimiter
     * 
     * @return {@link String} next word
     */
    protected String getNextWord(StringBuilder sb, String delimiter){
        String result =  "";
        int firstDelim = sb.indexOf(delimiter);
        if(firstDelim != -1){
            result = sb.substring(0, firstDelim).trim();
            sb.delete(0,  firstDelim + delimiter.length());
        }
        return result;

    }
    
    /**
     * Returns player stats by player name (or creates a new object if not exists)
     * 
     * @param clientUID {@link String} player client UID
     * @param playerName {@link String} playerName
     * 
     * @return {@link UrTPlayerMatchStats} player stats object
     */
    public UrTPlayerMatchStats getPlayerByClientUID(String clientUID, String playerName){
        UrTPlayerMatchStats result = null;
        if(playersByClientUID.containsKey(clientUID)){
            result = playersByClientUID.get(clientUID);
        }
        
        if(result == null){
            result = new UrTPlayerMatchStats(playerName);
            playersByClientUID.put(clientUID, result);
            addPlayer(result);
        }
        
        return result;
    }
    
    /**
     * Returns player stats by player name
     * 
     * @param playerName {@link String} playerName
     * 
     * @return {@link UrTPlayerMatchStats} player stats object
     */
    public UrTPlayerMatchStats getPlayerByName(String playerName){
        return playersByName.get(playerName);
    }
    
    /**
     * Returns player stats by player ingame ID
     * 
     * @param playerIngameId int player ingame ID
     * 
     * @return {@link UrTPlayerMatchStats} player stats object
     */
    public UrTPlayerMatchStats getPlayerByIngameId(int playerIngameId){
        return playersByIngameId.get(playerIngameId);
    }

    /**
     * Returns match players
     * 
     * @return {@link Map} match players
     */
    public Map<String, UrTPlayerMatchStats> getPlayersByName() {
        return playersByName;
    }

    /**
     * Returns team stats by team type (or creates a new object if not exists)
     * 
     * @param teamType {@link UrTTeam} team type
     * @param createIfNeeded boolean <code>true</code> - create new team if player does not exist
     * 
     * @return {@link UrTTeamMatchStats} team stats object
     */
    public UrTTeamMatchStats getTeam(UrTTeam teamType, boolean createIfNeeded) {
        if(teamType == null){
            logger.warn("Null team type!");
            return null;
        }
        UrTTeamMatchStats result = null;
        if(matchStats.getTeams().containsKey(teamType)){
            result = matchStats.getTeams().get(teamType);
        }
        if(createIfNeeded && result == null){
            result = new UrTTeamMatchStats(teamType);
            matchStats.getTeams().put(teamType, result);
        }
        
        return result;
    }

    
    /**
     * Adds player match stats
     * 
     * @param playerMatchStats {@link UrTPlayerMatchStats} player match stats
     */
    public void addPlayer(UrTPlayerMatchStats playerMatchStats) {
        playersByName.put(playerMatchStats.getPlayerName(), playerMatchStats);
        matchStats.addPlayer(playerMatchStats);
    }

    /**
     * Stats hit
     * 
     * @param playerName {@link String} playerName
     * @param hitTarget {@link UrTHitTarget} hit target
     */
    public void statHit(String playerName, UrTHitTarget hitTarget){
        UrTPlayerMatchStats player = getPlayerByName(playerName);
        addPlayerMatchHit(player, hitTarget);
    }

    /**
     * Stats kill
     * 
     * @param playerName {@link String} playerName
     */
    public void statKill(String playerName){
        UrTPlayerMatchStats player = getPlayerByName(playerName);
        player.incrementKills();
    }
    
    /**
     * Revoke kill from player stats (-1 to kills)
     * 
     * @param playerName {@link String} playerName
     */
    public void revokeKill(String playerName){
        UrTPlayerMatchStats player = getPlayerByName(playerName);
        player.decrementKills();
    }
    
    /**
     * Stats death
     * 
     * @param playerName {@link String} playerName
     */
    public void statDeath(String playerName){
        UrTPlayerMatchStats player = getPlayerByName(playerName);
        player.incrementDeaths();
    }

     /**
     * Stats team kill (for team based game type)
     * 
     * @param teamType {@link UrTTeam} team type
     */
    public void statTeamKill(UrTTeam teamType){
        UrTTeamMatchStats team = getTeam(teamType, true);
        if(team == null){
            return;
        }
        if(matchStats.isTeamBasedMatch()){
            team.incrementKills();
        }
    }
    
    /**
     * Revoke team kill from team stats (-1 to kills)
     * 
     * @param teamType {@link UrTTeam} team type
     */
    public void revokeTeamKill(UrTTeam teamType){
        UrTTeamMatchStats team = getTeam(teamType, true);
        if(team == null){
            return;
        }
        if(matchStats.isTeamBasedMatch()){
            team.decrementKills();
        }
    }
    
    /**
     * Stats death
     * 
     * @param teamType {@link UrTTeam} team type
     */
    public void statTeamDeath(UrTTeam teamType){
        UrTTeamMatchStats team = getTeam(teamType, true);
        if(team == null){
            return;
        }
        if(matchStats.isTeamBasedMatch()){
            team.incrementDeaths();
        }
    }

    /**
     * Adds player hit on target
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param target {@link UrTHitTarget} hit target
     */
    public void addPlayerMatchHit(UrTPlayerMatchStats player, UrTHitTarget target) {
        UrTPlayerMatchHitStat hitStat = getHitTargetStat(player, target);
        hitStat.incrementHitCount();
    }

    /**
     * Adds player count achievement
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param achvType {@link UrTCountAchvType} achievement type
     */
    public void addPlayerCountAchv(UrTPlayerMatchStats player, UrTCountAchvType achvType) {
        UrTPlayerMatchCountAchv achv = getCountAchv(player, achvType);
        achv.incrementAchvCount();
    }
    
    /**
     * Returns existing hit target stat (or creates the new one if it does not exist)
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param hitTarget {@link UrTHitTarget} hit target
     * 
     * @return {@link UrTPlayerMatchHitStat} hit target stat
     */
    protected UrTPlayerMatchHitStat getHitTargetStat(UrTPlayerMatchStats player, UrTHitTarget hitTarget) {
        UrTPlayerMatchHitStat result = null;
        for(UrTPlayerMatchHitStat stat : player.getPlayerMatchHits()){
            if(hitTarget.equals(stat.getTarget())){
                return stat;
            }
        }
        
        if(result == null){
            result = new UrTPlayerMatchHitStat(hitTarget);
            player.getPlayerMatchHits().add(result);
        }
        
        return result;
    }
    
    /**
     * Returns existing achievement (or creates the new one if it does not exist)
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param achvType {@link UrTCountAchvType} q achievement
     * 
     * @return {@link UrTPlayerMatchCountAchv} q achievement
     */
    protected UrTPlayerMatchCountAchv getCountAchv(UrTPlayerMatchStats player, UrTCountAchvType achvType) {
        UrTPlayerMatchCountAchv result = null;
        for(UrTPlayerMatchCountAchv achv : player.getPlayerMatchAchvs()){
            if(achvType.equals(achv.getAchievementType())){
                return achv;
            }
        }
        
        if(result == null){
            result = new UrTPlayerMatchCountAchv(achvType);
            player.getPlayerMatchAchvs().add(result);
        }
        
        return result;
    }


    /**
     * Returns existing weapon stat (or creates the new one if it does not exist)
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param weapon {@link UrTWeapon} weapon
     * 
     * @return {@link UrTPlayerWeaponStats} weapon stats
     */
    protected UrTPlayerWeaponStats getWpnStat(UrTPlayerMatchStats player, UrTWeapon weapon) {
        UrTPlayerWeaponStats result = null;
        
        if(player.getPlayerWpns().containsKey(weapon)){
            result = player.getPlayerWpns().get(weapon);
        }
        if(result == null){
            result = new UrTPlayerWeaponStats(weapon);
            player.getPlayerWpns().put(weapon, result);
        }
        
        return result;
    }
    
    /**
     * Increments weapon kill count for the given player
     * 
     * @param player {@link UrTPlayerMatchStats} player match stats
     * @param weapon {@link UrTWeapon} weapon
     */
    protected void incrementWpnKills(UrTPlayerMatchStats player, UrTWeapon weapon) {
        if(player == null || weapon == null) {
            return;
        }
        if(weapon.getWeaponType() != UrTWeaponType.FAKE){
            UrTPlayerWeaponStats wpnStats = getWpnStat(player, weapon);
            wpnStats.incrementKills();
        }
    }
}

