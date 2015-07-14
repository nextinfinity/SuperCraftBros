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
				int i = Integer.parseInt(args[0]);
				GameManager.getInstance().getGame(i).disable();
				FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
				system.set("system.arenas." + i + ".enabled", false);
				Message.send(player, ChatColor.YELLOW + "Arena " + i + " disabled!");
			}
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
