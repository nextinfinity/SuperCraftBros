package org.mcsg.double0negative.supercraftbros.classes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class GhastClass extends PlayerClassBase{

	public GhastClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("deprecation")
	@Override 
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 88000, 4));

		PlayerInventory i = player.getInventory();
		i.clear();

		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET),250,0,0));
		
		ItemStack chest = Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 250, 250, 250);
		chest.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 10);
		i.setChestplate(chest);

		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 250, 250, 250);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);

		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  250, 250, 250);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		i.setBoots(boot);


		ItemStack i1 = new ItemStack(Material.GHAST_TEAR);
		i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		i1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
		i.addItem(i1);

		i.addItem(new ItemStack(Material.FIREBALL));

		player.updateInventory();

	}

	public void PlayerInteract(Action a){
		if(player.getItemInHand().getType() == Material.FIREBALL){
			Fireball e = player.launchProjectile(Fireball.class);
			e.setVelocity(e.getVelocity().multiply(10));
		}
	}
	
	@Override
	public GhastClass newInstance(Player p){
		return new GhastClass(p);
	}
	
	public String getName(){
		return "Ghast";
	}

}
