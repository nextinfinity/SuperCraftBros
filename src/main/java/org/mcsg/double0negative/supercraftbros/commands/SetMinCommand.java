/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros.commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class SetMinCommand implements SubCommand{
	FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
	
	public boolean onCommand(Player player, String[] args) {
		int no = Integer.parseInt(args[0]);
		int i = Integer.parseInt(args[1]);
		c.set("system.arenas." + no + ".min", i);
		SettingsManager.getInstance().saveSystemConfig();
		Message.send(player, "Minimum amount for arena " + no + " set to " + i + "!");
		return true;
	}
	
	public String help(Player p) {
		// TODO Auto-generated method stub
		return null;
	}
}
