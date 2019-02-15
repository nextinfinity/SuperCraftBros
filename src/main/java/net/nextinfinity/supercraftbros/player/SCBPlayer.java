package net.nextinfinity.supercraftbros.player;

import net.nextinfinity.core.Game;
import net.nextinfinity.core.entity.impl.CoreGamePlayer;
import net.nextinfinity.supercraftbros.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SCBPlayer extends CoreGamePlayer {

	private double damage;

	public SCBPlayer(Player player, Game game) {
		super(player, game);
	}

	public double damage(double damageAmount) {
		setDamage(damage + damageAmount);
		return damage;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
		if (getTeam() != null) {
			getTeam().setSuffix(ChatColor.WHITE + " [" + ColorUtil.getColor(damage) +
					String.format("%.1f", damage) + "%" + ChatColor.WHITE + "]");
			getArena().getScoreboard().refreshScores();
		}
	}

	@Override
	public void heal() {
		super.heal();
		setDamage(0);
	}
}
