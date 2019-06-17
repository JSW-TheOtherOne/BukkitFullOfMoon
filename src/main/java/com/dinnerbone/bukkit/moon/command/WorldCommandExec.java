package com.dinnerbone.bukkit.moon.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Javi
 */
public class WorldCommandExec implements CommandExecutor {
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            //get player's old world and teleport
            if(MoonCommandExec.getWorldCache().containsKey(player)) {
                player.teleport(MoonCommandExec.getWorldCache().get(player).getSpawnLocation());
                //back to earths normal gravity
                player.removePotionEffect(PotionEffectType.JUMP);
                MoonCommandExec.getWorldCache().remove(player);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You are already on the earth!");
            }

        } else {
            sender.sendMessage("You must be a player to execute this command!");
        }

        return true;
    }
}
