package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.entity.GamePlayer;
import net.nextinfinity.core.events.GameStartEvent;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import net.nextinfinity.supercraftbros.util.SCBSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameStart implements Listener {

	@EventHandler
	public void onStart(GameStartEvent event) {
		for (GamePlayer player : event.getArena().getPlayers()) {
			((SCBPlayer) player).setLives(SCBSettings.getLives());
		}
	}
}
