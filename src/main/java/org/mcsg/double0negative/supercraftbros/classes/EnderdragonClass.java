package org.mcsg.double0negative.supercraftbros.classes;


import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.GameManager;

public class EnderdragonClass extends PlayerClassBase{

	public EnderdragonClass(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	int fly = 0;
	boolean canFly = false;
	
	public EnderdragonClass newInstance(Player p){
		return new EnderdragonClass(p);
	}
	
	public String getName(){
		return "Ender Dragon";
	}

	
	public void PlayerMove(){
		super.PlayerMove();
		if(fsmash){
			return;
		}
		if(player.isSneaking()){
			if(fly < 20 && canFly){
				player.setVelocity(player.getLocation().getDirection().multiply(2));
				Random r = new Random();
				double h = player.getHealth();
				player.setHealth(player.getMaxHealth());
				player.getWorld().createExplosion(player.getLocation().add(r.nextInt(6) - 3, r.nextInt(6) - 3, r.nextInt(6) - 3), 2);
				player.setHealth(h);
				fly++;
			}
			else{
				canFly = false;
			}

			
		}else if(fly > -30 && !canFly){
			fly--;
		}
		if(fly == -30){
			canFly = true;
			fly = 0;
		}
		System.out.println(fly);
	}
	
	int stid = 0;
	public void Smash(){
		smash = true;
		final long time = new Date().getTime();
		stid = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameManager.getInstance().getPlugin(), new Runnable(){
			public void run(){
				Random r = new Random();
				player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				player.getWorld().createExplosion(player.getLocation().add(r.nextInt(6) - 3, r.nextInt(6) - 3, r.nextInt(6) - 3), 2);
				if(new Date().getTime() > time + 5000){
					Bukkit.getScheduler().cancelTask(stid);
					smash = false;

				}
			}
		}, 0, 1);
		
		
	}
	
	
}
