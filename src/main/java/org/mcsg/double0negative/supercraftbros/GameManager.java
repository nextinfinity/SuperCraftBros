package org.mcsg.double0negative.supercraftbros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class GameManager {

	private SuperCraftBros p;
	
    static GameManager instance = new GameManager();
    private ArrayList < Game > games = new ArrayList < Game > ();
    public HashMap<String, ArrayList<ItemStack>>classList = new HashMap<String, ArrayList<ItemStack>>();
    public HashMap<String, ArrayList<PotionEffect>>classEffects = new HashMap<String, ArrayList<PotionEffect>>();
    public HashMap<String, ItemStack>classHelmet = new HashMap<String, ItemStack>();
    public HashMap<String, ItemStack>classChest = new HashMap<String, ItemStack>();
    public HashMap<String, ItemStack>classLeg = new HashMap<String, ItemStack>();
    public HashMap<String, ItemStack>classBoots = new HashMap<String, ItemStack>();
    
    
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

    public Plugin getPlugin() {
        return p;
    }

    public void reloadGames() {
        LoadGames();
    }

    public void LoadGames() {
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        games.clear();
        int no = c.getInt("system.arenano", 0);
        int loaded = 0;
        int a = 1;
        while (loaded < no) {
            if (c.isSet("system.arenas." + a + ".x1")) {
                //c.set("system.arenas."+a+".enabled",c.getBoolean("system.arena."+a+".enabled", true));
                    //System.out.println(c.getString("system.arenas."+a+".enabled"));
                    //c.set("system.arenas."+a+".vip",c.getBoolean("system.arenas."+a+".vip", false));
                    System.out.println("Loading Arena: " + a);
                    loaded++;
                    games.add(new Game(a));
            }
            a++;
        }
    }
    
    public void loadClasses(){
    	int i = 1;
    	while(i > 0){
    		if(SettingsManager.getInstance().getClasses().contains("classes." + i)){
    			String key = ("classes." + i);
    			String name = SettingsManager.getInstance().getClasses().getString(key + ".name").toLowerCase();
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
    		}else{
    			i = -1;
    		}
    	}
    }
    
    public void loadSigns(){
    	FileConfiguration s = SettingsManager.getInstance().getSigns();
    	try{
    	for(String string : s.getStringList("signs")){
    		String[] l = string.split(",");
    		World world = Bukkit.getWorld(l[0]);
    		int x = Integer.parseInt(l[1]);
    		int y = Integer.parseInt(l[2]);
    		int z = Integer.parseInt(l[3]);
    		int id = Integer.parseInt(l[4]);
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
    			int id = SuperCraftBros.joinSigns.get(loc);
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
    	int x = 1;
    	while(x > 0){
    		if(c.contains(id + ".items." + x)){
    			try{
    				Material m = Material.getMaterial(c.getInt(id + ".items." + x + ".id"));
    				int amount = c.getInt(id + ".items." + x + ".amount");
    				ItemStack is = new ItemStack(m, amount);
    				if(c.contains(id + ".items." + x + ".id-modifier")){
    					int idm = c.getInt(id + ".items." + x + ".id-modifier");
    					is.setDurability((short)idm);
    				}
    				int y = 1;
    				while(y > 0){
    					if(c.contains(id + ".items." + x + ".enchantments." + y)){
    						String s = c.getString(id + ".items." + x + ".enchantments." + y);
    						String[] values = s.split(",");
    						String e = values[0];
    						Enchantment enchant = Enchantment.getByName(e);
    						int level = Integer.parseInt(values[1]);
    						is.addUnsafeEnchantment(enchant, level);
    						y = y+1;
    					}else{
    						y = -1;
    					}
    				}
    				inv.add(is);
    			}catch(Exception e){
    				System.out.println("Error adding item " + x + " for class " + c.getString(id + ".name") + ", please check the yml file.");
    			}
    			x = x+1;
    		}else{
    			x = -1;
    		}
    	}
    	return inv;
    }
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
    		return is;
    	}else{
			return new ItemStack(Material.AIR);
		}
    }
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
    		return is;
    	}else{
			return new ItemStack(Material.AIR);
		}
    }	
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
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}
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
			return is;
		}else{
			return new ItemStack(Material.AIR);
		}
	}
	
	public ArrayList<PotionEffect> getEffects(String id){
		FileConfiguration c = SettingsManager.getInstance().getClasses();
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		int x = 1;
		while(x > 0){
    		if(c.contains(id + ".effects." + x)){
    			String s = c.getString(id + ".effects." + x);
    			String[] values = s.split(",");
    			String effect = values[0];
    			int level = Integer.parseInt(values[1]);
    			PotionEffectType e = PotionEffectType.getByName(effect);
    			PotionEffect p = new PotionEffect(e, Integer.MAX_VALUE, level);
    			effects.add(p);
    			x = x+1;
    		}else{
    			x = -1;
    		}
		}
		return effects;
	}	
	
    public int getBlockGameId(Location v) {
        for (Game g: games) {
            if (g.isBlockInArena(v)) {
                return g.getID();
            }
        }
        return -1;
    }

    public int getPlayerGameId(Player p) {
        for (Game g: games) {
            if (g.isPlayerActive(p)) {
                return g.getID();
            }
        }
        return -1;
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
                return true;
            }
        }
        return false;
    }
    

    public void removeFromOtherQueues(Player p, int id) {
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

    public Game getGame(int a) {
        //int t = gamemap.get(a);
        for (Game g: games) {
            if (g.getID() == a) {
                return g;
            }
        }
        return null;
    }

    

    public void disableGame(int id) {
        getGame(id).disable();
    }

    public void enableGame(int id) {
        getGame(id).enable();
    }

    public ArrayList<Game> getGames() {
        return games;
    }
    
    public Game.State getGameMode(int a) {
        for (Game g: games) {
            if (g.getID() == a) {
                return g.getState();
            }
        }
        return null;
    }

    //TODO: Actually make this countdown correctly
    public void startGame(int a) {
        getGame(a).countdown(10);
    }

    public void addPlayer(Player p, int g) {
        Game game = getGame(g);
        if (game == null) {
            Message.send(p, ChatColor.RED+ "Game does not exist!");
            return;
        }
        getGame(g).addPlayer(p);
    }
    
    public String getPlayerClass(Player p){
    	Game g = getGame(getPlayerGameId(p));
    	return g.getPlayerClass(p);
    }
    

/*    public void autoAddPlayer(Player pl) {
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

    public void createArenaFromSelection(Player pl) {
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

        int no = c.getInt("system.arenano") + 1;
        c.set("system.arenano", no);
        if (games.size() == 0) {
            no = 1;
        } else no = games.get(games.size() - 1).getID() + 1;
        SettingsManager.getInstance().getSpawns().set(("spawns." + no), null);
        c.set("system.arenas." + no + ".world", max.getWorld().getName());
        c.set("system.arenas." + no + ".x1", max.getBlockX());
        c.set("system.arenas." + no + ".y1", max.getBlockY());
        c.set("system.arenas." + no + ".z1", max.getBlockZ());
        c.set("system.arenas." + no + ".x2", min.getBlockX());
        c.set("system.arenas." + no + ".y2", min.getBlockY());
        c.set("system.arenas." + no + ".z2", min.getBlockZ());
        c.set("system.arenas." + no + ".enabled", false);
        c.set("system.arenas." + no + ".min", 3);
        c.set("system.arenas." + no + ".max", 4);
        SettingsManager.getInstance().saveSystemConfig();
        hotAddArena(no);
        Message.send(pl, ChatColor.GREEN + "Arena ID " + no + " Succesfully added");

    }

    private void hotAddArena(int no) {
        Game game = new Game(no);
        games.add(game);
    }

    public void hotRemoveArena(int no) {
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