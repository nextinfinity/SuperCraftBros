package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class SetLobbyGameSpawn implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			
			if(args.length == 1){
				int i = Integer.parseInt(args[0]);
				SettingsManager.getInstance().setGameLobbySpawn(i, player.getLocation());
			}
			
			
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
