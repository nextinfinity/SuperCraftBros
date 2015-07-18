package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SuperCraftBros;

public class SignEvents implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(PlayerInteractEvent e){

        if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.LEFT_CLICK_BLOCK)) return;
        

        Block clickedBlock = e.getClickedBlock(); 
        if(!(clickedBlock.getType()==Material.SIGN || clickedBlock.getType()==Material.SIGN_POST || clickedBlock.getType()==Material.WALL_SIGN)) return;
        Sign thisSign = (Sign) clickedBlock.getState();
        String[] lines = thisSign.getLines();
        if(ChatColor.stripColor(lines[0]).equalsIgnoreCase("[SCB]")){
        	if(lines[1].equalsIgnoreCase("class")){
            	String cl = lines[2];
            	Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer()));
            	try{
            		g.setPlayerClass(e.getPlayer(), GameManager.getInstance().classList.get(cl.toLowerCase()).newInstance(e.getPlayer()));
            		g.getPlayerClass(e.getPlayer()).PlayerSpawn();
            	}catch(NullPointerException ex){
            		Message.send(e.getPlayer(), ChatColor.RED + "That class doesn't exist!");
            	}
            }
            else if(lines[1].equalsIgnoreCase("join")){
            	int game = Integer.parseInt(lines[2]);
            	Game g = GameManager.getInstance().getGame(game);
            	g.addPlayer(e.getPlayer());
            }
        }	
	}
    
    @EventHandler
	public void Place(SignChangeEvent e){
		if(ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[SCB]")){
			e.setLine(0, ChatColor.GOLD + "[" + ChatColor.YELLOW + "SCB" + ChatColor.GOLD + "]");
			if(ChatColor.stripColor(e.getLine(1)).equalsIgnoreCase("join")){
				e.setLine(1, ChatColor.GOLD + "Join");
				int gameint = Integer.parseInt(ChatColor.stripColor(e.getLine(2)));
				SuperCraftBros.joinSigns.put(e.getBlock().getLocation(), gameint);
			}
			if(ChatColor.stripColor(e.getLine(1)).equalsIgnoreCase("class")){
				e.setLine(1, ChatColor.GOLD + "Class");
			}
			e.setLine(2, ChatColor.YELLOW + ChatColor.stripColor(e.getLine(1)));
		}
	}
    
    @EventHandler
    public void Destroy(BlockBreakEvent e) {
      Block b = e.getBlock();
      if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
    	 Sign s = (Sign) b.getState();
    	 if(ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[SCB]")){
    		 if(ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("join")){
    			 SuperCraftBros.joinSigns.remove(e.getBlock().getLocation());
    		 }
    	 }
      }
    }
}
