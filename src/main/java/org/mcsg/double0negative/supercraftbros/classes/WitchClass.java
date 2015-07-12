package org.mcsg.double0negative.supercraftbros.classes;

import java.util.Random;








import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.util.Colorizer;

public class WitchClass extends PlayerClassBase{

	int sugar = 0;


	public WitchClass(Player p) {
		super(p);
	}

	@Override 
	public void PlayerSpawn(){
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80000, 4));
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 80000, 4));

		PlayerInventory i = player.getInventory();
		i.clear();

		i.setHelmet(Colorizer.setColor(new ItemStack(Material.LEATHER_HELMET), 148, 52, 169));
		i.setChestplate(Colorizer.setColor(new ItemStack(Material.LEATHER_CHESTPLATE), 148, 52, 169));

		ItemStack legs = Colorizer.setColor(new ItemStack(Material.LEATHER_LEGGINGS),  148, 52, 169);
		legs.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		i.setLeggings(legs);

		ItemStack boot = Colorizer.setColor(new ItemStack(Material.LEATHER_BOOTS),   9, 141, 40);
		boot.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
		i.setBoots(boot);

		ItemStack i1 = new ItemStack(Material.EMERALD);
		i1.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
		i.addItem(i1);



		ItemStack i2 = new ItemStack(373, 5, (short)16420);
		i.addItem(i2);

		i.addItem(new ItemStack(Material.SUGAR));


		player.updateInventory();
	}

	@Override
	public void PlayerAttack(Player victim){
		Random r = new Random();
		int i = r.nextInt(20);
		if(i == 5){
			victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
		}		
		if(i == 6){
			victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 1000));
		}
		if(i == 7){
			victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 5));
		}
	}

	public void PlayerInteract(Action a){
		if(sugar < 2){
			if(player.getItemInHand().getType() == Material.SUGAR && ( a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)){
				player.setVelocity(new Vector(0, 2, 0));
				sugar++;
			}
			if(player.getItemInHand().getType() == Material.SUGAR && ( a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)){
				System.out.println(player.getVelocity());
				player.setVelocity(player.getLocation().getDirection().multiply(4));
				System.out.println(player.getVelocity());

				sugar++;
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
			public void run(){
				sugar--;
			}
		}, 400);
	}

	public void PlayerMove(){
		super.PlayerMove();
		if(player.isSneaking()){
			player.setVelocity(new Vector(0, -0.005,0));
		}
	}

	public void Smash(){
		int id = GameManager.getInstance().getPlayerGameId(player);
		if(id != -1){
			Game g = GameManager.getInstance().getGame(id);
			for(Player p: g.getActivePlayers()){
				final Player pl = player;
				pl.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1000000));

				Bukkit.getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
					public void run(){
						Location l = pl.getLocation();
						pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1000, 1));
						//SendPacketToAll(new PacketPlayOutWorldEvent(2002,l.getBlockX(), l.getBlockY()+1, l.getBlockZ(), 0, false));
					}
				}, 20);
			}
		}

	}

	public String getName(){
		return "Witch";
	}

	public WitchClass newInstance(Player p){
		return new WitchClass(p);
	}


}
