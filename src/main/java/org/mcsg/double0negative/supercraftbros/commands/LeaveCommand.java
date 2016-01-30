package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class LeaveCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		String game = GameManager.getInstance().getPlayerGameId(player);
		if(!(game == null)){
			GameManager.getInstance().getGame(game).removePlayer(player, false);
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
}
