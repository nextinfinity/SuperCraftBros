/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.Game;
import net.nextinfinity.core.arena.GameState;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

	private final Game game;

	public PlayerDamage(Game game) {
		this.game = game;
	}

	/**
	 * If the player is damaged by the environment, kill them in void, otherwise cancel damage
	 * @param event
	 */
	@EventHandler
	public void playerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player bukkitPlayer = (Player) event.getEntity();
			SCBPlayer player = (SCBPlayer) game.getPlayerHandler().getPlayer(bukkitPlayer);
			if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
				if (bukkitPlayer.getLocation().getBlockY() < 0) {
					if (bukkitPlayer.getHealth() > 0) {
						bukkitPlayer.damage(bukkitPlayer.getHealth());
					}
				} else {
					event.setDamage(0);
				}
			}

		}
	}

	/**
	 * If the player is damaged by another player, give them percent damage and knockback
	 * @param event
	 */
	@EventHandler
	public void playerDamagePlayer(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player bukkitPlayer = (Player) event.getEntity();
			SCBPlayer player = (SCBPlayer) game.getPlayerHandler().getPlayer(bukkitPlayer);
			if (player.isPlaying() && player.getArena().getState() == GameState.INGAME) {
				player.damage(Math.pow(event.getFinalDamage(), 2));
				double multiplier = Math.pow(player.getDamage() / 100, 2);
				bukkitPlayer.setVelocity(event.getDamager().getLocation().getDirection().multiply(multiplier));
				event.setDamage(0);
			}

		}
	}
}
