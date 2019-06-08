
package com.dinnerbone.bukkit.moon;

import com.dinnerbone.bukkit.moon.BukkitMoon;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MoonCommandExec implements CommandExecutor {
    private static Map<Player, World> worlds = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //cache old world
            if(worlds.containsKey(player)) {
                sender.sendMessage(ChatColor.RED  + "You are already on the moon!");
                return true;
            }

            worlds.put(player, player.getWorld());
            player.teleport(BukkitMoon.getMoon().getSpawnLocation());
        } else {
            sender.sendMessage("I don't know who you are!");
        }

        return true;
    }

    public static Map<Player, World> getWorldCache() { return worlds; }
}
