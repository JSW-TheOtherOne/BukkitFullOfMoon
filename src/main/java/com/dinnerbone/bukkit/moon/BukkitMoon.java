package com.dinnerbone.bukkit.moon;

import com.dinnerbone.bukkit.moon.command.MoonCommandExec;
import com.dinnerbone.bukkit.moon.command.WorldCommandExec;
import com.dinnerbone.bukkit.moon.terrain.MoonChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class BukkitMoon extends JavaPlugin implements Listener {
    private final static String WORLD_NAME = "BukkitMoon";
    private static World moon = null;

    public void onDisable() {
    }
    
    public void onEnable() {
        PluginDescriptionFile desc = this.getDescription();

        System.out.println( desc.getName() + " version " + desc.getVersion() + " is enabled!" );

        getCommand("moon").setExecutor(new MoonCommandExec());
        getCommand("earth").setExecutor(new WorldCommandExec());
        Bukkit.getPluginManager().registerEvents(this, this);
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

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(MoonCommandExec.getWorldCache().containsKey(event.getEntity())) {
            MoonCommandExec.getWorldCache().remove(event.getEntity());
            event.getEntity().removePotionEffect(PotionEffectType.JUMP);
        }
    }
}