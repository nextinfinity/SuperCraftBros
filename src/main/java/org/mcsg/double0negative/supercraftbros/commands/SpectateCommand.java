package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class SpectateCommand implements SubCommand{

	public boolean onCommand(Player player, String[] args){
		Player p = player;
		if(GameManager.getInstance().isSpectating(p)) GameManager.getInstance().getGamePlayer(p).removeSpectator(p);
		if(args[0] != null){
			String i = args[0].toLowerCase();
			FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
			if(c.getBoolean("system.arenas." + i + ".enabled")){
				GameManager.getInstance().addSpectator(p, i);
			}else{
				Message.send(p, ChatColor.RED + "Arena is disabled!");
			}
		}else{
			Message.send(p, "/scb spectate [arena]");
		}
		return true;
	}
	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
