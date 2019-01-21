/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.nextinfinity.supercraftbros.commands;

import java.util.HashMap;

import com.nextinfinity.supercraftbros.Game;
import com.nextinfinity.supercraftbros.GameManager;
import com.nextinfinity.supercraftbros.SettingsManager;
import com.nextinfinity.supercraftbros.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements SubCommand{

    HashMap<String, Integer>next = new HashMap<String,Integer>();

 

    public void loadNextSpawn(){
        for(Game g: GameManager.getInstance().getGames().toArray(new Game[0])){ //Avoid Coccurency problems
            next.put(g.getID(), SettingsManager.getInstance().getSpawnCount(g.getID())+1);
        }
    }
    
    public boolean onCommand(Player player, String[] args) {
        if(!player.hasPermission("scb.admin")){
    		Message.send(player, ChatColor.RED + "You don't have permission for that!");
            return true;
        }
        loadNextSpawn();
        System.out.println("settings spawn");
        Location l =  player.getLocation();
        String game =  GameManager.getInstance().getBlockGameId(l);
        System.out.println(game+" "+next.size());
        if(game == null){
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
        Message.send(player, ChatColor.GREEN+"Spawn "+i +" in arena "+game.toUpperCase()+" set!");

        return true;
    }
    
    
    public String help(Player p) {
        return "/sg setspawn next- Sets a spawn in the arena you are located in.";
    }
}


