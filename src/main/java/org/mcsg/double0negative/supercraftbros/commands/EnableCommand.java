package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class EnableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.isOp()){
			
			if(args.length == 1){
				int i = Integer.parseInt(args[0]);
				GameManager.getInstance().getGame(i).enable();
			}
			
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
