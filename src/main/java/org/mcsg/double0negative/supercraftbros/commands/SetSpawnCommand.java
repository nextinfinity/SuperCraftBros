package org.mcsg.double0negative.supercraftbros.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mcsg.double0negative.supercraftbros.Game;
import org.mcsg.double0negative.supercraftbros.GameManager;
import org.mcsg.double0negative.supercraftbros.Message;
import org.mcsg.double0negative.supercraftbros.SettingsManager;

public class SetSpawnCommand implements SubCommand{

    HashMap<Integer, Integer>next = new HashMap<Integer,Integer>();

 

    public void loadNextSpawn(){
        for(Game g:GameManager.getInstance().getGames().toArray(new Game[0])){ //Avoid Coccurency problems
            next.put(g.getID(), SettingsManager.getInstance().getSpawnCount(g.getID())+1);
        }
    }
    
    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("scb.admin")){
            Message.send(player, ChatColor.RED+"No Permission");
            return true;
        }
        loadNextSpawn();
        System.out.println("settings spawn");
        Location l =  player.getLocation();
        int game =  GameManager.getInstance().getBlockGameId(l);
        System.out.println(game+" "+next.size());
        if(game == -1){
            Message.send(player, ChatColor.RED+"Must be in an arena!");
        }
        int i = 0;
        if(args[0].equalsIgnoreCase("next")){
            i = next.get(game);
            next.put(game, next.get(game)+1);
        }
        else{
            try{
            i = Integer.parseInt(args[0]);
            if(i>next.get(game)+1 || i<1){
                Message.send(player, ChatColor.RED+"Spawn must be between 1 & "+next.get(game));
                return true;
            }
            if(i == next.get(game)){
                next.put(game, next.get(game)+1);
            }
            }catch(Exception e){
                Message.send(player, ChatColor.RED+"Malformed input. Must be \"next\" or a number");
                return false;
            }
        }
        if(i == -1){
            Message.send(player, ChatColor.RED+"You must be inside an arnea");
            return true;
        }
        SettingsManager.getInstance().setSpawn(game, i, l.toVector());
        Message.send(player, ChatColor.GREEN+"Spawn "+i +" in arena "+game+" set!");

        return true;
    }
    
    
    public String help(Player p) {
        return "/sg setspawn next- Sets a spawn in the arena you are located in.";
    }
}


