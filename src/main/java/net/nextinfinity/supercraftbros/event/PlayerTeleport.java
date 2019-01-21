/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.event;

import net.nextinfinity.supercraftbros.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import net.nextinfinity.supercraftbros.GameManager;

public class PlayerTeleport implements Listener{


	@EventHandler(priority = EventPriority.HIGHEST)
	public void teleportHandler(PlayerTeleportEvent e){
		String game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
		if(!(game == null)){
			Game g = GameManager.getInstance().getGame(game);
			if(g.getState() == Game.State.LOBBY){
				e.setCancelled(true);
			}

		}
	}
}

