package net.nextinfinity.supercraftbros.util;

import org.bukkit.ChatColor;

public class ColorUtil {

	private final static double high_cutoff = 120.0;
	private final static double low_cutoff = 60.0;

	public static ChatColor getColor(double damage) {
		if (damage > high_cutoff) {
			return ChatColor.DARK_RED;
		} else if (damage > low_cutoff) {
			return ChatColor.RED;
		} else {
			return ChatColor.WHITE;
		}
	}
}
