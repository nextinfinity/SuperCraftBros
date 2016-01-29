/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Game {


	public enum State{
		INGAME, LOBBY, DISABLED, WAITING
	}

	//209.188.0.154



	private int gameID;
	private int spawnCount;
	private Arena arena;
	private State state;



	private HashMap<Player, Integer>players = new HashMap<Player, Integer>();
	private HashMap<Player, Double>damage = new HashMap<Player, Double>();
	private HashMap<Player, String>pClasses = new HashMap<Player, String>();
	private ArrayList<Player>inactive = new ArrayList<Player>();
	private ArrayList<Player>queue = new ArrayList<Player>();


	public Game(int a) {
		this.gameID = a;
		init();



	}


	public void init(){
		FileConfiguration s = SettingsManager.getInstance().getSystemConfig();
		int x = s.getInt("system.arenas." + gameID + ".x1");
		int y = s.getInt("system.arenas." + gameID + ".y1");
		int z = s.getInt("system.arenas." + gameID + ".z1");
		System.out.println(x + " " + y + " " + z);
		int x1 = s.getInt("system.arenas." + gameID + ".x2");
		int y1 = s.getInt("system.arenas." + gameID + ".y2");
		int z1 = s.getInt("system.arenas." + gameID + ".z2");
		System.out.println(x1 + " " + y1 + " " + z1);
		Location max = new Location(SettingsManager.getGameWorld(gameID), Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));
		System.out.println(max.toString());
		Location min = new Location(SettingsManager.getGameWorld(gameID), Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));
		System.out.println(min.toString());

		arena = new Arena(min, max);

		state = State.LOBBY;

		spawnCount = SettingsManager.getInstance().getSpawnCount(gameID);


	}


	public void addPlayer(Player p){
		int max = SettingsManager.getInstance().getSystemConfig().getInt("system.arenas." + gameID + ".max");
		int game = GameManager.getInstance().getPlayerGameId(Bukkit.getPlayerExact(p.getName()));
		if(state == State.LOBBY && players.size() < max && game == -1){
			p.teleport(SettingsManager.getInstance().getGameLobbySpawn(gameID));

			players.put(p , 3);
			damage.put(p, 0D);
			p.setGameMode(GameMode.SURVIVAL);
			p.setHealth(20); p.setFoodLevel(20);

			Message.send(p, ChatColor.YELLOW + "" + ChatColor.BOLD + "Joined arena " + gameID + ". Select a class!");
			msgAll(ChatColor.GREEN + p.getName()+ " joined the game!");
			updateTabAll();
			updateSigns();
		}
		else if(state == State.INGAME){
			Message.send(p, ChatColor.RED + "Game already started!");
		}
		else if(players.size() >= max){
			Message.send(p, ChatColor.RED + "Game Full!");
		}
		else if(game != -1){
			Message.send(p, ChatColor.RED + "Already in game!");
		}
		else{
			Message.send(p, ChatColor.RED + "Arena is disabled!");
		}


	}
	
	public void updateSigns(){
		FileConfiguration sys = SettingsManager.getInstance().getSystemConfig();
		for(Location loc : SuperCraftBros.joinSigns.keySet()){
			if(SuperCraftBros.joinSigns.get(loc) == gameID){
				Block b = loc.getBlock();
				Sign s = (Sign) b.getState();
				int i1 = players.size();
				int i2 = sys.getInt("system.arenas." + gameID + ".max");
				if(state == State.LOBBY){
					if(players != null){
					try{
						s.setLine(3, ChatColor.GREEN + "" + i1 + " / " + i2);
						s.update();
					}catch(Exception e){
						SuperCraftBros.joinSigns.remove(loc);
					}
					}else{
					try{
						s.setLine(3, ChatColor.GREEN + "0 / " + i2);
						s.update();
					}catch(Exception e){
						SuperCraftBros.joinSigns.remove(loc);
					}	
					}
				}else if(state == State.INGAME){
					try{
						s.setLine(3, ChatColor.YELLOW + "IN-GAME");
						s.update();
					}catch(Exception e){
						SuperCraftBros.joinSigns.remove(loc);
					}	
				}else{
					try{
						s.setLine(3, ChatColor.RED + "DISABLED");
						s.update();
					}catch(Exception e){
						SuperCraftBros.joinSigns.remove(loc);
					}	
				}
			}
		}
		GameManager.getInstance().saveSigns();
	}

	public void startGame(){
		if(players.size() < 2){
			msgAll("Not enough players");
			return;
		}
		inactive.clear();
		state = State.INGAME;
		updateSigns();

		for(Player p: players.keySet().toArray(new Player[0])){
			if(pClasses.containsKey(p)){
				spawnPlayer(p);
			}
			else{
				removePlayer(p, false);
				Message.send(p, ChatColor.RED + "You didn't pick a class!");
			}

		}
	}


	int count = 20;
	int tid = 0;
	public void countdown(int time) {
		count = time;
		Bukkit.getScheduler().cancelTask(tid);

		if (state == State.LOBBY) {
			tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameManager.getInstance().getPlugin(), new Runnable() {
				public void run() {
					if (count > 0) {
						if (count % 10 == 0) {
							msgAll(ChatColor.BLUE + "Game starting in "+count);
						}
						if (count < 6) {
							msgAll(ChatColor.BLUE + "Game starting in "+count);
						}
						count--;
					} else {
						startGame();
						Bukkit.getScheduler().cancelTask(tid);
					}
				}
			}, 0, 20);

		}
	}
	
	public void addDamage(Player p, double i){
		double d = getDamage(p);
		double nd = d + (i*10);
		damage.put(p, nd);
		updateTabAll();
	}

	public double getDamage(Player p){
		double i = damage.get(p);
		return i;
	}
	
	boolean started = false;


	public void setPlayerClass(Player player, String playerClass){
		int min = SettingsManager.getInstance().getSystemConfig().getInt("system.arenas." + gameID + ".min");
		if(player.hasPermission("scb.class." + playerClass) || player.hasPermission("scb.class.*")){
			clearPotions(player);
			Message.send(player, ChatColor.GREEN + "You choose " + playerClass.toUpperCase() + "!");
			//int prev = pClasses.keySet().size();
			pClasses.put(player, playerClass);
			updateTabAll();
			if(!started && pClasses.keySet().size()>= min && players.size() >= min ){
				countdown(SettingsManager.getConfig().getInt("countdown"));
				started = true;
			}
		}
		else{
			Message.send(player, ChatColor.RED + "You do not have permission for this class!");
		}
	}

	public void killPlayer(Player p, String msg){
		clearPotions(p);

		msgAll(ChatColor.GOLD + msg);
		int lives = players.get(p)-1;
		if(lives <= 0){
			playerEliminate(p);

		}
		else{
			players.put(p, lives);
			damage.put(p, 0D);
			msgAll(p.getName() + " has " + lives + " lives left");
		}
		updateTabAll();

	}

	@SuppressWarnings("deprecation")
	public void playerEliminate(Player p){
		started = false;
		msgAll(ChatColor.DARK_RED + p.getName() + " has been eliminated!");

		players.remove(p);
		//pClasses.remove(p);
		inactive.add(p);
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.updateInventory();
		p.setAllowFlight(false);
		p.setFlying(false);
		clearPotions(p);
		p.setVelocity(new Vector(0, 0, 0));
		p.teleport(SettingsManager.getInstance().getLobbySpawn());
		final ScoreboardManager m = Bukkit.getScoreboardManager();
		final Scoreboard board = m.getNewScoreboard();
		p.setScoreboard(board);

		if(players.keySet().size() <= 1 && state == State.INGAME){
			Player pl = null;
			for(Player pl2 : players.keySet()){
				pl = pl2;
			}
			Bukkit.broadcastMessage(ChatColor.BLUE + pl.getName() + " won Super Craft Bros on arena " + gameID);
			gameEnd();
		}
		updateTabAll();

	}

	public void clearPotions(Player p){
		for(PotionEffectType e: PotionEffectType.values()){
			if(e != null && p.hasPotionEffect(e))
				p.removePotionEffect(e);
		}
	}

	@SuppressWarnings("deprecation")
	public void gameEnd(){
		/*for(Entity e:SettingsManager.getGameWorld(gameID).getEntities()){
			if(arena.containsBlock(e.getLocation())){
				e.remove();
			}
		}*/
		for(Player p:players.keySet()){
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			p.updateInventory();
			p.teleport(SettingsManager.getInstance().getLobbySpawn());
			final ScoreboardManager m = Bukkit.getScoreboardManager();
			final Scoreboard board = m.getNewScoreboard();
			p.setScoreboard(board);
			clearPotions(p);
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		players.clear();
		pClasses.clear();
		inactive.clear();
		state = State.LOBBY;
		updateSigns();
	}


	public void updateTabAll(){
		for(Player p: players.keySet()){
			updateTab(p);
		}
	}

	public void updateTab(Player p){
		final ScoreboardManager m = Bukkit.getScoreboardManager();
		final Scoreboard board = m.getNewScoreboard();
		final Objective o = board.registerNewObjective("title", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(ChatColor.GOLD + "SuperCraftBros");
		for(Player pl: players.keySet()){
			if(SettingsManager.getConfig().getBoolean("use-percents")){
				Score score = o.getScore(ChatColor.YELLOW + pl.getName() + " [" + Math.round(damage.get(pl)) + "]");
				score.setScore(players.get(pl));
			}else{
				Score score = o.getScore(ChatColor.YELLOW + pl.getName());
				score.setScore(players.get(pl));
			}
			p.setScoreboard(board);
		}
	}

	public void spawnPlayer(Player p){
		if(players.containsKey(p)){
			p.setAllowFlight(true);
			Random r = new Random();
			Location l = SettingsManager.getInstance().getSpawnPoint(gameID, r.nextInt(spawnCount)+1);
			p.teleport(getSafePoint(l));
			setInventory(p);
		}

	}

	public void setInventory(Player p){
		p.getInventory().clear();
		for(ItemStack i : GameManager.getInstance().classList.get(pClasses.get(p))){
			p.getInventory().addItem(i);
		}
		p.getInventory().setHelmet(GameManager.getInstance().classHelmet.get(pClasses.get(p)));
		p.getInventory().setChestplate(GameManager.getInstance().classChest.get(pClasses.get(p)));
		p.getInventory().setLeggings(GameManager.getInstance().classLeg.get(pClasses.get(p)));
		p.getInventory().setBoots(GameManager.getInstance().classBoots.get(pClasses.get(p)));
		for(PotionEffect e : GameManager.getInstance().classEffects.get(pClasses.get(p))){
			p.addPotionEffect(e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public Location getSafePoint(Location l){
		if(isInVoid(l)){
			while(l.getBlockY() < 256){
				if(l.getBlock().getTypeId() != 0){
					return l.add(0,1,0);
				}
				else{
					l.add(0,1,0);
				}
			}
		}
		return l; //nothing safe at this point
	}

	@SuppressWarnings("deprecation")
	public boolean isInVoid(Location l){
		Location loc = l.clone();
		while(loc.getBlockY() > 0){
			loc.add(0,-1,0);
			if(loc.getBlock().getTypeId() != 0){
				return false;
			}
		}
		return true;
	}
	
	public int getID() {
		return gameID;
	}


	public boolean isBlockInArena(Location v) {
		return arena.containsBlock(v);
	}


	public void addSpawn() {
		spawnCount++;
	}


	public boolean isPlayerActive(Player p) {
		return players.keySet().contains(p);
	}


	public boolean isInQueue(Player p) {
		return queue.contains(p);
	}


	public void removeFromQueue(Player p) {
		queue.remove(p);
	}


	@SuppressWarnings("deprecation")
	public void removePlayer(Player p, boolean b) {
		players.remove(p);
		p.getInventory().clear();
		p.updateInventory();
		clearPotions(p);
		playerEliminate(p);
		inactive.remove(p);
		p.teleport(SettingsManager.getInstance().getLobbySpawn());
		final ScoreboardManager m = Bukkit.getScoreboardManager();
		final Scoreboard board = m.getNewScoreboard();
		p.setScoreboard(board);
		msgAll(ChatColor.RED + p.getName() + " left the game!");
		updateSigns();
	}

	public void msgAll(String msg){
		for(Player p: players.keySet()){
			Message.send(p, msg);
		}
	}


	public void enable(){
		disable();
		state = State.LOBBY;
	}

	public void disable() {
		for(Player p: players.keySet().toArray(new Player[0])){
			playerEliminate(p);
			Message.send(p, ChatColor.RED + "Game Disabled");
		}
		gameEnd();
	}


	public State getState() {
		return state;
	}


	public String getPlayerClass(Player p) {
		return pClasses.get(p).toUpperCase();
	}

	public Set<Player> getActivePlayers(){
		return players.keySet();
	}


}
