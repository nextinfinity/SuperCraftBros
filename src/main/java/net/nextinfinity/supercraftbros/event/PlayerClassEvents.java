/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.Game;
import net.nextinfinity.core.arena.Arena;
import net.nextinfinity.core.arena.GameState;
import net.nextinfinity.core.entity.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerClassEvents implements Listener {

	private final Game game;

	private final ArrayList<UUID> fire = new ArrayList<UUID>();
	private final ArrayList<UUID> sugar = new ArrayList<UUID>();
	private final ArrayList<UUID> doublej = new ArrayList<UUID>();
	private final ArrayList<UUID> fsmash = new ArrayList<UUID>();

	public PlayerClassEvents(Game game) {
		this.game = game;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void blockFire(BlockIgniteEvent e) {
		final Block block = e.getBlock();
		Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> {
			block.setType(Material.AIR);
			block.getState().update();
		}, 60);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player bukkitPlayer = event.getPlayer();
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		final UUID uuid = bukkitPlayer.getUniqueId();
		if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
			if (bukkitPlayer.getItemInHand().getType() == Material.ENDER_EYE) {
				event.setCancelled(true);
			} else if (!fire.contains(uuid)) {
				if (bukkitPlayer.getItemInHand().getType() == Material.FIRE) {
					Fireball f = bukkitPlayer.launchProjectile(Fireball.class);
					f.setVelocity(f.getVelocity().multiply(10));
					fire.add(uuid);
					Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> fire.remove(uuid), 600);
				}
			} else if (!sugar.contains(uuid)) {
				if (bukkitPlayer.getItemInHand().getType() == Material.SUGAR && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
					bukkitPlayer.setVelocity(new Vector(0, 2, 0));
					sugar.add(uuid);
					Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> sugar.remove(uuid), 100);
				}
				if (bukkitPlayer.getItemInHand().getType() == Material.SUGAR && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
					bukkitPlayer.setVelocity(bukkitPlayer.getLocation().getDirection().multiply(4));
					sugar.add(uuid);
					Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> sugar.remove(uuid), 100);
				}
			}
		} else {
			event.setCancelled(true);
		}
	}


	public boolean isOnGround(Player bukkitPlayer) {
		Location l = bukkitPlayer.getLocation();
		l = l.add(0, -1, 0);
		return l.getBlock().getState().getType() != Material.AIR;
	}

	public void explodePlayers(Player bukkitPlayer) {
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		if (player.isPlaying()) {
			Location l = bukkitPlayer.getLocation();
			l = l.add(0, -1, 0);
			for (int x = l.getBlockX() - 1; x <= l.getBlockX() + 1; x++) {
				for (int z = l.getBlockZ() - 1; z <= l.getBlockZ() + 1; z++) {
					//SendPacketToAll(new PacketPlayOutWorldEvent(2001,x, l.getBlockY()+1, z, l.getBlock().getState().getTypeId(), false));
					explodeBlocks(bukkitPlayer, new Location(l.getWorld(), x, l.getBlockY(), z));
				}
			}
			for (Entity pl : bukkitPlayer.getWorld().getEntities()) {
				if (pl != bukkitPlayer) {
					Location l2 = pl.getLocation();
					double d = pl.getLocation().distance(bukkitPlayer.getLocation());
					if (d < 5) {
						d = d / 5;
						pl.setVelocity(new Vector((1.5 - d) * getSide(l2.getBlockX(), l.getBlockX()), 1.5 - d, (1.5 - d) * getSide(l2.getBlockZ(), l.getBlockZ())));

					}
				}
			}
		}
	}

	public void explodeBlocks(Player bukkitPlayer, Location baseLoc) {
		Location playerLoc = bukkitPlayer.getLocation();
		Location aboveLoc = baseLoc.add(0, 1, 0);
		if (baseLoc.getBlock().getState().getType() != Material.AIR &&
				aboveLoc.getBlock().getState().getType() == Material.AIR) {
			final Entity entity = baseLoc.getWorld().spawnFallingBlock(aboveLoc, baseLoc.getBlock().getState().getType(), baseLoc.getBlock().getState().getData().getData());
			entity.setVelocity(new Vector((getSide(baseLoc.getBlockX(), playerLoc.getBlockX()) * 0.3), .1, (getSide(baseLoc.getBlockZ(), playerLoc.getBlockZ()) * 0.3)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> entity.remove(), 5);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player bukkitPlayer = event.getPlayer();
		UUID uuid = bukkitPlayer.getUniqueId();
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
			if (bukkitPlayer.isFlying()) {
				bukkitPlayer.setFlying(false);
				bukkitPlayer.setAllowFlight(false);
				Vector newVelocity = bukkitPlayer.getLocation().getDirection().multiply(.5);
				newVelocity.setY(.75);
				bukkitPlayer.setVelocity(newVelocity);
				doublej.add(uuid);
			}
			if (isOnGround(bukkitPlayer)) {
				bukkitPlayer.setAllowFlight(true);
				if (fsmash.contains(uuid)) {
					if (bukkitPlayer.isSneaking()) {
						explodePlayers(bukkitPlayer);
					}
					fsmash.remove(uuid);
				}
				doublej.remove(uuid);

			}
			if (doublej.contains(uuid) && bukkitPlayer.isSneaking()) {
				bukkitPlayer.setVelocity(new Vector(0, -1, 0));
				fsmash.add(uuid);
			}
		}
	}

	public int getSide(int i, int u) {
		if (i > u) return 1;
		if (i < u) return -1;
		return 0;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplode(EntityExplodeEvent e) {
		Location l = e.getLocation();
		if (e.getEntity() instanceof Fireball) {
			e.setCancelled(true);
			double x = l.getX();
			double y = l.getY();
			double z = l.getZ();
			l.getWorld().createExplosion(x, y, z, 3, false, false);
		}
	}


	@EventHandler
	public void onEntityRespawn(PlayerRespawnEvent event) {
		final Player bukkitPlayer = event.getPlayer();
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> {
			if (player.isPlaying()) {
				player.getArena().spawnPlayer(player);
			}
		}, 1);
	}

	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Player bukkitPlayer = event.getPlayer();
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
			if (event.getBlockPlaced().getType() == Material.TNT) {
				Location l = event.getBlockPlaced().getLocation();
				l.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
				event.getBlockPlaced().setType(Material.AIR);
				event.setCancelled(false);
			}
		}
	}
}
