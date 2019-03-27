package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.core.events.PlayerLeaveArenaEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeave implements Listener {

	@EventHandler
	public void onLeave(PlayerLeaveArenaEvent event) {
		if (event.getPlayer().getTeam() != null) {
			event.getPlayer().getTeam().setScore(0);
		}
	}
}
