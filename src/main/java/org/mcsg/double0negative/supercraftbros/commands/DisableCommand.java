package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;

public class DisableCommand implements SubCommand{

	
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			
			if(args.length == 1){
				int i = Integer.parseInt(args[0]);
				GameManager.getInstance().getGame(i).disable();
				Message.send(player, ChatColor.YELLOW + "Arena " + i + " disabled!");
			}
			else if(args.length == 0){
				for(Game g: GameManager.getInstance().getGames()){
					g.disable();
					Message.send(player, ChatColor.YELLOW + "All arenas disabled!");
				}
			}
			
			
			
			
			
		}
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
