package com.dinnerbone.bukkit.moon.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dinnerbone.bukkit.moon.BukkitMoon;

public class ReloadConfigExec implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			sender.sendMessage(ChatColor.RED + "Only use from console!");
			return true;
		}
		((JavaPlugin) BukkitMoon.plugin).reloadConfig();
		BukkitMoon.setPluginConfig(BukkitMoon.plugin.getConfig());
		
		return true;	
	}
}