/*
 * Copyright (c) 2016, Justin W. Flory and others
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mcsg.double0negative.supercraftbros;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.event.*;

import java.util.HashMap;

/**
 * Main class for SuperCraftBros project.
 *
 * This plugin intends to emulate the classic console title, Super Smash BrothersⒸ title in a Spigot server. Players are placed
 * head-to-head in arenas with a variety of custom classes to choose from at their disposal. Last man standing wins. All players
 * have a fixed number of lives.
 *
 * @author Drew             <Double0negative>
 * @author Ian Ryan         <nextinfinity>
 * @author Justin W. Flory  <jflory7>
 * @version 1.0
 */
public class SuperCraftBros extends JavaPlugin {

	public static HashMap<Location, Integer> joinSigns = new HashMap<Location, Integer>();

    /**
     * Behavior implemented on plugin start-up.
     */
	public void onEnable() {
		SettingsManager.getInstance().setup(this);
		GameManager.getInstance().setup(this);

		// Register all event listeners
		this.getServer().getPluginManager().registerEvents(new BreakBlock(), this);
		this.getServer().getPluginManager().registerEvents(new SignEvents(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerClassEvents(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
		this.getServer().getPluginManager().registerEvents(new InventoryEvents(), this);

        // Register command executor
		this.getCommand("scb").setExecutor(new CommandHandler(this));

        // Checks that all online players meet the requirements for a new SCB lobby and game
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.setVelocity(new Vector(0, 0, 0));
			p.teleport(SettingsManager.getInstance().getLobbySpawn());
			p.getInventory().clear();
			p.getInventory().setArmorContents(new ItemStack[4]);
			p.updateInventory();
			for (PotionEffectType e : PotionEffectType.values()){
				if (e != null && p.hasPotionEffect(e))
					p.removePotionEffect(e);
			}
		}
	}

    /**
     * Behavior implemented on plugin shut-down.
     */
	public void onDisable() {
		for(Game g:GameManager.getInstance().getGames()) {
			g.disable();
		}
		GameManager.getInstance().saveSigns();
	}

    /**
     * Listener to detect WorldEdit plugin used on the server.
     *
     * @return a WorldEditPlugin instance
     */
	public WorldEditPlugin getWorldEdit() {
		Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit instanceof WorldEditPlugin) {
			return (WorldEditPlugin) worldEdit;
		} else {
			return null;
		}
	}
}
