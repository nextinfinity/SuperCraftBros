/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.Message;

public class HelpCommand implements SubCommand{
	public boolean onCommand(Player player, String[] args) {
		Player p = player;
		Message.send(p, ChatColor.GOLD  + "/scb join <number> - Join an arena");
		Message.send(p, ChatColor.GOLD  + "/scb leave - Leave a game");
		Message.send(p, ChatColor.GOLD  + "/scb start - Start a game");
		Message.send(p, ChatColor.GOLD  + "/scb createarena - Create a new arena with the current WorldEdit selection");
		Message.send(p, ChatColor.GOLD  + "/scb setspawn next - Adds a spawn to arena you're in");
		Message.send(p, ChatColor.GOLD  + "/scb setlobbyspawn - Sets the scb lobby");
		Message.send(p, ChatColor.GOLD  + "/scb set lobby <number> - Set the lobby for an arena");
		Message.send(p, ChatColor.GOLD  + "/scb enable <number> - Enable an arena");
		Message.send(p, ChatColor.GOLD  + "/scb disable <number> - Disable an arena");
		return true;
	}

	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}
}
