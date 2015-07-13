package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class CactusClass extends PlayerClassBase{





	public CactusClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}


	@SuppressWarnings("deprecation")
	@Override 
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80000, 2));

		PlayerInventory i = player.getInventory();
		i.clear();

		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 50, 150, 50));

		ItemStack chest = Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 50, 150, 50);
		chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		chest.addUnsafeEnchantment(Enchantment.THORNS, 4);
		i.setChestplate(chest);

		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 50, 150, 50);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		i.setLeggings(legs);

		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  50, 150, 250);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		i.setBoots(boot);


		ItemStack i1 = new ItemStack(Material.WOOD_SWORD);
		i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		i1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
		i.addItem(i1);


		player.updateInventory();

	}

	public CactusClass newInstance(Player p){
		return new CactusClass(p);
	}

	@Override
	public void PlayerDamaged() {
		if(player.isSneaking()){
			Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
				public void run(){
				player.setVelocity(new Vector(0, 0, 0));
				}
			}, 1);
		}
	}

	public String getName(){
		return "Cactus";
	}


}
