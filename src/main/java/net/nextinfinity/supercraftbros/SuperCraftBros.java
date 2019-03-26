package net.nextinfinity.supercraftbros;

import net.nextinfinity.core.Game;
import net.nextinfinity.supercraftbros.event.*;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import net.nextinfinity.supercraftbros.util.SCBSettings;
import org.bukkit.Bukkit;

public class SuperCraftBros extends Game {

	@Override
	public void setup() {
		loadSCBSettings();
		loadSCBListeners();
		getPlayerHandler().setPlayerClass(SCBPlayer.class);
		setCommand("scb");
	}

	private void loadSCBListeners() {
		Bukkit.getPluginManager().registerEvents(new GameStart(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerClassEvents(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamage(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLeave(), this);
	}

	private void loadSCBSettings() {
		SCBSettings.setLives(getConfig().getInt("scb.lives"));
	}

}
