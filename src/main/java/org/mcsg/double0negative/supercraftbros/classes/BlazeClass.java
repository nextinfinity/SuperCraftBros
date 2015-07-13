package org.mcsg.double0negative.supercraftbros.classes;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class BlazeClass extends PlayerClassBase {

	public BlazeClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 4));

		PlayerInventory i = player.getInventory();
		i.clear();

		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 255, 144, 0));
		i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 255, 144, 0));

		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS),  255, 144, 0);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);

		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  255, 144, 0);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 10);
		i.setBoots(boot);

		ItemStack i1 = new ItemStack(Material.BOW);
		i1.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
		i1.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
		i1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		i1.addUnsafeEnchantment(Enchantment.DURABILITY, 2);

		i.addItem(i1);

		ItemStack i2 = new ItemStack(Material.BLAZE_ROD, 1);
		i2.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		i2.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		i.addItem(i2);

		i.addItem(new ItemStack(Material.ARROW, 1));




		player.updateInventory();

	}

	public BlazeClass newInstance(Player p){
		return new BlazeClass(p);
	}

	public String getName(){
		return "Blaze";
	}

	public void Smash(){
		smash = true;
		//final Location l = player.getLocation();
		final Random r = new Random();
		
		final int [] i1 = {2, 1, 0, -1, -2, -1, 0, 1, 2, 1, 0, -1, -2, -1, 0, 1, 2, 1, 0, -1};
		
		for(int a = 0; a < 60; a++){
			final int b = a;
			Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
				public void run(){
					Location l = player.getLocation();
					Location l2 = l;
					l2.add(r.nextInt(7) - 3, 10 + r.nextInt(4) - 7, r.nextInt(7) - 3);
					Location l3 = l;
					Fireball f = l.getWorld().spawn(l2, Fireball.class);
					f.setVelocity(new Vector(0, -2, 0));
					
					if(b < 20){
					  Entity tnt1 = l2.getWorld().spawn(l3.add(i1[b],b,i1[b]), TNTPrimed.class);
				        ((TNTPrimed)tnt1).setFuseTicks(5);
		        		l.getWorld().createExplosion(l3.getX(), l3.getY(), l3.getZ(), 2, false, true);

					}
				}
			}, a);
		}

		
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			 public void run(){
				 smash = false;
			 }
		}, 400);

		PlayerSpawn();

	}

}
