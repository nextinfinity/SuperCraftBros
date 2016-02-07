/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class DeleteArenaCommand implements SubCommand{
	

	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
			if(args.length >= 1){
				String name = args[0].toLowerCase();
				if(c.contains("system.arenas." + name)){
					c.set("system.arenas." + name, null);
					SettingsManager.getInstance().getSpawns().set("spawns." + name, null);
					GameManager.getInstance().hotRemoveArena(name);
					SettingsManager.getInstance().saveSystemConfig();
				}
				Message.send(player, "Arena " + name.toUpperCase() + " removed!");
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
