/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import org.bukkit.entity.Player;
import net.nextinfinity.supercraftbros.Game;
import net.nextinfinity.supercraftbros.GameManager;

public class LeaveCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		Game game = GameManager.getInstance().getGamePlayer(player);
		if(!(game == null)){
			if(game.isPlaying(player)){
				game.removePlayer(player, false);
			}else{
				game.removeSpectator(player);
			}
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
}
