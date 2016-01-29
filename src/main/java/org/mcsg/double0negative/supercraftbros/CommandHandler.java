/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mcsg.double0negative.supercraftbros.commands.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;


public class CommandHandler implements CommandExecutor
{
	@SuppressWarnings("unused")
	private Plugin plugin;
	private HashMap<String, SubCommand> commands;

	public CommandHandler(Plugin plugin)
	{
		this.plugin = plugin;
		commands = new HashMap<String, SubCommand>();
		loadCommands();
	}

	private void loadCommands()
	{
		commands.put("join", new JoinCommand());
		commands.put("createarena", new CreateArenaCommand());
		commands.put("disable", new DisableCommand());
		commands.put("enable", new EnableCommand());
		commands.put("setlobbyspawn", new SetLobbySpawnCommand());
		commands.put("setlobby", new SetLobbyGameSpawn());
		commands.put("setmin", new SetMinCommand());
		commands.put("setmax", new SetMaxCommand());
		commands.put("setspawn", new SetSpawnCommand());
		commands.put("leave", new LeaveCommand());
		commands.put("start", new StartCommand());
		commands.put("help", new HelpCommand());

	}

	public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args){
		String cmd = cmd1.getName();

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		else{
			System.out.println("Only ingame players can use Super Craft Bros commands");
			return true;
		}


		if(cmd.equalsIgnoreCase("scb")){ 
			if(player.hasPermission("scb.player")){
				if(args == null || args.length < 1){
					Message.send(player, ChatColor.GOLD +""+ ChatColor.BOLD +"Super Craft Bros Reloaded by NextInfinity");
					Message.send(player, ChatColor.GOLD +""+ ChatColor.BOLD +"Original source by double0negative");
					Message.send(player, ChatColor.GOLD +""+ ChatColor.BOLD +"Modified for Minecraft 1.8");
					Message.send(player, ChatColor.GOLD +""+ ChatColor.BOLD +"Type /scb help for commands");
					//Message.send(player, ChatColor.GOLD +"Type /scb help for help" );

					return true;
				}
				String sub = args[0];

				Vector<String> l  = new Vector<String>();
				l.addAll(Arrays.asList(args));
				l.remove(0);
				args = l.toArray(new String[0]);
				if(!commands.containsKey(sub)){
					Message.send(player, ChatColor.RED+"Command dosent exist.");
					Message.send(player, ChatColor.GOLD +"Type /scb help for help" );
					return true;
				}
				try{

					commands.get(sub).onCommand( player,  args);
				}catch(Exception e){e.printStackTrace(); Message.send(player, ChatColor.RED+"An error occured while executing the command. Check the      console");                Message.send(player, ChatColor.BLUE +"Type /scb help for help" );
				}
				return true;
			}
		}
		return false;
	}
}