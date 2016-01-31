/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void clickHandler(PlayerInteractEvent e){
    	Player p = e.getPlayer();
        if(!(e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction()==Action.LEFT_CLICK_BLOCK)) return;        
        Block clickedBlock = e.getClickedBlock(); 
        if(!(clickedBlock.getType()==Material.SIGN || clickedBlock.getType()==Material.SIGN_POST || clickedBlock.getType()==Material.WALL_SIGN)) return;
        Sign s = (Sign) clickedBlock.getState();
        if(ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[SCB]")){
        	if(ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("class")){
            	String cl = ChatColor.stripColor(s.getLine(2));
            	Game g = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer()));
            	String id = GameManager.getInstance().getPlayerGameId(p);
        		if(!(id == null)){
        			if(GameManager.getInstance().classList.keySet().contains(cl.toLowerCase())){
        				g.setPlayerClass(e.getPlayer(), cl.toLowerCase());
        				g.setInventory(p);
        			}else{
        				Message.send(e.getPlayer(), ChatColor.RED + "That class doesn't exist!");
        			}
        		}	
            }
            else if(ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("join")){
            	int game = Integer.parseInt(ChatColor.stripColor(s.getLine(2)));
            	p.chat("/scb join " + game);
            }
            else if(ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("leave")){
            	p.chat("/scb leave");
            }
        }	
	}
    
    @EventHandler
	public void Place(SignChangeEvent e){
		if(ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[SCB]")){
			e.setLine(0, ChatColor.GOLD + "[" + ChatColor.YELLOW + "SCB" + ChatColor.GOLD + "]");
			if(ChatColor.stripColor(e.getLine(1)).equalsIgnoreCase("join")){
				e.setLine(1, ChatColor.GOLD + "Join");
				String gameint = ChatColor.stripColor(e.getLine(2).toLowerCase());
				SuperCraftBros.joinSigns.put(e.getBlock().getLocation(), gameint);
				GameManager.getInstance().getGame(gameint).updateSigns();
			}
			if(ChatColor.stripColor(e.getLine(1)).equalsIgnoreCase("class")){
				e.setLine(1, ChatColor.GOLD + "Class");
			}
			if(ChatColor.stripColor(e.getLine(1)).equalsIgnoreCase("leave")){
				e.setLine(1, ChatColor.GOLD + "Leave");
			}
			e.setLine(2, ChatColor.YELLOW + ChatColor.stripColor(e.getLine(2)));
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
