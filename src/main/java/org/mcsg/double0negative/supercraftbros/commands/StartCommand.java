package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class StartCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.start")){
			int game = GameManager.getInstance().getPlayerGameId(player);
			if(game != -1){
				GameManager.getInstance().getGame(game).countdown(2);
			}
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
