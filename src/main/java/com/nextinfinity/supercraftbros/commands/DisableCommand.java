/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.nextinfinity.supercraftbros.commands;

import com.nextinfinity.supercraftbros.GameManager;
import com.nextinfinity.supercraftbros.SettingsManager;
import com.nextinfinity.supercraftbros.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DisableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			if(args.length == 1){
				String game = args[0].toLowerCase();
				GameManager.getInstance().getGame(game).disable();
				FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
				system.set("system.arenas." + game + ".enabled", false);
				SettingsManager.getInstance().saveSystemConfig();
				Message.send(player, ChatColor.YELLOW + "Arena " + game.toUpperCase() + " disabled!");
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
