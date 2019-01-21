/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {
	public static void send(Player p, String msg){
		p.sendMessage(ChatColor.GOLD + "[" + ChatColor.YELLOW + "SCB" + ChatColor.GOLD + "] " + msg);
	}
}
