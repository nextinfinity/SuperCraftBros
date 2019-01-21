/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.nextinfinity.supercraftbros.GameManager;
import net.nextinfinity.supercraftbros.util.Message;

public class CreateArenaCommand implements SubCommand {

	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission("scb.admin")){
			if(args.length >= 1){
				GameManager.getInstance().createArenaFromSelection(player, args[0].toLowerCase());
			}
		}else{
			Message.send(player, ChatColor.RED + "You don't have permission for that!");
		}
		return true;
	}

	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

}
