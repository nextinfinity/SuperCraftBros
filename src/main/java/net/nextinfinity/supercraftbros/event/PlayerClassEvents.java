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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

//TODO: Clean this class up
public class PlayerClassEvents implements Listener {

	private final Game game;

	private final ArrayList<UUID> fire = new ArrayList<>();
	private final ArrayList<UUID> sugar = new ArrayList<>();
	private final ArrayList<UUID> doublej = new ArrayList<>();
	private final ArrayList<UUID> fsmash = new ArrayList<>();

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
				Location loc = bukkitPlayer.getLocation();
				Entity tnt = bukkitPlayer.getWorld().spawnEntity(bukkitPlayer.getLocation(), EntityType.PRIMED_TNT);
				tnt.setVelocity(bukkitPlayer.getLocation().getDirection().multiply(2));

			}
		}
	}

	private void explodePlayers(Player bukkitPlayer) {
		GamePlayer player = game.getPlayerHandler().getPlayer(bukkitPlayer);
		if (player.isPlaying()) {
			Location l = bukkitPlayer.getLocation();
			l = l.add(0, -1, 0);
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
						pl.setVelocity(new Vector((1.5 - d) * getSide(l2.getBlockX(), l.getBlockX()), 1.5 - d,
								(1.5 - d) * getSide(l2.getBlockZ(), l.getBlockZ())));

					}
				}
			}
		}
	}

	private void explodeBlocks(Player bukkitPlayer, Location baseLoc) {
		Location playerLoc = bukkitPlayer.getLocation();
		Location aboveLoc = baseLoc.add(0, 1, 0);
		if (baseLoc.getBlock().getState().getType() != Material.AIR &&
				aboveLoc.getBlock().getState().getType() == Material.AIR) {
			final Entity entity = baseLoc.getWorld().spawnFallingBlock(aboveLoc, baseLoc.getBlock().getBlockData());
			entity.setVelocity(new Vector((getSide(baseLoc.getBlockX(), playerLoc.getBlockX()) * 0.3), .1,
					(getSide(baseLoc.getBlockZ(), playerLoc.getBlockZ()) * 0.3)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(game, entity::remove, 5);
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
				newVelocity.setY(1);
				bukkitPlayer.setVelocity(newVelocity);
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


	//TODO: Example: This utility method probably shouldn't be in a listener class
	private int getSide(int i, int u) {
		return Integer.compare(i, u);
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
}
