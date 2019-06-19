package com.dinnerbone.bukkit.moon;

import com.dinnerbone.bukkit.moon.MoonUtils;

import com.dinnerbone.bukkit.moon.command.MoonCommandExec;
import com.dinnerbone.bukkit.moon.command.ReloadConfigExec;
import com.dinnerbone.bukkit.moon.command.WorldCommandExec;
import com.dinnerbone.bukkit.moon.terrain.MoonChunkGenerator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class BukkitMoon extends JavaPlugin implements Listener {
	private final static String moon_Name = "Bukkit_Moon";
    public static JavaPlugin plugin = null;
	private static World moon = null;
	public static FileConfiguration config;

	private static int craterChance;
	private static int bigCraterChance;
	private static int minCraterSize;
	private static int smallCraterSize;
	private static int bigCraterSize;
	private static int noiseVariance;
	private static int subDivitions;
	private static double noiseScale;
	private static boolean craterRim;
	
	@Override
	public void onDisable() {
		MoonUtils.log(ChatColor.RED,"Stopping BukkitMoon Generator");
	}
    
	@Override
	public void onEnable() {
        plugin = this;
		saveDefaultConfig();
		//Get the config file
        setPluginConfig(plugin.getConfig());
		//TO DO get list of moon worlds (config.getString("BukkitMoonWorlds")
		
		// Log Enabled
		PluginDescriptionFile desc = this.getDescription();

        getCommand("moon").setExecutor(new MoonCommandExec());
        getCommand("earth").setExecutor(new WorldCommandExec());
        getCommand("BMreload").setExecutor(new ReloadConfigExec());
        Bukkit.getPluginManager().registerEvents(this, this);
        
		MoonUtils.log(ChatColor.GREEN, desc.getName() + " version " + desc.getVersion() + " is enabled!"); 
    }

	 public static World getMoon() {
		 if (moon == null) {
			 WorldCreator wCreator = new WorldCreator(moon_Name);
			 wCreator.environment(World.Environment.NORMAL);
			 wCreator.generator(new MoonChunkGenerator());
			 
			moon = Bukkit.getServer().createWorld(wCreator);
		 }
		 return moon;
	}

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new MoonChunkGenerator();
    }

	 public boolean anonymousCheck(CommandSender sender) {
		 if (!(sender instanceof Player)) {
			 sender.sendMessage(ChatColor.LIGHT_PURPLE + "Only Players can run this commad!");
			 return true;
		 } else {
			 return false;
		 }
	 }
	 
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(MoonCommandExec.getWorldCache().containsKey(event.getEntity())) {
            MoonCommandExec.getWorldCache().remove(event.getEntity());
            event.getEntity().removePotionEffect(PotionEffectType.JUMP);
        }
    }

    //Get the config file settings and check if valid
    public static void setPluginConfig(FileConfiguration config) {
		BukkitMoon.config = config;
        setNoiseVariance(plugin.getConfig().getInt("TerrainGeneration.NOISE_VARIANCE"));
        setNoiseScale(plugin.getConfig().getInt("TerrainGeneration.NOISE_SCALE"));
		setSubDivitions(plugin.getConfig().getInt("TerrainGeneration.SUB_DIVITIONS"));
		setCraterChance(plugin.getConfig().getInt("ConfigCraters.CRATER_CHANCE"));
		setBigCraterChance(plugin.getConfig().getInt("ConfigCraters.BIG_CRATER_CHANCE"));
		setMinCraterSize(plugin.getConfig().getInt("ConfigCraters.MIN_CRATER_SIZE"));
		setSmallCraterSize(plugin.getConfig().getInt("ConfigCraters.SMALL_CRATER_SIZE"));
		setBigCraterSize(plugin.getConfig().getInt("ConfigCraters.BIG_CRATER_SIZE"));
		setCraterRim(plugin.getConfig().getBoolean("ConfigCraters.RIM_OR_NOT_TO_RIM"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW+ "BukkitMoon Plugin Config Loaded.");
	}

	public FileConfiguration getPluginConfig() {
		return config;
	}
    
	public static int getSubDivitions() {
		return subDivitions;
	}

	public static void setSubDivitions(int subDivitions) {
		if (subDivitions != 1 && subDivitions != 2 && subDivitions != 4) subDivitions = 1;
		BukkitMoon.subDivitions = subDivitions;
	}

	public static int getNoiseVariance() {
		return noiseVariance;
	}

	public static void setNoiseVariance(int noiseVariance) {
		BukkitMoon.noiseVariance = noiseVariance;
	}
	
	private static void setNoiseScale(double noiseScale) {
		BukkitMoon.noiseScale = noiseScale;
	}

	public static int getCraterChance() {
		return craterChance;
	}

	public static void setCraterChance(int cRATER_CHANCE) {
		if (cRATER_CHANCE < 1) cRATER_CHANCE = 1;
		if (cRATER_CHANCE > 100) cRATER_CHANCE = 100;
		craterChance = cRATER_CHANCE;
	}

	public static int getBigCraterChance() {
		return bigCraterChance;
	}

	public static void setBigCraterChance(int bIG_CRATER_CHANCE) {
		if (bIG_CRATER_CHANCE < 1) bIG_CRATER_CHANCE = 1;
		if (bIG_CRATER_CHANCE > 100) bIG_CRATER_CHANCE = 100;
		bigCraterChance = bIG_CRATER_CHANCE;
	}

	public static int getBigCraterSize() {
		return bigCraterSize;
	}

	public static void setBigCraterSize(int bIG_CRATER_SIZE) {
		if (bIG_CRATER_SIZE <= getSmallCraterSize()) bIG_CRATER_SIZE = (getSmallCraterSize() + 1);
		if (bIG_CRATER_SIZE > 16) bIG_CRATER_SIZE = 16;
		bigCraterSize = bIG_CRATER_SIZE;
	}

	public static int getSmallCraterSize() {
		return smallCraterSize;
	}

	public static void setSmallCraterSize(int sMALL_CRATER_SIZE) {
		if (sMALL_CRATER_SIZE <= getMinCraterSize()) sMALL_CRATER_SIZE = (getMinCraterSize() + 1);
		if (sMALL_CRATER_SIZE > 8) sMALL_CRATER_SIZE = 8;
		smallCraterSize = sMALL_CRATER_SIZE;
	}

	public static int getMinCraterSize() {
		return minCraterSize;
	}

	public static void setMinCraterSize(int mIN_CRATER_SIZE) {
		if (mIN_CRATER_SIZE < 2) mIN_CRATER_SIZE = 2;
		if (mIN_CRATER_SIZE > 4) mIN_CRATER_SIZE = 4;
		minCraterSize = mIN_CRATER_SIZE;
	}

	public static boolean getCraterRim() {
		return craterRim;
	}

	public static void setCraterRim(boolean craterRim) {
		BukkitMoon.craterRim = craterRim;
	}
}