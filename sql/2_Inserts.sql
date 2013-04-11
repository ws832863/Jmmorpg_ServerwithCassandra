-- ----------------------------
-- @Autor Michel Montenegro --
-- ----------------------------

-- Cria os mapas (Areas do mundo)
insert into Map (name, widthInTiles, heightInTiles, sizeTile, startTileHeroPosX, startTileHeroPosY, position) 
values ('map_1', 30, 30, 32, 12, 9, 0);

-- Inserindo o Clan dos Administradores do jogo 
-- Obs: "id" é auto incremento
insert into Clan (id, name) values (null, '[Admin]Administradores');
insert into Clan (id, name) values (null, '[PLT]Player Teste');
insert into Clan (id, name) values (null, '[MON]Monstros');


-- Inserindo a Raça Huamana para ser a primeira do jogo
insert into Race (id, race) values (null, 'Humano');
insert into Race (id, race) values (null, 'Monstro');
 
-- Inserindo a classe Warrior  
insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Warrior', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Warrior', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Monge', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Monge', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Mage', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Assassin', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Assassin', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, 0, 1);

insert into Classe (id, nameClasse, hpMaxBase, manaMaxBase, strBase, dexBase, inteBase, conBase, chaBase, wisBase, staminaBase, sexBase, resMagicBase, resPhysicalBase, evasionBase, spBase, Race_id)
  values(null, 'Mage', 100, 100, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, 0, 1);
  
  
  
 -- Inserindo os usuarios testes para ter acesso no login
insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'admin', 'admin', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Michel Montenengro', 'admin@teste.com.br', '1979-04-24', 1);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player1', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 1', 'player1@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player2', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 2', 'player2@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player3', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 3', 'player3@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player4', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 4', 'player4@teste.com.br', '1979-04-24', 2);
  
insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player5', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 5', 'player5@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player6', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 6', 'player6@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player7', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 7', 'player7@teste.com.br', '1979-04-24', 2);

insert into Login (id, login, user_password, accessLevel, dateRegister, lastIP, lastactive, userCurrIP, lastServer, name, email, birth, Clan_id) 
  values(null, 'player8', 'player', '0', '2011-07-09', '127.0.0.1', '2011-07-09', '127.0.0.1', 'Server 1', 'Player 8', 'player8@teste.com.br', '1979-04-24', 2);
 
  
-- Inserindo os Players
insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 1, 'Administrador', 1, 8, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
values(null, 2, 'player1', 1, 1, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);
  
insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 2, 'player2', 1, 2, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 3, 'player3', 1, 3, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 4, 'player4', 1, 4, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 5, 'player5', 1, 5, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 6, 'player6', 1, 6, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'F', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 7, 'player7', 1, 7, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

insert into Player (id, Login_id, name, Map_id, Classe_id, hpMax, hpCurr, manaMax, manaCurr, expMax, expCurr, sp, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, dateCreate, onLine, lastAcess, sector)
  values(null, 8, 'player8', 1, 8, 100, 100, 100, 100, 100, 0, 0, 10, 10, 10, 10, 10, 10, 10, 'M', 5, 5, 0, '2011-07-09', 'f', '2011-07-09', 1);

-- Insere os NPCs do jogo
insert into NPC (Map_id, Race_id, Clan_id, Classe_id, name, manaMax, str, dex, inte, con, cha, wis, stamina, sex, resMagic, resPhysical, evasion, hpMax, hpCurr, manaCurr, level, typeNPC, aggro, posXMap, posYMap ) 
 values (1, 2, 3, 1, 'Monstruoso', 50, 5, 5, 5, 5, 5, 5, 5, 'M', 0, 0, 0, 50, 50, 50, 1, 'Monster', 'T', 0,0  );