/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.util.Message;
import net.nextinfinity.supercraftbros.SettingsManager;

public class EnableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			if(args.length == 1){
				FileConfiguration spawns = SettingsManager.getInstance().getSpawns();
				FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
				String game = args[0].toLowerCase();
				try{
					if(spawns.isSet("spawns." + game + ".1.x")){
						if(system.isSet("system.lobby.spawn.x")){
							if(spawns.isSet("spawns." + game + ".lobby.world")){
								GameManager.getInstance().getGame(game).enable();
								system.set("system.arenas." + game + ".enabled", true);
								SettingsManager.getInstance().saveSystemConfig();
								Message.send(player, ChatColor.GREEN + "Arena " + game.toUpperCase() + " enabled!");
							}else{
								Message.send(player, ChatColor.RED  + "No game lobby set!");
							}
						}else{
							Message.send(player, ChatColor.RED + "No lobby set!");
						}
					}else{
						Message.send(player, ChatColor.RED + "No arena spawns set!");
					}
				}catch(NullPointerException e){
					Message.send(player, ChatColor.RED + "That arena doesn't exist!");
				}
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
