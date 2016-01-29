/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class PlayerJoin implements Listener{
	
	@EventHandler
	public void join(PlayerJoinEvent e){
	final Player p = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			public void run(){
				if(SettingsManager.getConfig().getBoolean("use-arena-teleport")){
					Location l =  p.getLocation();
			        int game =  GameManager.getInstance().getBlockGameId(l);
			        if(game != -1){
			        	clearPlayer(p);
			        }
				}else{
					clearPlayer(p);
				}	
				
			}
		}, 2);
	}

	@SuppressWarnings("deprecation")
	void clearPlayer(Player p){
		p.teleport(SettingsManager.getInstance().getLobbySpawn());
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.updateInventory();
		for(PotionEffectType e: PotionEffectType.values()){
			if(e != null && p.hasPotionEffect(e))
				p.removePotionEffect(e);
		}
	}
}
