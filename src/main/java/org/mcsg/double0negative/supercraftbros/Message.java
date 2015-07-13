package org.mcsg.double0negative.supercraftbros;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {
	public static void send(Player p, String msg){
		p.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "SCB" + ChatColor.GOLD + "] " + msg);
	}
}
