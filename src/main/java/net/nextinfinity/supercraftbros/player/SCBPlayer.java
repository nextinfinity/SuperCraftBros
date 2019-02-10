package net.nextinfinity.supercraftbros.player;

import net.nextinfinity.core.Game;
import net.nextinfinity.core.entity.impl.CoreGamePlayer;
import org.bukkit.entity.Player;

public class SCBPlayer extends CoreGamePlayer {

	private double damage;

	public SCBPlayer(Player player, Game game) {
		super(player, game);
	}

	public double damage(double damageAmount) {
		damage += damageAmount;
		return damage;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	@Override
	public void heal() {
		super.heal();
		damage = 0;
	}
}
