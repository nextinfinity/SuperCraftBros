/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mcsg.double0negative.supercraftbros;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.event.BreakBlock;
import org.mcsg.double0negative.supercraftbros.event.SignEvents;
import org.mcsg.double0negative.supercraftbros.event.InventoryEvents;
import org.mcsg.double0negative.supercraftbros.event.PlayerClassEvents;
import org.mcsg.double0negative.supercraftbros.event.PlayerDamage;
import org.mcsg.double0negative.supercraftbros.event.PlayerJoin;
import org.mcsg.double0negative.supercraftbros.event.PlayerLeave;
import org.mcsg.double0negative.supercraftbros.event.PlayerTeleport;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SuperCraftBros extends JavaPlugin{

	public static HashMap<Location, String> joinSigns = new HashMap<Location, String>();
	
	public void onEnable(){
		SettingsManager.getInstance().setup(this);
		GameManager.getInstance().setup(this);
		
		this.getServer().getPluginManager().registerEvents(new BreakBlock(), this);
		this.getServer().getPluginManager().registerEvents(new SignEvents(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerClassEvents(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
		this.getServer().getPluginManager().registerEvents(new InventoryEvents(), this);

		this.getCommand("scb").setExecutor(new CommandHandler(this));
		
		for(Player p:Bukkit.getOnlinePlayers()){
			p.setVelocity(new Vector(0, 0, 0));
			p.teleport(SettingsManager.getInstance().getLobbySpawn());
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			p.updateInventory();
			for(PotionEffectType e: PotionEffectType.values()){
				if(e != null && p.hasPotionEffect(e))
					p.removePotionEffect(e);
			}
		}

	}


	public void onDisable(){
		for(Game g:GameManager.getInstance().getGames()){
			g.disable();
		}
		GameManager.getInstance().saveSigns();
	}


	public WorldEditPlugin getWorldEdit() {
		Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit instanceof WorldEditPlugin) {
			return (WorldEditPlugin) worldEdit;
		} else {
			return null;
		}
	}
}
