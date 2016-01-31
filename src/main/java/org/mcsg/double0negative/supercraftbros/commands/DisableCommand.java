/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class DisableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			if(args.length == 1){
				String game = args[0].toLowerCase();
				GameManager.getInstance().getGame(game).disable();
				FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
				system.set("system.arenas." + game + ".enabled", false);
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
