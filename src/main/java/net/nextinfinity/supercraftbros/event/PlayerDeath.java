package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.events.GameDeathEvent;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeath implements Listener {

	@EventHandler
	public void onDeath(GameDeathEvent event) {
		SCBPlayer player = (SCBPlayer) event.getPlayer();
		player.kill();
		if (player.getLives() > 0) {
			player.getArena().spawnPlayer(player);
		} else {
			player.getArena().removePlayer(player);
		}
	}
}
