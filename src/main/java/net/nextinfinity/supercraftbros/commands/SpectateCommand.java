package net.nextinfinity.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.util.Message;
import net.nextinfinity.supercraftbros.SettingsManager;

public class SpectateCommand implements SubCommand{

	public boolean onCommand(Player player, String[] args){
		Player p = player;
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
