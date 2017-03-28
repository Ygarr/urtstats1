-- SEQ urt_player
CREATE SEQUENCE seq_urt_player;

-- SEQ urt_match
CREATE SEQUENCE seq_urt_match;

-- SEQ urt_player_match
CREATE SEQUENCE seq_urt_player_match;

-- SEQ urt_team_match
CREATE SEQUENCE seq_urt_team_match;

-- TABLE urt_weapon_type
CREATE TABLE urt_weapon_type (
  id        integer              PRIMARY KEY
, name      varchar(20)
, descr     varchar(50)
);

COMMENT ON TABLE urt_weapon_type         IS 'UrT weapon type constants.';
COMMENT ON COLUMN urt_weapon_type.id     IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_weapon_type.name   IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_weapon_type.descr  IS 'Constant description.';


INSERT INTO urt_weapon_type (id, name, descr)
VALUES (0, 'PRIMARY', 'Primary weapon');

INSERT INTO urt_weapon_type (id, name, descr)
VALUES (1, 'SECONDARY', 'Secondary weapon');

INSERT INTO urt_weapon_type (id, name, descr)
VALUES (2, 'PISTOL', 'Pistol');

INSERT INTO urt_weapon_type (id, name, descr)
VALUES (3, 'MISC', 'Misc weapon');

INSERT INTO urt_weapon_type (id, name, descr)
VALUES (4, 'FAKE', 'Fake weapon');

COMMIT;

-- TABLE urt_weapon
CREATE TABLE urt_weapon (
  id        integer              PRIMARY KEY
, name      varchar(20)
, descr     varchar(50)
, type_id   integer              REFERENCES urt_weapon_type (id)
);

CREATE INDEX urt_weapon_idx_01 ON urt_weapon (type_id);

COMMENT ON TABLE urt_weapon           IS 'UrT weapon constants.';
COMMENT ON COLUMN urt_weapon.id       IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_weapon.name     IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_weapon.descr    IS 'Constant description.';
COMMENT ON COLUMN urt_weapon.type_id  IS 'Weapon type ID ref';

INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (0, 'CHANGE_TEAM', 'Change team', 4);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (1, 'FALLING', 'Lemming thing', 4);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (2, 'DROWNED', 'Drowned', 4);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (3, 'SPLODED', 'Self-explode', 4);

INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (4, 'BLEED', 'Bleed to death', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (5, 'HE_GRENADE', 'HE-grenade', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (6, 'KICK', 'Kick', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (7, 'KNIFE', 'Knife', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (8, 'KNIFE_THROWN', 'Knife-thrown', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (9, 'STOMPED', 'Stomped', 3);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (10, 'TELEFRAG', 'Telefrag', 3);

INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (11, 'G36', 'G36', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (12, 'AK103', 'AK103', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (13, 'LR300', 'LR300', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (14, 'HK69', 'HK69', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (15, 'HK69_HIT', 'HK69 grenade hit', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (16, 'PSG1', 'PSG1', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (17, 'SR8', 'SR8', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (18, 'NEGEV', 'Negev', 0);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (19, 'M4', 'M4', 0);

INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (20, 'SPAS', 'SPAS shotgun', 1);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (21, 'MP5', 'MP5', 1);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (22, 'UMP45', 'UMP45', 1);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (23, 'MAC11', 'MAC11', 1);

INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (24, 'BERETTA', 'Beretta', 2);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (25, 'DEAGLE', 'Desert eagle', 2);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (26, 'GLOCK', 'Glock', 2);
INSERT INTO urt_weapon (id, name, descr, type_id)
VALUES (27, 'COLT', 'Colt', 2);

COMMIT;


-- TABLE urt_team
CREATE TABLE urt_team (
  id        integer              PRIMARY KEY
, name      varchar(20)
, descr     varchar(50)
);

COMMENT ON TABLE urt_team            IS 'UrT team constants.';
COMMENT ON COLUMN urt_team.id        IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_team.name      IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_team.descr     IS 'Constant description.';

INSERT INTO urt_team (id, name, descr)
VALUES (0, 'FFA', 'Free for all (no teams)');

INSERT INTO urt_team (id, name, descr)
VALUES (1, 'RED', 'Red');

INSERT INTO urt_team (id, name, descr)
VALUES (2, 'BLUE', 'Blue');

INSERT INTO urt_team (id, name, descr)
VALUES (3, 'SPECTATOR', 'Spectator');

INSERT INTO urt_team (id, name, descr)
VALUES (4, 'SKIPPED', 'Skiped');


COMMIT;


-- TABLE urt_hit_target
CREATE TABLE urt_hit_target (
  id        integer              PRIMARY KEY
, name      varchar(20)
, descr     varchar(50)
);

COMMENT ON TABLE urt_hit_target         IS 'UrT hit target constants.';
COMMENT ON COLUMN urt_hit_target.id     IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_hit_target.name   IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_hit_target.descr  IS 'Constant description.';

INSERT INTO urt_hit_target (id, name, descr)
VALUES (0, 'HEAD', 'Head');

INSERT INTO urt_hit_target (id, name, descr)
VALUES (1, 'TORSO', 'Torso');

INSERT INTO urt_hit_target (id, name, descr)
VALUES (2, 'BUTT', 'Butt');

INSERT INTO urt_hit_target (id, name, descr)
VALUES (3, 'GROIN', 'Groin');

INSERT INTO urt_hit_target (id, name, descr)
VALUES (4, 'LEG', 'Leg (any leg, any part)');

INSERT INTO urt_hit_target (id, name, descr)
VALUES (5, 'ARM', 'Arm (any arm)');

COMMIT;

-- TABLE urt_game_type
CREATE TABLE urt_game_type (
  id              integer        PRIMARY KEY
, name            varchar(20)
, descr           varchar(50)
, is_team_based   boolean
);

COMMENT ON TABLE urt_game_type                 IS 'UrT game type constants.';
COMMENT ON COLUMN urt_game_type.id             IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_game_type.name           IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_game_type.descr          IS 'Constant description.';
COMMENT ON COLUMN urt_game_type.is_team_based  IS 'Team based game sign (true - team based).';

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (0, 'FFA', 'Free for all', false);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (1, 'LMS', 'Last man standing', false);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (2, 'JUMP', 'Jump', false);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (3, 'TDM', 'Team deathmatch', true);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (4, 'TS', 'Team survivor', true);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (5, 'FTL', 'Follow the leader', true);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (6, 'CAH', 'Capture and hold', true);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (7, 'CTF', 'Capture the flag', true);

INSERT INTO urt_game_type (id, name, descr, is_team_based)
VALUES (8, 'BOMB', 'Bomb mode', true);

COMMIT;


-- TABLE urt_count_achv
CREATE TABLE urt_count_achv (
  id              integer        PRIMARY KEY
, name            varchar(20)
, descr           varchar(50)
, is_positive     boolean
);

COMMENT ON TABLE urt_count_achv                 IS 'UrT count achievement constants.';
COMMENT ON COLUMN urt_count_achv.id             IS 'Constant ID (Enum ordinal).';
COMMENT ON COLUMN urt_count_achv.name           IS 'Constant name (Enum name).';
COMMENT ON COLUMN urt_count_achv.descr          IS 'Constant description.';
COMMENT ON COLUMN urt_count_achv.is_positive    IS 'Positive or negative achievement.';

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (0, 'FALL', 'Lemming thing', false);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (1, 'DROWN', 'Drowned', false);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (2, 'SELF_SPLODE', 'Self-explode death', false);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (3, 'TM_KILL', 'Teammate kill', false);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (4, 'BLEED', 'Made foe to bleed', true);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (5, 'HE_GRENADE_KILL', 'HE-grenade kill', true);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (6, 'KICK_KILL', 'Kick kill', true);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (7, 'KNIFE_KILL', 'Knife kill', true);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (8, 'KNIFE_THROWN_KILL', 'Knife-thrown kill', true);

INSERT INTO urt_count_achv (id, name, descr, is_positive)
VALUES (9, 'STOMP_KILL', 'Stomp kill', true);

COMMIT;


-- TABLE urt_player
CREATE TABLE urt_player (
  id              bigint         PRIMARY KEY 
, name            varchar(50)
, kills           integer        DEFAULT 0
, deaths          integer        DEFAULT 0
, kd_ratio        numeric(4,2)   DEFAULT 0
, avg_kd_ratio    numeric(4,2)   DEFAULT 0
, games_played    integer        DEFAULT 0
, last_played     timestamp
);

CREATE INDEX urt_player_idx_01 ON urt_player (name);

COMMENT ON TABLE urt_player                    IS 'UrT player stats.';
COMMENT ON COLUMN urt_player.id                IS 'Player unique ID.';
COMMENT ON COLUMN urt_player.name              IS 'Player nick name.';
COMMENT ON COLUMN urt_player.kills             IS 'Player total kill count.';
COMMENT ON COLUMN urt_player.deaths            IS 'Player total death count.';
COMMENT ON COLUMN urt_player.avg_kd_ratio      IS 'Player average kills to deaths ratio.';
COMMENT ON COLUMN urt_player.kd_ratio          IS 'Player total kills to deaths ratio.';
COMMENT ON COLUMN urt_player.games_played      IS 'Player total games played.';
COMMENT ON COLUMN urt_player.last_played       IS 'Player last play time.';


-- TABLE urt_player_aliases
CREATE TABLE urt_player_aliases (
  alias           varchar(50)    NOT NULL
, player_id       bigint         NOT NULL  REFERENCES urt_player (id)
, CONSTRAINT urt_player_aliases_uk1 UNIQUE(alias, player_id) 
);

CREATE INDEX urt_player_aliases_idx_01 ON urt_player_aliases (alias);
CREATE INDEX urt_player_aliases_idx_02 ON urt_player_aliases (player_id);

COMMENT ON TABLE urt_player_aliases                    IS 'UrT player stats.';
COMMENT ON COLUMN urt_player_aliases.alias             IS 'Player alias.';
COMMENT ON COLUMN urt_player_aliases.player_id         IS 'Player ID.';


-- TABLE urt_match
CREATE TABLE urt_match (
  id              bigint         PRIMARY KEY
, start_time      timestamp
, end_time        timestamp
, game_type       integer        REFERENCES urt_game_type (id)
, map_id          bigint         REFERENCES urt_map (id)
, log_file_name   varchar(50)
);

CREATE INDEX urt_match_idx_01 ON urt_match (game_type);

COMMENT ON TABLE urt_match                    IS 'UrT match stats.';
COMMENT ON COLUMN urt_match.id                IS 'Match unique ID.';
COMMENT ON COLUMN urt_match.start_time        IS 'Match start time.';
COMMENT ON COLUMN urt_match.end_time          IS 'Match end time.';
COMMENT ON COLUMN urt_match.game_type         IS 'Match game type ID ref.';
COMMENT ON COLUMN urt_match.map_id            IS 'Match map ID ref.';
COMMENT ON COLUMN urt_match.log_file_name     IS 'Match game log file name.';



-- TABLE urt_player_match
CREATE TABLE urt_player_match (
  id              bigint             PRIMARY KEY
, player_id       bigint   NOT NULL  REFERENCES urt_player (id)
, match_id        bigint   NOT NULL  REFERENCES urt_match (id)
, team_id         integer            REFERENCES urt_team (id) 
, kills           integer            DEFAULT 0
, deaths          integer            DEFAULT 0
, kd_ratio        numeric(4,2)       DEFAULT 0
, has_hits        boolean
, has_achvs       boolean
);

CREATE INDEX urt_player_match_idx_01 ON urt_player_match (player_id);
CREATE INDEX urt_player_match_idx_02 ON urt_player_match (match_id);
CREATE INDEX urt_player_match_idx_03 ON urt_player_match (team_id);

COMMENT ON TABLE urt_player_match                    IS 'UrT player match stats.';
COMMENT ON COLUMN urt_player_match.id                IS 'Player match unique ID.';
COMMENT ON COLUMN urt_player_match.player_id         IS 'Player ID ref.';
COMMENT ON COLUMN urt_player_match.match_id          IS 'Match ID ref.';
COMMENT ON COLUMN urt_player_match.team_id           IS 'Player team ID ref.';
COMMENT ON COLUMN urt_player_match.kills             IS 'Player kill count.';
COMMENT ON COLUMN urt_player_match.deaths            IS 'Player death count.';
COMMENT ON COLUMN urt_player_match.kd_ratio          IS 'Player kills to deaths ratio.';
COMMENT ON COLUMN urt_player_match.has_hits          IS 'Player has hit stats.';
COMMENT ON COLUMN urt_player_match.has_achvs         IS 'Player has achievements.';



-- TABLE urt_team_match
CREATE TABLE urt_team_match (
  id              bigint             PRIMARY KEY
, match_id        bigint   NOT NULL  REFERENCES urt_match (id)
, team_id         integer  NOT NULL  REFERENCES urt_team (id)
, team_kills      integer            DEFAULT 0
, team_deaths     integer            DEFAULT 0 
);

CREATE INDEX urt_team_match_idx_01 ON urt_team_match (match_id);
CREATE INDEX urt_team_match_idx_02 ON urt_team_match (team_id);

COMMENT ON TABLE urt_team_match                    IS 'UrT team match stats.';
COMMENT ON COLUMN urt_team_match.id                IS 'team match unique ID.';
COMMENT ON COLUMN urt_team_match.match_id          IS 'Match ID ref.';
COMMENT ON COLUMN urt_team_match.team_id           IS 'Team ID ref.';
COMMENT ON COLUMN urt_team_match.team_kills        IS 'Team kill count.';
COMMENT ON COLUMN urt_team_match.team_deaths       IS 'Team death count.';


-- TABLE urt_pm_hits
CREATE TABLE urt_pm_hits (
  player_match_id  bigint   NOT NULL  REFERENCES urt_player_match (id)
, target_id        integer  NOT NULL  REFERENCES urt_hit_target (id)
, hit_count        integer  NOT NULL
);

CREATE INDEX urt_pm_hits_idx_01 ON urt_pm_hits (player_match_id);
CREATE INDEX urt_pm_hits_idx_02 ON urt_pm_hits (target_id);

COMMENT ON TABLE urt_pm_hits                   IS 'UrT player match hits on target.';
COMMENT ON COLUMN urt_pm_hits.player_match_id  IS 'Player match ID ref.';
COMMENT ON COLUMN urt_pm_hits.target_id        IS 'Hit target ID ref.';
COMMENT ON COLUMN urt_pm_hits.hit_count        IS 'Target hit count.';


-- TABLE urt_pm_count_achv
CREATE TABLE urt_pm_count_achv (
  player_match_id  bigint   NOT NULL  REFERENCES urt_player_match (id)
, achv_id          integer  NOT NULL  REFERENCES urt_count_achv (id)
, achv_count       integer  NOT NULL
);

CREATE INDEX urt_pm_count_achv_idx_01 ON urt_pm_count_achv (player_match_id);
CREATE INDEX urt_pm_count_achv_idx_02 ON urt_pm_count_achv (achv_id);

COMMENT ON TABLE urt_pm_count_achv                   IS 'UrT player match hits on target.';
COMMENT ON COLUMN urt_pm_count_achv.player_match_id  IS 'Player match ID ref.';
COMMENT ON COLUMN urt_pm_count_achv.achv_id          IS 'Count achievement ID ref.';
COMMENT ON COLUMN urt_pm_count_achv.achv_count       IS 'Achievement count.';

-- TABLE urt_player_wpns
CREATE TABLE urt_player_wpns (
  player_match_id bigint         REFERENCES urt_player_match (id)
, weapon_id       integer        REFERENCES urt_weapon (id)
, kills           integer        DEFAULT 0
, CONSTRAINT urt_player_wpns_uk1 UNIQUE(player_match_id, weapon_id)
);

COMMENT ON TABLE urt_player_wpns                  IS 'UrT player weapon stats.';
COMMENT ON COLUMN urt_player_wpns.player_match_id    IS 'Player match ID ref.';
COMMENT ON COLUMN urt_player_wpns.weapon_id       IS 'Weapon ID ref.';
COMMENT ON COLUMN urt_player_wpns.kills           IS 'Kill count with weapon.';

CREATE INDEX urt_player_wpns_idx_01 ON urt_player_wpns (player_match_id);
CREATE INDEX urt_player_wpns_idx_02 ON urt_player_wpns (weapon_id);
