/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.SettingsManager;

public class PlayerJoin implements Listener{
	
	private boolean arenaTeleport;
	
	public PlayerJoin(){
		Boolean arenaTp = SettingsManager.getInstance().getConfig().getBoolean("use-arena-teleport");
		arenaTeleport = arenaTp != null ? arenaTp : false;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		final Player p = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			public void run(){
				if(arenaTeleport){
					Location l =  p.getLocation();
					String game =  GameManager.getInstance().getBlockGameId(l);
					if(!(game == null)){
						clearPlayer(p);
					}
				}else{
					clearPlayer(p);
				}				
			}
		}, 2);
	}

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
