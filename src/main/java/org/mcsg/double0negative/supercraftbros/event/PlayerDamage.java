package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
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
	
	@EventHandler
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
