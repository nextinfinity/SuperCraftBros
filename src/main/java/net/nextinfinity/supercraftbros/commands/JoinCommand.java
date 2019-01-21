/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.SettingsManager;
import net.nextinfinity.supercraftbros.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class JoinCommand implements SubCommand{
	
	public boolean onCommand(Player player, String[] args) {
		Player p = player;
		if(args[0] != null){
			String i = args[0].toLowerCase();
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
