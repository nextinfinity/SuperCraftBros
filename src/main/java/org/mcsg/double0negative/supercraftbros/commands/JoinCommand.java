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
		int i = Integer.parseInt(args[0]);
		FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
		if(c.getBoolean("system.arenas." + i + ".enabled")){
			GameManager.getInstance().addPlayer(p, i);
		}else{
			Message.send(p, ChatColor.RED + "Arena is disabled!");
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
