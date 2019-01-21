/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.nextinfinity.supercraftbros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nextinfinity.supercraftbros.util.Colorizer;
import com.nextinfinity.supercraftbros.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class GameManager {

	private SuperCraftBros p;
	
	static GameManager instance = new GameManager();
	private ArrayList < Game > games = new ArrayList < Game > ();
	public HashMap<String, ArrayList<ItemStack>> classList = new HashMap<String, ArrayList<ItemStack>>();
	public HashMap<String, ArrayList<PotionEffect>> classEffects = new HashMap<String, ArrayList<PotionEffect>>();
	public HashMap<String, ItemStack> classHelmet = new HashMap<String, ItemStack>();
	public HashMap<String, ItemStack> classChest = new HashMap<String, ItemStack>();
	public HashMap<String, ItemStack> classLeg = new HashMap<String, ItemStack>();
	public HashMap<String, ItemStack> classBoots = new HashMap<String, ItemStack>();
	
	
	private GameManager() {

	}

	public static GameManager getInstance() {
		return instance;
	}

	public void setup(SuperCraftBros plugin) {
		p = plugin;
		LoadGames();
		loadSigns();
		loadClasses();
	}
	
	public void setup() {
		LoadGames();
		loadSigns();
		loadClasses();
	}

	public Plugin getPlugin() {
		return p;
	}

	public void reloadGames() {
		LoadGames();
	}

	public void LoadGames() {
		FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
		games.clear();
		if(c.contains("system.arenas")){
			for(String a : c.getConfigurationSection("system.arenas").getKeys(false)){
				if (c.isSet("system.arenas." + a + ".x1")) {
					System.out.println("Loading Arena: " + a);
					games.add(new Game(a));
				}
			}
		}else{
			System.out.println("No games in config!");
		}
	}
	
	public void loadClasses(){
		classList.clear();
		classHelmet.clear();
		classChest.clear();
		classLeg.clear();
		classBoots.clear();
		classEffects.clear();
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		if(c.contains("classes")){
			for(String i : c.getConfigurationSection("classes").getKeys(false)){
				String key = ("classes." + i);
				String name = i.toLowerCase();
				ArrayList<ItemStack> inv = getInventory(key);
				classList.put(name, inv);
				ItemStack i1 = getHelmet(key);
				classHelmet.put(name, i1);
				ItemStack i2 = getChestplate(key);
				classChest.put(name, i2);
				ItemStack i3 = getLeggings(key);
				classLeg.put(name, i3);
				ItemStack i4 = getBoots(key);
				classBoots.put(name, i4);
				ArrayList<PotionEffect> effects = getEffects(key);
				classEffects.put(name, effects);
				i = i+1;
			}
		}else{
			System.out.println("No classes in config!");
		}
	}
	
	public void loadSigns(){
		SuperCraftBros.joinSigns.clear();
		FileConfiguration s = SettingsManager.getInstance().getSigns();
		try{
		for(String string : s.getStringList("signs")){
			String[] l = string.split(",");
			World world = Bukkit.getWorld(l[0]);
			int x = Integer.parseInt(l[1]);
			int y = Integer.parseInt(l[2]);
			int z = Integer.parseInt(l[3]);
			String id = l[4].toLowerCase();
			Location loc = new Location(world, x, y, z);
			SuperCraftBros.joinSigns.put(loc, id);
		}
		}catch(NullPointerException e){
			System.out.println("No signs in config!");
		}
	}
	
	public void saveSigns(){
		FileConfiguration s = SettingsManager.getInstance().getSigns();
		List<String> signList = new ArrayList<String>();
		for(Location loc : SuperCraftBros.joinSigns.keySet()){
			if(loc.getBlock().getType() == Material.SIGN || loc.getBlock().getType() == Material.SIGN_POST){
				String world = loc.getWorld().getName();
				int x = loc.getBlockX();
				int y = loc.getBlockY();
				int z = loc.getBlockZ();
				String id = SuperCraftBros.joinSigns.get(loc);
				signList.add(world + "," + x + "," + y + "," + z + "," + id);
			}
		}
		s.set("signs", signList);
		SettingsManager.getInstance().saveSigns();
	}
	
	@SuppressWarnings("deprecation")
	public ArrayList<ItemStack> getInventory(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		ArrayList<ItemStack> inv = new ArrayList<ItemStack>();
		for(String item : c.getConfigurationSection(id + ".items").getKeys(false)){
			try{
				Material m = Material.getMaterial(c.getInt(id + ".items." + item + ".id"));
				int amount = c.getInt(id + ".items." + item + ".amount");
				ItemStack is = new ItemStack(m, amount);
				if(c.contains(id + ".items." + item + ".id-modifier")){
					int idm = c.getInt(id + ".items." + item + ".id-modifier");
					is.setDurability((short)idm);
				}
				if(c.contains(id + ".items." + item + ".enchantments")){
					for(String e : c.getConfigurationSection(id + ".items." + item + ".enchantments").getKeys(false)){
						Enchantment enchant = Enchantment.getByName(e);
						int level = Integer.parseInt(c.getString(id + ".items." + item + ".enchantments." + e));
						is.addUnsafeEnchantment(enchant, level);
					}
				}
				inv.add(is);
			}catch(Exception e){
				System.out.println("Error adding item " + item + " for class " + id + ", please check the yml file.");
			}
		}
		return inv;
	}
	@SuppressWarnings("deprecation")
	public ItemStack getHelmet(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		if(c.contains(id + ".helmet")){
			Material m = Material.getMaterial(c.getInt(id + ".helmet.id"));
			ItemStack is = new ItemStack(m, 1);
			if(m == Material.LEATHER_HELMET && c.contains(id + ".helmet.color")){
				String color = c.getString(id + ".helmet.color");
				String[] rgb = color.split(",");
				int r = Integer.parseInt(rgb[0]);
				int g = Integer.parseInt(rgb[1]);
				int b = Integer.parseInt(rgb[2]);
				is = Colorizer.setColor(is, r, g, b);
			}
			if(c.contains(id + ".helmet.id-modifier")){
				int idm = c.getInt(id + ".helmet.id-modifier");
				is.setDurability((short)idm);
			}
			if(c.getInt(id + ".helmet.id") == 397 && c.getInt(id + ".helmet.id-modifier") == 3 && c.contains(id + ".helmet.entity")){
				String player = c.getString(id + ".helmet.entity");
				SkullMeta meta = (SkullMeta) is.getItemMeta();
				meta.setOwner(player);
				is.setItemMeta(meta);
			}
			if(c.contains(id + ".helmet.enchantments")){
				for(String e : c.getConfigurationSection(id + ".helmet.enchantments").getKeys(false)){
					Enchantment enchant = Enchantment.getByName(e);
					int level = Integer.parseInt(c.getString(id + ".helmet.enchantments." + e));
					is.addUnsafeEnchantment(enchant, level);
				}
			}
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}
	@SuppressWarnings("deprecation")
	public ItemStack getChestplate(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		if(c.contains(id + ".chestplate")){
			Material m = Material.getMaterial(c.getInt(id + ".chestplate.id"));
			ItemStack is = new ItemStack(m, 1);
			if(m == Material.LEATHER_CHESTPLATE && c.contains(id + ".chestplate.color")){
				String color = c.getString(id + ".chestplate.color");
				String[] rgb = color.split(",");
				int r = Integer.parseInt(rgb[0]);
				int g = Integer.parseInt(rgb[1]);
				int b = Integer.parseInt(rgb[2]);
				is = Colorizer.setColor(is, r, g, b);
			}
			if(c.contains(id + ".chestplate.enchantments")){
				for(String e : c.getConfigurationSection(id + ".chestplate.enchantments").getKeys(false)){
					Enchantment enchant = Enchantment.getByName(e);
					int level = Integer.parseInt(c.getString(id + ".chestplate.enchantments." + e));
					is.addUnsafeEnchantment(enchant, level);
				}
			}
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}	
	@SuppressWarnings("deprecation")
	public ItemStack getLeggings(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		if(c.contains(id + ".leggings")){
			Material m = Material.getMaterial(c.getInt(id + ".leggings.id"));
			ItemStack is = new ItemStack(m, 1);
			if(m == Material.LEATHER_LEGGINGS && c.contains(id + ".leggings.color")){
				String color = c.getString(id + ".leggings.color");
				String[] rgb = color.split(",");
				int r = Integer.parseInt(rgb[0]);
				int g = Integer.parseInt(rgb[1]);
				int b = Integer.parseInt(rgb[2]);
				is = Colorizer.setColor(is, r, g, b);
			}
			if(c.contains(id + ".leggings.enchantments")){
				for(String e : c.getConfigurationSection(id + ".leggings.enchantments").getKeys(false)){
					Enchantment enchant = Enchantment.getByName(e);
					int level = Integer.parseInt(c.getString(id + ".leggings.enchantments." + e));
					is.addUnsafeEnchantment(enchant, level);
				}
			}
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}
	@SuppressWarnings("deprecation")
	public ItemStack getBoots(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		if(c.contains(id + ".boots")){
			Material m = Material.getMaterial(c.getInt(id + ".boots.id"));
			ItemStack is = new ItemStack(m, 1);
			if(m == Material.LEATHER_BOOTS && c.contains(id + ".boots.color")){
				String color = c.getString(id + ".boots.color");
				String[] rgb = color.split(",");
				int r = Integer.parseInt(rgb[0]);
				int g = Integer.parseInt(rgb[1]);
				int b = Integer.parseInt(rgb[2]);
				is = Colorizer.setColor(is, r, g, b);
			}
			if(c.contains(id + ".boots.enchantments")){
				for(String e : c.getConfigurationSection(id + ".boots.enchantments").getKeys(false)){
					Enchantment enchant = Enchantment.getByName(e);
					int level = Integer.parseInt(c.getString(id + ".boots.enchantments." + e));
					is.addUnsafeEnchantment(enchant, level);
				}
			}
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}
	
	public ArrayList<PotionEffect> getEffects(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		for(String effect : c.getConfigurationSection(id + ".effects").getKeys(false)){
			int level = Integer.parseInt(c.getString(id + ".effects." + effect));
			PotionEffectType e = PotionEffectType.getByName(effect);
			PotionEffect p = new PotionEffect(e, Integer.MAX_VALUE, level);
			effects.add(p);
		}
		return effects;
	}	
	
	public String getBlockGameId(Location v) {
		for (Game g: games) {
			if (g.isBlockInArena(v)) {
				return g.getID();
			}
		}
		return null;
	}

	public String getPlayerGameId(Player p) {
		for (Game g: games) {
			if (g.isPlayerActive(p)) {
				return g.getID();
			}
		}
		return null;
	}
	
	public boolean isPlaying(Player player){
		for(Game g: games){
			if(g.isPlaying(player)) return true;
		}
		return false;
	}
	
	public boolean isSpectating(Player player){
		for(Game g: games){
			if(g.isSpectating(player)) return true;
		}
		return false;
	}

	public boolean isPlayerActive(Player player) {
		for (Game g: games) {
			if (g.isPlayerActive(player)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPlayerInactive(Player player) {
		for (Game g: games) {
			if (g.isPlayerActive(player)) {
				return false;
			}
		}
		return true;
	}
	

	public void removeFromOtherQueues(Player p, String id) {
		for (Game g: getGames()) {
			if (g.isInQueue(p) && g.getID() != id) {
				g.removeFromQueue(p);
				Message.send(p, ChatColor.GREEN+"Removed from the queue in arena "+ g.getID());
			}
		}
	}

	public int getGameCount() {
		return games.size();
	}

	public Game getGame(String a) {
		//int t = gamemap.get(a);
		for (Game g: games) {
			if (g.getID().equalsIgnoreCase(a)) {
				return g;
			}
		}
		return null;
	}

	

	public void disableGame(String id) {
		getGame(id).disable();
	}

	public void enableGame(String id) {
		getGame(id).enable();
	}

	public ArrayList<Game> getGames() {
		return games;
	}
	
	public Game.State getGameMode(String a) {
		for (Game g: games) {
			if (g.getID() == a) {
				return g.getState();
			}
		}
		return null;
	}

	//TODO: Actually make this countdown correctly
	public void startGame(String a) {
		getGame(a).countdown(10);
	}

	public void addPlayer(Player p, String g) {
		Game game = getGame(g);
		if(isPlayerActive(p)){
			Message.send(p, ChatColor.RED + "You are already in a game!");
			return;
		}
		if (game == null) {
			Message.send(p, ChatColor.RED+ "Game does not exist!");
			return;
		}
		getGame(g).addPlayer(p);
	}
	
	public void addSpectator(Player p, String g) {
		Game game = getGame(g);
		if(isPlayerActive(p)){
			Message.send(p, ChatColor.RED + "You are already in a game!");
			return;
		}
		if (game == null) {
			Message.send(p, ChatColor.RED+ "Game does not exist!");
			return;
		}
		getGame(g).addSpectator(p);
	}
	
	public String getPlayerClass(Player p){
		Game g = getGame(getPlayerGameId(p));
		return g.getPlayerClass(p);
	}
	

/*	public void autoAddPlayer(Player pl) {
		ArrayList < Game > qg = new ArrayList < Game > (5);
		for (Game g: games) {
			if (g.getMode() == Game.GameMode.WAITING) qg.add(g);
		}
		//TODO: fancy auto balance algorithm
		if (qg.size() == 0) {
			Message.send(pl, ChatColor.RED + "No games to join");
			msgmgr.sendMessage(PrefixType.WARNING, "No games to join!", pl);
			return;
		}
		qg.get(0).addPlayer(pl);
	}*/

	public WorldEditPlugin getWorldEdit() {
		return p.getWorldEdit();
	}

	public void createArenaFromSelection(Player pl, String name) {
		FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
		//SettingsManager s = SettingsManager.getInstance();

		WorldEditPlugin we = p.getWorldEdit();
		Selection sel = we.getSelection(pl);
		if (sel == null) {
			Message.send(pl, ChatColor.RED+"You must make a WorldEdit Selection first!");
			return;
		}
		Location max = sel.getMaximumPoint();
		Location min = sel.getMinimumPoint();

		/* if(max.getWorld()!=SettingsManager.getGameWorld() || min.getWorld()!=SettingsManager.getGameWorld()){
			Message.send(pl, ChatColor.RED+"Wrong World!");
			return;
		}*/
		SettingsManager.getInstance().getSpawns().set(("spawns." + name), null);
		c.set("system.arenas." + name + ".world", max.getWorld().getName());
		c.set("system.arenas." + name + ".x1", max.getBlockX());
		c.set("system.arenas." + name + ".y1", max.getBlockY());
		c.set("system.arenas." + name + ".z1", max.getBlockZ());
		c.set("system.arenas." + name + ".x2", min.getBlockX());
		c.set("system.arenas." + name + ".y2", min.getBlockY());
		c.set("system.arenas." + name + ".z2", min.getBlockZ());
		c.set("system.arenas." + name + ".enabled", false);
		c.set("system.arenas." + name + ".min", 3);
		c.set("system.arenas." + name + ".max", 4);
		SettingsManager.getInstance().saveSystemConfig();
		hotAddArena(name);
		Message.send(pl, ChatColor.GREEN + "Arena " + name.toUpperCase() + " succesfully added");
	}
	
	

	private void hotAddArena(String no) {
		Game game = new Game(no);
		games.add(game);
	}

	public void hotRemoveArena(String no) {
		for (Game g: games.toArray(new Game[0])) {
			if (g.getID() == no) {
				games.remove(getGame(no));
			}
		}
	}

	public Game getGamePlayer(Player player) {
		return getGame(getPlayerGameId(player));
	}


 
}