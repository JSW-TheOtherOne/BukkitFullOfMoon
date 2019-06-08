package com.dinnerbone.bukkit.moon;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class MoonChunkGenerator extends ChunkGenerator {
	private NoiseGenerator generator;

	private NoiseGenerator getGenerator(World world) {
		if (generator == null) {
			generator = new SimplexNoiseGenerator(world);
		}
		return generator;
	}

	private int getHeight(World world, double x, double y, int variance) {
		NoiseGenerator gen = getGenerator(world);
		double result = gen.noise(x, y);
		result *= variance;
		return 0;//NoiseGenerator.floor(result); 
	}
	
	@Override
	public ChunkData generateChunkData(World world, Random random, int cx, int cz, ChunkGenerator.BiomeGrid biome) {
		// Create the chunk data from method
		ChunkData data = this.createChunkData(world);
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int height = this.getHeight(world, cx + (x * 0.0625), cz + (z * 0.625), 2) + 60;
				for (int y = 0; y < height; y++) {
					if (y == 0) {
						data.setBlock(x, y, z, Material.BEDROCK);
					} else {
						if (y <= 2 && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
							data.setBlock(x, y, z, Material.BEDROCK);
						} else {
							data.setBlock(x, y, z, Material.END_STONE);}
					}
					biome.setBiome(x, z, Biome.DESERT);
				}
			}
		}
		return data;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
	if (BukkitMoon.config.getBoolean("BlockPopulators.GenerateCraters"))
		world.getPopulators().add(new MoonCraterPopulator());
	if (BukkitMoon.config.getBoolean("BlockPopulators.GenerateFlags"))	
		world.getPopulators().add(new FlagPopulator());
		return world.getPopulators();
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// Create random position within 100 blocks of 0,0
		int x = random.nextInt(200) - 100;
		int z = random.nextInt(200) - 100;
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}
}
