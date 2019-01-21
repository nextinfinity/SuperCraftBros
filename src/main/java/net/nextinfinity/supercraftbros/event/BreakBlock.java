/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import net.nextinfinity.supercraftbros.GameManager;

public class BreakBlock implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(BlockBreakEvent e){
    	String game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    	if(!(game == null)){
    		e.setCancelled(true);
    		
    	}
    	
    	
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickHandler(BlockPlaceEvent e){
    	String game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    	if(!(game == null)){
    		e.setCancelled(true);
    		
    	}
    	
    	
    	
    	
    }
}
