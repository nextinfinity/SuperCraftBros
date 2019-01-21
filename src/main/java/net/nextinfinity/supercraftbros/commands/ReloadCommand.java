/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.SettingsManager;
import net.nextinfinity.supercraftbros.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			SettingsManager s = SettingsManager.getInstance();
			s.reloadClasses();
			s.reloadSigns();
			s.reloadSpawns();
			s.reloadSystem();
			GameManager.getInstance().setup();
			Message.send(player, "SCB reloaded!");
			return true;
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
