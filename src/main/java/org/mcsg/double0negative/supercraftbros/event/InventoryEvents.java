/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class InventoryEvents implements Listener {
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent e){
		String game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
    	if(!(game == null)){
    		e.setCancelled(true);
    		
    	}
    	
	}
	
	@EventHandler
	public void itemDrop(InventoryClickEvent e){
		String game = GameManager.getInstance().getPlayerGameId(Bukkit.getPlayerExact(e.getWhoClicked().getName()));
    	if(!(game == null)){
    		e.setCancelled(true);
    	}
    	
	}

}
