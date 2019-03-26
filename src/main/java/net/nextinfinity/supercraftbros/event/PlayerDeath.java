package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.player.GamePlayer;
import net.nextinfinity.core.events.GameDeathEvent;
import net.nextinfinity.core.team.Team;
import net.nextinfinity.core.utils.Settings;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeath implements Listener {

	@EventHandler
	public void onDeath(GameDeathEvent event) {
		SCBPlayer player = (SCBPlayer) event.getPlayer();
		Team team = player.getTeam();
		team.setScore(team.getScore() - 1);
		for (GamePlayer gamePlayer : player.getArena().getPlayers()) {
			gamePlayer.sendMessage(Settings.getSecondary() + player.getBukkitPlayer().getName() + " died! " +
					"They have " + team.getScore() + " lives left!");
		}
		if (team.getScore() > 0) {
			player.setDamage(0.0);
			player.getArena().spawnPlayer(player);
			player.initializeClass();
		} else {
			player.getArena().removePlayer(player);
		}
	}
}
