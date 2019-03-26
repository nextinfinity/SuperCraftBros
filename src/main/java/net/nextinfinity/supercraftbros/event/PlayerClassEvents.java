/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.Game;
import net.nextinfinity.core.arena.GameState;
import net.nextinfinity.core.player.GamePlayer;
import net.nextinfinity.supercraftbros.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerClassEvents implements Listener {

	private final Game game;

	private final ArrayList<UUID> fire = new ArrayList<>();
	private final ArrayList<UUID> sugar = new ArrayList<>();
	private final ArrayList<UUID> doublej = new ArrayList<>();
	private final ArrayList<UUID> fsmash = new ArrayList<>();

	public PlayerClassEvents(Game game) {
		this.game = game;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player bukkitPlayer = event.getPlayer();
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		final UUID uuid = bukkitPlayer.getUniqueId();
		if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
			if (bukkitPlayer.getInventory().getItemInMainHand().getType() == Material.ENDER_EYE) {
				event.setCancelled(true);
			}
			if (!fire.contains(uuid)) {
				if (bukkitPlayer.getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
					Fireball f = bukkitPlayer.launchProjectile(Fireball.class);
					f.setVelocity(f.getVelocity().multiply(10));
					fire.add(uuid);
					Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> fire.remove(uuid), 600);
				}
			}
			if (!sugar.contains(uuid)) {
				if (bukkitPlayer.getInventory().getItemInMainHand().getType() == Material.SUGAR) {
					if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
						bukkitPlayer.setVelocity(new Vector(0, 2, 0));
					} else {
						bukkitPlayer.setVelocity(bukkitPlayer.getLocation().getDirection().multiply(4));
					}
					sugar.add(uuid);
					Bukkit.getScheduler().scheduleSyncDelayedTask(game, () -> sugar.remove(uuid), 100);
				}
			}
			if (bukkitPlayer.getInventory().getItemInMainHand().getType() == Material.TNT) {
				Entity tnt = bukkitPlayer.getWorld().spawnEntity(bukkitPlayer.getLocation(), EntityType.PRIMED_TNT);
				tnt.setVelocity(bukkitPlayer.getLocation().getDirection().multiply(2));
				ItemStack item = bukkitPlayer.getInventory().getItemInMainHand();
				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
					bukkitPlayer.updateInventory();
				} else {
					bukkitPlayer.getInventory().remove(item);
				}
			}
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
				Vector newVelocity = bukkitPlayer.getLocation().getDirection().multiply(.3);
				newVelocity.setY(1);
				bukkitPlayer.setVelocity(bukkitPlayer.getVelocity().add(newVelocity));
				doublej.add(uuid);
			}
			if (LocationUtil.isOnGround(bukkitPlayer)) {
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (game.getArenaManager().isArenaWorld(event.getLocation().getWorld())) {
			if (event.getEntity() instanceof Fireball || event.getEntity() instanceof TNTPrimed) {
				Location l = event.getLocation();
				l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 3, false, false);
				event.setCancelled(true);
			}
		}
	}

	private void explodePlayers(Player bukkitPlayer) {
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		if (player.isPlaying()) {
			Location l = bukkitPlayer.getLocation();
			l.add(0, -1, 0);
			for (int x = l.getBlockX() - 1; x <= l.getBlockX() + 1; x++) {
				for (int z = l.getBlockZ() - 1; z <= l.getBlockZ() + 1; z++) {
					explodeBlocks(bukkitPlayer, new Location(l.getWorld(), x, l.getBlockY(), z));
				}
			}
			for (Entity pl : bukkitPlayer.getWorld().getEntities()) {
				if (pl != bukkitPlayer) {
					Location l2 = pl.getLocation();
					double d = pl.getLocation().distance(bukkitPlayer.getLocation());
					if (d < 5) {
						d = d / 5;
						pl.setVelocity(LocationUtil.getVector(bukkitPlayer, pl).multiply(1.5 - d));

					}
				}
			}
		}
	}

	private void explodeBlocks(Player bukkitPlayer, Location baseLoc) {
		Location aboveLoc = baseLoc.add(0, 1, 0);
		if (baseLoc.getBlock().getState().getType() != Material.AIR &&
				aboveLoc.getBlock().getState().getType() == Material.AIR) {
			final Entity entity = baseLoc.getWorld().spawnFallingBlock(aboveLoc, baseLoc.getBlock().getBlockData());
			entity.setVelocity(LocationUtil.getVector(bukkitPlayer, entity));
			Bukkit.getScheduler().scheduleSyncDelayedTask(game, entity::remove, 5);
		}
	}
}
