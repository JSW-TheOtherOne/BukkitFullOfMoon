package com.dinnerbone.bukkit.moon;

import com.dinnerbone.bukkit.moon.MoonCommandExec;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Javi
 */
public class WorldCommandExec implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //get player's old world and teleport
            if(MoonCommandExec.getWorldCache().containsKey(player)) {
                player.teleport(MoonCommandExec.getWorldCache().get(player).getSpawnLocation());
                MoonCommandExec.getWorldCache().remove(player);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You are already on the earth!");
            }

        } else {
            sender.sendMessage("I don't know who you are!");
        }

        return true;
    }
}
