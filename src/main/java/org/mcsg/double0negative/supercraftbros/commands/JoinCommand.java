package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class JoinCommand implements SubCommand{
	
	public boolean onCommand(Player player, String[] args) {
		Player p = player;
		int i = Integer.parseInt(args[0]);
		GameManager.getInstance().addPlayer(p, i);
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
