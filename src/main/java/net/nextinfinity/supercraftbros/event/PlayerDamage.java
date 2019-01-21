/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.supercraftbros.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.SettingsManager;

public class PlayerDamage implements Listener{

	private boolean usePercents;
	
	public PlayerDamage(){
		Boolean percents = SettingsManager.getInstance().getConfig().getBoolean("use-percents");
		usePercents = percents != null ? percents : false;
	}

	@EventHandler
	public void PlayerDamaged(EntityDamageEvent e){
		try{
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				String i = GameManager.getInstance().getPlayerGameId(p);
				if(!(i == null)){
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
					else if(usePercents){
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
				if(usePercents){
					String i = GameManager.getInstance().getPlayerGameId(p);
					if(!(i == null)){
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerDamaged(PlayerDeathEvent e){
		Player p = (Player)e.getEntity();
		String i = GameManager.getInstance().getPlayerGameId(p);
		if(!(i == null)){
			e.getDrops().clear();
			GameManager.getInstance().getGame(i).killPlayer(p, e.getDeathMessage());
			e.setDeathMessage(null);
		}
	}
}
