package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class WitherClass extends PlayerClassBase{

	
	public WitherClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	public void PlayerAttack(Player victim){
		player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 3, 4));

	}
	
	public void PlayerShootArrow(Entity pro){
		
	}
	
	
	@SuppressWarnings("deprecation")
	@Override 
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 4));

		PlayerInventory i = player.getInventory();
		i.clear();
	
		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 50, 50, 50));
		
		ItemStack chest = Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 50, 50, 50);
		chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		i.setChestplate(chest);
		
		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 50, 50, 50);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);
		
		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  50, 50, 50);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		i.setBoots(boot);
				
		
		ItemStack i1 = new ItemStack(Material.BOW);
		i1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		i1.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		i.addItem(i1);
		
		i.addItem(new ItemStack(Material.ARROW));
		
		
		player.updateInventory();
		
		
	}
	
	public WitherClass newInstance(Player p){
		return new WitherClass(p);
	}
		
	public String getName(){
		return "Wither";
	}
		
		
		
	
}
