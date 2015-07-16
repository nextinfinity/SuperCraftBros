package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class EnableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			
			if(args.length == 1){
				FileConfiguration spawns = SettingsManager.getInstance().getSpawns();
				FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
				int i = Integer.parseInt(args[0]);
				try{
					if(spawns.isSet("spawns." + i + ".1.x")){
						if(system.isSet("system.lobby.spawn.x")){
							if(spawns.isSet("spawns." + i + ".lobby.world")){
								GameManager.getInstance().getGame(i).enable();
								system.set("system.arenas." + i + ".enabled", true);
								Message.send(player, ChatColor.GREEN + "Arena " + i + " enabled!");
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
