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

public class JoinCommand implements SubCommand{
	
	public boolean onCommand(Player player, String[] args) {
		Player p = player;
		if(args[0] != null){
			int i = Integer.parseInt(args[0]);
			FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
			if(c.getBoolean("system.arenas." + i + ".enabled")){
				GameManager.getInstance().addPlayer(p, i);
			}else{
				Message.send(p, ChatColor.RED + "Arena is disabled!");
			}
		}else{
			Message.send(p, "/scb join <arena>");
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
