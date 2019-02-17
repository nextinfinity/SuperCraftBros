package net.nextinfinity.supercraftbros.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LocationUtil {

	public static boolean isOnGround(Player bukkitPlayer) {
		Location l = bukkitPlayer.getLocation();
		l = l.add(0, -1, 0);
		return l.getBlock().getState().getType() != Material.AIR;
	}

}
