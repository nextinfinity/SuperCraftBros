package org.mcsg.double0negative.supercraftbros.classes;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class CreeperClass extends PlayerClassBase{

	public CreeperClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void PlayerPlaceBlock(Block b){
		
		if(b.getTypeId() == 46){
			   final Entity tnt1 = b.getWorld().spawn(b.getLocation(), TNTPrimed.class);
		        ((TNTPrimed)tnt1).setFuseTicks(31);
		        
		        Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
		        	public void run(){
		        		Location l = tnt1.getLocation();
		        		l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 6, false, false);
		        		tnt1.remove();
		        		
		        	}
		        }, 30);
		        
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override 
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 8000, 2));

		PlayerInventory i = player.getInventory();
		i.clear();
		
		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 0, 200, 0));
		i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 0, 200, 0));
		
		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS), 0, 200, 0);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);
		
		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),  0, 0, 0);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
		i.setBoots(boot);
				
		ItemStack i1 = new ItemStack(Material.SULPHUR);
		i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		i.addItem(i1);
		
		i.addItem(new ItemStack(46, 10));

		
		ItemStack i2 = new ItemStack(373, 10, (short)16396);
		i.addItem(i2);
		
		
		player.updateInventory();
	}
	
	public CreeperClass newInstance(Player p){
		return new CreeperClass(p);
	}
	
	public String getName(){
		return "Creeper";
	}
	
}
