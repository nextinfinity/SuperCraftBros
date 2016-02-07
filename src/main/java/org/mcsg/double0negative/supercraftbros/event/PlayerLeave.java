/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mcsg.double0negative.supercraftbros.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class PlayerLeave implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void leave(PlayerQuitEvent e){
    	String game = GameManager.getInstance().getPlayerGameId(e.getPlayer());
		if(!(game == null)){
			GameManager.getInstance().getGame(game).removePlayer(e.getPlayer(), true);
		}
    }
}
