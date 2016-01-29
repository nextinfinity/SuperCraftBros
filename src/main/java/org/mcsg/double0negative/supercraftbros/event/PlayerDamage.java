/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class PlayerDamage implements Listener{


	@EventHandler
	public void PlayerDamaged(EntityDamageEvent e){
		try{
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				int i = GameManager.getInstance().getPlayerGameId(p);
				if(i != -1){
					Game g = GameManager.getInstance().getGame(i);
					if(g.getState() != Game.State.INGAME){
						e.setCancelled(true);
					}
					else if(e.getCause() == DamageCause.FALL){
						e.setCancelled(true);
					}
					else if(e.getCause() == DamageCause.VOID){
						e.setDamage(40);
					}
					else if(SettingsManager.getConfig().getBoolean("use-percents")){
						g.addDamage(p, e.getDamage());
						e.setDamage(0);
					}
				}
			}
		}catch(Exception er){}
	}
	
	@EventHandler
	public void PlayerDamagePlayer(EntityDamageByEntityEvent e){
		try{
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				if(SettingsManager.getConfig().getBoolean("use-percents")){
					int i = GameManager.getInstance().getPlayerGameId(p);
					if(i != -1){
						Game g = GameManager.getInstance().getGame(i);
						if(g.getState() != Game.State.INGAME){
							e.setCancelled(true);
						}else{
							g.addDamage(p, e.getDamage());
							double damage = g.getDamage(p);
							double m = (damage)/100;
							p.setVelocity(e.getDamager().getLocation().getDirection().multiply(m*m));
							e.setDamage(0);
						}	
					}	
				}
			}	
		}catch(Exception ex){}
	}
	
	@EventHandler
	public void PlayerDamaged(PlayerDeathEvent e){
		Player p = e.getEntity();
		int i = GameManager.getInstance().getPlayerGameId(p);
		if(i != -1){
			e.getDrops().clear();
			GameManager.getInstance().getGame(i).killPlayer(p, e.getDeathMessage());
			e.setDeathMessage(null);
		}
	}
}
