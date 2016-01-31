/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.event;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.Game.State;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

import java.util.HashMap;
import java.util.Set;

public class PlayerClassEvents implements Listener{

	GameManager gm;
	
	HashMap<Player, Boolean> sugar = new HashMap<Player, Boolean>();
	
	protected boolean smash = false;

	private boolean doublej = false;
	protected boolean fsmash  = false;


	public PlayerClassEvents(){
		gm = GameManager.getInstance();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void blockFire(BlockIgniteEvent e){
		final Block b = e.getBlock();
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			@SuppressWarnings("deprecation")
			public void run(){
				b.setTypeId(0);
				b.getState().update();
			}
		}, 60);
	}
	
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent event){
		if (event.getEntity() instanceof Player){
			Player p = (Player)event.getEntity();
			String id = gm.getPlayerGameId(p);
			if(!(id == null)){
				event.setCancelled(true);
				p.setFoodLevel(20);
			}	
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		final Player p = e.getPlayer();
		String id = gm.getPlayerGameId(p);
		if(!(id == null)){
			Game g = gm.getGame(id);
			if(g.getState() == Game.State.INGAME){
				if(sugar.get(p) == null){
					sugar.put(p, true);
				}
				if(e.getPlayer().getItemInHand().getType() == Material.DIAMOND_AXE){
					Smash(p);
				}
				else if(p.getItemInHand().getType() == Material.EYE_OF_ENDER){
					e.setCancelled(true);
				}
				else if(p.getItemInHand().getType() == Material.FIREBALL){
                    Fireball f = p.launchProjectile(Fireball.class);
                    f.setVelocity(f.getVelocity().multiply(10));
				}else if(sugar.get(p)){
                    if(p.getItemInHand().getType() == Material.SUGAR && (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)){
                        p.setVelocity(new Vector(0, 2, 0));
                        sugar.put(p, false);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
                			public void run(){
                				sugar.put(p, true);
                			}
                		}, 100);
                    }
                    if(p.getItemInHand().getType() == Material.SUGAR && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
                        p.setVelocity(p.getLocation().getDirection().multiply(4));
                        sugar.put(p, false);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
                			public void run(){
                				sugar.put(p, true);
                			}
                		}, 100);
                    }
				}
			}else{
				e.setCancelled(true);
			}
		}

	}
	
	public void Smash(Player p){
		
	}

	@SuppressWarnings("deprecation")
	public boolean isOnGround(Player p){
		Location l = p.getLocation();
		l = l.add(0, -1, 0);
		return l.getBlock().getState().getTypeId() != 0;
	}

	public void explodePlayers(Player p){
		String i = GameManager.getInstance().getPlayerGameId(p);
		if(!(i == null)){
			Set<Player>pls = GameManager.getInstance().getGame(i).getActivePlayers();

			Location l = p.getLocation();
			l = l.add(0, -1, 0);
			for(int x = l.getBlockX() - 1; x<=l.getBlockX()+1; x++){
				for(int z = l.getBlockZ() - 1; z<=l.getBlockZ()+1; z++){
				 //SendPacketToAll(new PacketPlayOutWorldEvent(2001,x, l.getBlockY()+1, z, l.getBlock().getState().getTypeId(), false));
					explodeBlocks(p, new Location(l.getWorld(), x, l.getBlockY(), z));
				}
			}
			for(Entity pl:p.getWorld().getEntities()){
				if(pl != p){
					ItemStack s = p.getItemInHand();
					Location l2 = pl.getLocation();
					double d = pl.getLocation().distance(p.getLocation());
					if(d < 5){
						d = d / 5;
						pl.setVelocity(new Vector((1.5-d) * getSide(l2.getBlockX(), l.getBlockX()), 1.5-d, (1.5-d)*getSide(l2.getBlockZ(), l.getBlockZ())));
						
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void explodeBlocks(Player p, Location l){
		Location l2 = p.getLocation();
		if(l.getBlock().getState().getTypeId() != 0){
			final Entity e  = l.getWorld().spawnFallingBlock(l, l.getBlock().getState().getTypeId(), l.getBlock().getState().getData().getData());
			e.setVelocity(new Vector((getSide(l.getBlockX(), l2.getBlockX()) * 0.3),.1, (getSide(l.getBlockZ(), l2.getBlockZ()) * 0.3)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
				public void run(){
					e.remove();
				}
			}, 5);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void SendPacketToAll(Packet p, Player player){
		for(Player pl: GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).getActivePlayers()){
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(p);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		String id = gm.getPlayerGameId(p);
		if(!(id == null)){
			Game g = gm.getGame(id);
			if(g.getState() == Game.State.INGAME){
				if(p.isFlying()){
					p.setFlying(false);
					p.setAllowFlight(false);
					Vector v = p.getLocation().getDirection().multiply(.5);
					v.setY(.75);
					p.setVelocity(v);
					doublej = true;
				}
				if(isOnGround(p)){
					p.setAllowFlight(true);
					if(fsmash){
						explodePlayers(p);
						fsmash = false;
					}
					doublej = false;

				}
				if(doublej && p.isSneaking()){
					p.setVelocity(new Vector(0, -1, 0));
					fsmash = true;
				}
			}	
		}
	}

	public int getSide(int i, int u){
		if(i > u) return 1;
		if(i < u)return -1;
		return 0;
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			String game = GameManager.getInstance().getPlayerGameId(p);
			if(!(game == null)){
				Game g = gm.getGame(game);
				if(g.getState() == Game.State.INGAME){
					if(smash){
						p.setHealth(20);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent e){
		try{
			Player victim = null;
			Player attacker = null;
			if(e.getEntity() instanceof Player){
				victim = (Player)e.getEntity();
			}
			if(e.getDamager() instanceof Player){
				attacker = (Player)e.getDamager();
			}
			if(victim != null && attacker != null){
				if(!(gm.getPlayerGameId(victim) == null) && !(gm.getPlayerGameId(attacker) == null)){
					if(smash){
						victim.setHealth(20);
					}
				//	gm.getPlayerClass(attacker).PlayerAttack(victim);
				}
			}
		}catch(Exception et){}

	}


	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplode(EntityExplodeEvent e){
		e.blockList().clear();
		Location l = e.getLocation();
		if(e.getEntity() instanceof Fireball){
			e.setCancelled(true);
			double x = l.getX();
			double y = l.getY();
			double z = l.getZ();
			l.getWorld().createExplosion(x, y, z, 3, false, false);
		}
	}


	@EventHandler
	public void onEntityRespawn(PlayerRespawnEvent e){
		final Player p = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			public void run(){
				String id = gm.getPlayerGameId(p);
				if(!(id == null)){
					gm.getGame(id).spawnPlayer(p);
				}
				else{
					p.teleport(SettingsManager.getInstance().getLobbySpawn());
				}
			}
		}, 1);
	}

	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e){
		String id = gm.getPlayerGameId(e.getPlayer());
		if(!(id == null)){
			if(gm.getGame(id).getState() == State.INGAME){
			  if(e.getBlockPlaced().getType() == Material.TNT){
				  
			  }
		}
	}
}
}	
