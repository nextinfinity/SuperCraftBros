/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.nextinfinity.supercraftbros.util.Message;
import net.nextinfinity.supercraftbros.SettingsManager;

public class SetLobbyGameSpawn implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			if(args.length == 1){
				String i = args[0].toLowerCase();
				SettingsManager.getInstance().setGameLobbySpawn(i, player.getLocation());
				Message.send(player, ChatColor.GREEN + "Lobby for arena "+ i.toUpperCase() +" set!");
			}else{
				Message.send(player, ChatColor.RED + "/scb setlobby <arena>");
			}
		}else{
			Message.send(player, ChatColor.RED + "You don't have permission for that!");
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
