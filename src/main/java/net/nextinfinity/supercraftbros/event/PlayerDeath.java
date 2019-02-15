package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.entity.GamePlayer;
import net.nextinfinity.core.events.GameDeathEvent;
import net.nextinfinity.core.utils.Settings;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeath implements Listener {

	@EventHandler
	public void onDeath(GameDeathEvent event) {
		SCBPlayer player = (SCBPlayer) event.getPlayer();
		player.setScore(player.getScore() - 1);
		for (GamePlayer gamePlayer : player.getArena().getPlayers()) {
			gamePlayer.sendMessage(Settings.getSecondary() + player.getBukkitPlayer().getName() + " died! " +
					"They have " + player.getScore() + " lives left!");
		}
		if (player.getScore() > 0) {
			player.setDamage(0.0);
			player.getArena().spawnPlayer(player);
			player.initializeClass();
		} else {
			player.getArena().removePlayer(player);
		}
	}
}
