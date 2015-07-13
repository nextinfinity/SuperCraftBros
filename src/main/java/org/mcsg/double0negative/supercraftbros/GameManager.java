package org.mcsg.double0negative.supercraftbros;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mcsg.double0negative.supercraftbros.classes.BlazeClass;
import org.mcsg.double0negative.supercraftbros.classes.CactusClass;
import org.mcsg.double0negative.supercraftbros.classes.CreeperClass;
import org.mcsg.double0negative.supercraftbros.classes.EndermanClass;
import org.mcsg.double0negative.supercraftbros.classes.GhastClass;
import org.mcsg.double0negative.supercraftbros.classes.PlayerClass;
import org.mcsg.double0negative.supercraftbros.classes.SkeletonClass;
import org.mcsg.double0negative.supercraftbros.classes.SpiderClass;
import org.mcsg.double0negative.supercraftbros.classes.WitchClass;
import org.mcsg.double0negative.supercraftbros.classes.WitherClass;
import org.mcsg.double0negative.supercraftbros.classes.ZombieClass;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class GameManager {

	private SuperCraftBros p;
	
    static GameManager instance = new GameManager();
    private ArrayList < Game > games = new ArrayList < Game > ();
    public HashMap<String, PlayerClass>classList = new HashMap<String, PlayerClass>();
    
    
    private GameManager() {

    }

    public static GameManager getInstance() {
        return instance;
    }

    public void setup(SuperCraftBros plugin) {
        p = plugin;
        LoadGames();
        
        classList.put("blaze", new BlazeClass(null));
        classList.put("cactus", new CactusClass(null));
        classList.put("creeper", new CreeperClass(null));
        classList.put("enderman", new  EndermanClass(null));
        classList.put("skeleton", new SkeletonClass(null));
        classList.put("wither", new WitherClass(null));
        classList.put("spider", new SpiderClass(null));
        classList.put("zombie", new ZombieClass(null));
        classList.put("blaze", new BlazeClass(null));
        classList.put("witch", new WitchClass(null));
        classList.put("ghast", new GhastClass(null));

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
                if (c.getBoolean("system.arenas." + a + ".enabled")) {
                    //System.out.println(c.getString("system.arenas."+a+".enabled"));
                    //c.set("system.arenas."+a+".vip",c.getBoolean("system.arenas."+a+".vip", false));
                    System.out.println("Loading Arena: " + a);
                    loaded++;
                    games.add(new Game(a));
                }
            }
            a++;
        }
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
    
    public PlayerClass getPlayerClass(Player p){
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