package com.dinnerbone.bukkit.moon;

<<<<<<< HEAD
import com.dinnerbone.bukkit.moon.MoonUtils;

import com.dinnerbone.bukkit.moon.command.MoonCommandExec;
import com.dinnerbone.bukkit.moon.command.WorldCommandExec;
import com.dinnerbone.bukkit.moon.terrain.MoonChunkGenerator;

=======
>>>>>>> parent of 6100076... Add moon gravity, organize plugin files
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
<<<<<<< HEAD

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

=======
>>>>>>> parent of 6100076... Add moon gravity, organize plugin files
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMoon extends JavaPlugin {
<<<<<<< HEAD
	private final static String WORLD_NAME = "Bukkit_Moon";
	private static World moon = null;
	static FileConfiguration config;

	static int CRATER_CHANCE;
	static int BIG_CRATER_CHANCE;
	static int MIN_CRATER_SIZE;
	static int SMALL_CRATER_SIZE;
	static int BIG_CRATER_SIZE;
	
	@Override
	public void onDisable() {
		MoonUtils.log(ChatColor.RED,"Stopping BukkitMoon Generator");
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		//Get the config file
		config = this.getConfig();
		CRATER_CHANCE = config.getInt("ConfigCraters.CRATER_CHANCE");
		BIG_CRATER_CHANCE = config.getInt("ConfigCraters.BIG_CRATER_CHANCE");
		MIN_CRATER_SIZE = config.getInt("ConfigCraters.MIN_CRATER_SIZE");
		SMALL_CRATER_SIZE = config.getInt("ConfigCraters.SMALL_CRATER_SIZE");
		BIG_CRATER_SIZE = config.getInt("ConfigCraters.BIG_CRATER_SIZE");
		if (CRATER_CHANCE < 1) CRATER_CHANCE = 1;
		if (CRATER_CHANCE > 100) CRATER_CHANCE = 100;
		if (BIG_CRATER_CHANCE < 1) BIG_CRATER_CHANCE = 1;
		if (BIG_CRATER_CHANCE > 100) BIG_CRATER_CHANCE = 100;
		if (MIN_CRATER_SIZE < 1) MIN_CRATER_SIZE = 1;
		if (MIN_CRATER_SIZE > 7) MIN_CRATER_SIZE = 7;
		if (SMALL_CRATER_SIZE <= MIN_CRATER_SIZE) SMALL_CRATER_SIZE = MIN_CRATER_SIZE + 1;
		if (SMALL_CRATER_SIZE > 6) SMALL_CRATER_SIZE = 6;
		if (BIG_CRATER_SIZE <= SMALL_CRATER_SIZE) BIG_CRATER_SIZE = SMALL_CRATER_SIZE + 1;
		if (BIG_CRATER_SIZE > 16) BIG_CRATER_SIZE =16;
		//TO DO get list of moon worlds (config.getString("BukkitMoonWorlds")

		getCommand("moon").setExecutor(new MoonCommandExec());
		// Log Enabled
		PluginDescriptionFile desc = this.getDescription();
		MoonUtils.log(ChatColor.GREEN, desc.getName() + " version " + desc.getVersion() + " is enabled!");
	}

	 public boolean anonymousCheck(CommandSender sender) {
		 if (!(sender instanceof Player)) {
			 sender.sendMessage(ChatColor.LIGHT_PURPLE + "Only Players can run this commad!");
			 return true;
		 } else {
			 return false;
		 }
	 }

	 public static World getMoon() {
		 if (moon == null) {
			 WorldCreator creator = new WorldCreator(WORLD_NAME);
			 creator.environment(World.Environment.NORMAL);
			 creator.generator(new MoonChunkGenerator());
			moon = Bukkit.getServer().createWorld(creator);
		 }
		 return moon;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new MoonChunkGenerator();
	}

public class BukkitMoon extends JavaPlugin implements Listener {
=======
>>>>>>> parent of 6100076... Add moon gravity, organize plugin files
    private final static String WORLD_NAME = "BukkitMoon";
    private static World moon = null;

    public void onDisable() {
    }
    
    public void onEnable() {
        PluginDescriptionFile desc = this.getDescription();

        System.out.println( desc.getName() + " version " + desc.getVersion() + " is enabled!" );

        getCommand("moon").setExecutor(new MoonCommandExec());
        getCommand("earth").setExecutor(new WorldCommandExec());
    }

    public static World getMoon() {
        if (moon == null) {
            WorldCreator wc = new WorldCreator(WORLD_NAME);
            wc.environment(World.Environment.NORMAL);
            wc.generator(new MoonChunkGenerator());

            moon = Bukkit.getServer().createWorld(wc);
        }

        return moon;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new MoonChunkGenerator();
    }
}