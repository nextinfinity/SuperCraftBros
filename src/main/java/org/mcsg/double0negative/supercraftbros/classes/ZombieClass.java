package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class ZombieClass extends PlayerClassBase{

	public ZombieClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	
	@SuppressWarnings("deprecation")
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 2));

		PlayerInventory i = player.getInventory();
		i.clear();
		
		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 0, 100, 0));
		i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 0, 100, 0));
		
		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 0, 100, 0);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);
		
		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  0, 100, 0);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		i.setBoots(boot);
				
		
		ItemStack i1 = new ItemStack(Material.IRON_SPADE, 1);
		i1.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		i1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
		i.addItem(i1);
		
		player.updateInventory();
	}
	
	public ZombieClass newInstance(Player p){
		return new ZombieClass(p);
	}
	
	public String getName(){
		return "Zombie";
	}
	
}
