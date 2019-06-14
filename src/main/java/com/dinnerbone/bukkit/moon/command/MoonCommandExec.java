
package com.dinnerbone.bukkit.moon.command;

import com.dinnerbone.bukkit.moon.BukkitMoon;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class MoonCommandExec implements CommandExecutor {
    private static Map<Player, World> worlds = new HashMap<>();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	// Must be a player to teleport to world
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //cache old world
            if(worlds.containsKey(player)) {
                sender.sendMessage(ChatColor.RED  + "You are already on the moon!");
                return true;
            }

            worlds.put(player, player.getWorld());
            player.teleport(BukkitMoon.getMoon().getSpawnLocation());
            //gravity
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, Double.valueOf(2.66).intValue(), false));
        } else {
            sender.sendMessage("You must be a player to execute this command!");
        }

        return true;
    }

    public static Map<Player, World> getWorldCache() { return worlds; }
}
