package net.nextinfinity.supercraftbros;

import net.nextinfinity.core.Game;
import net.nextinfinity.supercraftbros.event.PlayerClassEvents;
import net.nextinfinity.supercraftbros.event.PlayerDamage;
import net.nextinfinity.supercraftbros.event.PlayerDeath;
import net.nextinfinity.supercraftbros.player.SCBPlayer;
import net.nextinfinity.supercraftbros.util.SCBSettings;
import org.bukkit.Bukkit;

public class SuperCraftBros extends Game {

	@Override
	public void setup() {
		loadSCBSettings();
		loadSCBListeners();
		getPlayerHandler().setPlayerClass(SCBPlayer.class);
	}

	private void loadSCBListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerClassEvents(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamage(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
	}

	private void loadSCBSettings() {
		SCBSettings.setLives(getConfig().getInt("scb.lives"));
	}

}
