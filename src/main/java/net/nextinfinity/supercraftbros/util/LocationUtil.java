package net.nextinfinity.supercraftbros.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationUtil {

	public static boolean isOnGround(Player bukkitPlayer) {
		Location l = bukkitPlayer.getLocation();
		l = l.add(0, -1, 0);
		return l.getBlock().getState().getType() != Material.AIR;
	}

	public static Vector getVector(Entity damager, Entity victim)  {
		Location baseLoc = damager.getLocation();
		Location playerLoc = victim.getLocation();
		Vector vector = playerLoc.toVector().subtract(baseLoc.toVector());
		vector.normalize();
		vector.add(new Vector(0, .1, 0));
		return vector;
	}

}
