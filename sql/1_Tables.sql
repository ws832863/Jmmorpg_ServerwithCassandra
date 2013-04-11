-- ----------------------------
-- @Autor Michel Montenegro --
-- ----------------------------
DROP DATABASE IF EXISTS dbjmmorpg;
CREATE DATABASE IF NOT EXISTS dbjmmorpg;
USE dbjmmorpg;
-- --------------------------

DROP TABLE IF EXISTS DropItems;

DROP TABLE IF EXISTS MobSpawn;

DROP TABLE IF EXISTS Player_Skill;

DROP TABLE IF EXISTS PlayerItem;

DROP TABLE IF EXISTS Skill;

DROP TABLE IF EXISTS NPC;

DROP TABLE IF EXISTS Player;

DROP TABLE IF EXISTS Classe;

DROP TABLE IF EXISTS AreaSpawn;

DROP TABLE IF EXISTS Login;

DROP TABLE IF EXISTS Race;

DROP TABLE IF EXISTS Clan;

DROP TABLE IF EXISTS Map;

DROP TABLE IF EXISTS Language;

DROP TABLE IF EXISTS Item;

DROP TABLE IF EXISTS GameServer;

CREATE TABLE GameServer (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  name VARCHAR(45)  NOT NULL  ,
  ip CHAR(20)  NULL  ,
  active INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX GameServer_UniqueIndex(name));



CREATE TABLE Item (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  name VARCHAR(45)  NOT NULL  ,
  stackable CHAR(1)  NOT NULL  ,
  bonusDamagePhysical INTEGER UNSIGNED  NULL  ,
  bonusDamageMagic INTEGER UNSIGNED  NULL  ,
  bonusArmorPhysical INTEGER UNSIGNED  NULL  ,
  bonusArmorMagic INTEGER UNSIGNED  NULL  ,
  bonusStr INTEGER UNSIGNED  NULL  ,
  bonusDex INTEGER UNSIGNED  NULL  ,
  bonusInte INTEGER UNSIGNED  NULL  ,
  bonusCon INTEGER UNSIGNED  NULL  ,
  manaLeft INTEGER UNSIGNED  NULL  ,
  bonusWis INTEGER UNSIGNED  NULL  ,
  bonusStamina INTEGER UNSIGNED  NULL  ,
  bonusEvasion INTEGER UNSIGNED  NULL  ,
  bonusSp INTEGER UNSIGNED  NULL  ,
  bonusHp INTEGER UNSIGNED  NULL  ,
  bonusMana INTEGER UNSIGNED  NULL  ,
  bonusCha INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Item__UniqueIndex(name));



CREATE TABLE Language (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  language VARCHAR(45)  NOT NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Language_Unique_Index(language));



CREATE TABLE Map (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  name VARCHAR(45)  NOT NULL  ,
  widthInTiles INTEGER UNSIGNED  NULL  ,
  heightInTiles INTEGER UNSIGNED  NULL  ,
  sizeTile INTEGER UNSIGNED  NULL  ,
  startTileHeroPosX INTEGER UNSIGNED  NULL  ,
  startTileHeroPosY INTEGER UNSIGNED  NULL  ,
  position INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Name_Unique_Index(name));



CREATE TABLE Clan (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  name VARCHAR(45)  NOT NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Clan_Unique_Index(name));



CREATE TABLE Race (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  race VARCHAR(45)  NOT NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Race_Unique_Index(race));



CREATE TABLE Login (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Clan_id INTEGER UNSIGNED  NULL  ,
  login VARCHAR(45)  NOT NULL  ,
  user_password VARCHAR(20)  NOT NULL  ,
  accessLevel CHAR(20)  NOT NULL  ,
  dateRegister DATETIME  NOT NULL  ,
  lastIP CHAR(20)  NULL  ,
  lastactive DATETIME  NULL  ,
  userCurrIP CHAR(20)  NOT NULL  ,
  lastServer CHAR(20)  NULL  ,
  name VARCHAR(45)  NOT NULL  ,
  email VARCHAR(45)  NOT NULL  ,
  birth DATE  NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Login_Unique_Index(login)  ,
INDEX Login_FKIndex1(Clan_id),
  FOREIGN KEY(Clan_id)
    REFERENCES Clan(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE AreaSpawn (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Map_id INTEGER UNSIGNED  NOT NULL  ,
  x INTEGER UNSIGNED  NULL  ,
  y INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX AreaSpawn_FKIndex1(Map_id),
  FOREIGN KEY(Map_id)
    REFERENCES Map(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Classe (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Race_id INTEGER UNSIGNED  NOT NULL  ,
  nameClasse VARCHAR(45)  NOT NULL  ,
  hpMaxBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  manaMaxBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  strBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  dexBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  inteBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  conBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  chaBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  wisBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  staminaBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  sexBase CHAR(1)  NOT NULL DEFAULT 0 ,
  resMagicBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  resPhysicalBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  evasionBase INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  startRowSprite INTEGER UNSIGNED  NULL  ,
  spBase INTEGER UNSIGNED  NULL  ,
  faceFileName VARCHAR(45)  NULL  ,
  facePosX INTEGER UNSIGNED  NULL  ,
  facePosY INTEGER UNSIGNED  NULL  ,
  faceWidth INTEGER UNSIGNED  NULL  ,
  faceHeight INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX Classe_FKIndex2(Race_id),
  FOREIGN KEY(Race_id)
    REFERENCES Race(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Player (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Map_id INTEGER UNSIGNED  NOT NULL  ,
  Login_id INTEGER UNSIGNED  NOT NULL  ,
  name VARCHAR(45)  NOT NULL  ,
  Classe_id INTEGER UNSIGNED  NOT NULL  ,
  hpMax INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  hpCurr INTEGER UNSIGNED  NULL  ,
  manaMax INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  manaCurr INTEGER UNSIGNED  NULL  ,
  expMax INTEGER UNSIGNED  NULL  ,
  expCurr INTEGER UNSIGNED  NULL  ,
  sp INTEGER UNSIGNED  NULL  ,
  str INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  dex INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  inte INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  con INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  cha INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  wis INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  stamina INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  sex CHAR(1)  NOT NULL DEFAULT 0 ,
  resMagic INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  resPhysical INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  evasion INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  dateCreate DATETIME  NULL  ,
  onLine CHAR(1)  NULL  ,
  lastAcess DATETIME  NULL  ,
  logoutPosXMap INTEGER UNSIGNED  NULL  ,
  logoutPosYMap INTEGER UNSIGNED  NULL  ,
  sector INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX Player_FKIndex1(Classe_id)  ,
UNIQUE INDEX Player_Unique_Index(name)  ,
INDEX Player_FKIndex6(Login_id)  ,
INDEX Player_FKIndex3(Map_id),
  FOREIGN KEY(Classe_id)
    REFERENCES Classe(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(Login_id)
    REFERENCES Login(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Map_id)
    REFERENCES Map(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE NPC (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Map_id INTEGER UNSIGNED  NOT NULL  ,
  Race_id INTEGER UNSIGNED  NOT NULL  ,
  Clan_id INTEGER UNSIGNED  NOT NULL  ,
  Classe_id INTEGER UNSIGNED  NOT NULL  ,
  name VARCHAR(45)  NOT NULL  ,
  hpMax INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  hpCurr INTEGER UNSIGNED  NOT NULL  ,
  manaMax INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  manaCurr INTEGER UNSIGNED  NOT NULL  ,
  str INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  dex INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  inte INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  con INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  cha INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  wis INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  stamina INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  sex CHAR(1)  NOT NULL DEFAULT 0 ,
  resMagic INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  resPhysical INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  evasion INTEGER UNSIGNED  NOT NULL DEFAULT 0 ,
  level INTEGER UNSIGNED  NOT NULL  ,
  typeNPC CHAR(20)  NOT NULL  ,
  aggro CHAR(1)  NOT NULL  ,
  posXMap INTEGER UNSIGNED  NOT NULL  ,
  posYMap INTEGER UNSIGNED  NOT NULL    ,
PRIMARY KEY(id)  ,
UNIQUE INDEX Player_Unique_Index(name)  ,
INDEX NPC_FKIndex1(Classe_id)  ,
INDEX NPC_FKIndex2(Clan_id)  ,
INDEX NPC_FKIndex3(Race_id)  ,
INDEX NPC_FKIndex4(Map_id),
  FOREIGN KEY(Classe_id)
    REFERENCES Classe(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(Clan_id)
    REFERENCES Clan(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(Race_id)
    REFERENCES Race(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY(Map_id)
    REFERENCES Map(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Skill (
  id INTEGER UNSIGNED  NOT NULL  ,
  Classe_id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  name VARCHAR(45)  NOT NULL  ,
  typeAction CHAR(15)  NOT NULL  ,
  damagePhysical INTEGER UNSIGNED  NULL  ,
  damageMagic INTEGER  NULL  ,
  bonusArmorPhysical INTEGER UNSIGNED  NULL  ,
  bonusArmorMagic INTEGER UNSIGNED  NULL  ,
  bonusDamagePhysical INTEGER UNSIGNED  NULL  ,
  bonusDamageMagic INTEGER UNSIGNED  NULL  ,
  bonusStr INTEGER UNSIGNED  NULL  ,
  bonusDex INTEGER UNSIGNED  NULL  ,
  bonusInte INTEGER UNSIGNED  NULL  ,
  bonusCon INTEGER UNSIGNED  NULL  ,
  manaLeft INTEGER UNSIGNED  NULL  ,
  bonusWis INTEGER UNSIGNED  NULL  ,
  bonusStamina INTEGER UNSIGNED  NULL  ,
  bonusEvasion INTEGER UNSIGNED  NULL  ,
  bonusSp INTEGER UNSIGNED  NULL  ,
  bonusHp INTEGER UNSIGNED  NULL  ,
  bonusMana INTEGER UNSIGNED  NULL  ,
  bonusCha INTEGER UNSIGNED  NULL  ,
  fileSpriteName VARCHAR(25)  NULL  ,
  colSprite INTEGER UNSIGNED  NULL  ,
  rowSprite INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX Skill_FKIndex1(Classe_id)  ,
UNIQUE INDEX Skill_UniqueIndex(name),
  FOREIGN KEY(Classe_id)
    REFERENCES Classe(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE PlayerItem (
  Player_id INTEGER UNSIGNED  NOT NULL  ,
  Item_id INTEGER UNSIGNED  NOT NULL  ,
  amount INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(Player_id, Item_id)  ,
INDEX Player_has_Item_FKIndex1(Player_id)  ,
INDEX Player_has_Item_FKIndex2(Item_id),
  FOREIGN KEY(Player_id)
    REFERENCES Player(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Item_id)
    REFERENCES Item(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE Player_Skill (
  Player_id INTEGER UNSIGNED  NOT NULL  ,
  Skill_id INTEGER UNSIGNED  NOT NULL    ,
PRIMARY KEY(Player_id, Skill_id)  ,
INDEX Player_has_Skill_FKIndex1(Player_id)  ,
INDEX Player_has_Skill_FKIndex2(Skill_id),
  FOREIGN KEY(Player_id)
    REFERENCES Player(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Skill_id)
    REFERENCES Skill(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE MobSpawn (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  NPC_id INTEGER UNSIGNED  NOT NULL  ,
  Map_id INTEGER UNSIGNED  NOT NULL  ,
  maxSpawn INTEGER UNSIGNED  NULL  ,
  minSpawn INTEGER UNSIGNED  NULL  ,
  boss INTEGER UNSIGNED  NULL  ,
  minion INTEGER UNSIGNED  NULL  ,
  timeRespawn INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX MobSpawn_FKIndex1(Map_id)  ,
INDEX MobSpawn_FKIndex2(NPC_id),
  FOREIGN KEY(Map_id)
    REFERENCES Map(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(NPC_id)
    REFERENCES NPC(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);



CREATE TABLE DropItems (
  id INTEGER UNSIGNED  NOT NULL   AUTO_INCREMENT,
  Item_id INTEGER UNSIGNED  NOT NULL  ,
  MobSpawn_id INTEGER UNSIGNED  NOT NULL  ,
  maxDrop INTEGER UNSIGNED  NULL  ,
  minDrop INTEGER UNSIGNED  NULL    ,
PRIMARY KEY(id)  ,
INDEX DropItems_FKIndex1(MobSpawn_id)  ,
INDEX DropItems_FKIndex2(Item_id),
  FOREIGN KEY(MobSpawn_id)
    REFERENCES MobSpawn(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
  FOREIGN KEY(Item_id)
    REFERENCES Item(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);