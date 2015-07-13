package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand{
	public boolean onCommand(Player player, String[] args) {
		Player p = player;
		p.sendMessage(ChatColor.GOLD  + "/scb join <number> - Join an arena");
		p.sendMessage(ChatColor.GOLD  + "/scb leave - Leave a game");
		p.sendMessage(ChatColor.GOLD  + "/scb start - Start a game");
		p.sendMessage(ChatColor.GOLD  + "/scb createarena - Create a new arena with the current WorldEdit selection");
		p.sendMessage(ChatColor.GOLD  + "/scb setspawn next - Adds a spawn to the arena you're in");
		p.sendMessage(ChatColor.GOLD  + "/scb setlobbyspawn - Sets the scb lobby");
		p.sendMessage(ChatColor.GOLD  + "/scb set lobby <number> - Set the lobby for an arena");
		p.sendMessage(ChatColor.GOLD  + "");
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}
}
