package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.player.GamePlayer;
import net.nextinfinity.core.events.GameStartEvent;
import net.nextinfinity.supercraftbros.util.SCBSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GameStart implements Listener {

	@EventHandler
	public void onStart(GameStartEvent event) {
		List<GamePlayer> players = new ArrayList<>(event.getArena().getPlayers());
		for (GamePlayer player : players) {
			player.getTeam().setColor(player.getGameClass().getColor());
			player.getTeam().setScore(SCBSettings.getLives());
		}
	}
}
