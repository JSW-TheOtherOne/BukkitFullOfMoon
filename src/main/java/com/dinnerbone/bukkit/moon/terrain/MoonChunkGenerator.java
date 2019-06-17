package com.dinnerbone.bukkit.moon.terrain;

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
import com.dinnerbone.bukkit.moon.BukkitMoon;

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
		return NoiseGenerator.floor(result); 
	}
	
/**
* @author JSW-TheOtherOne
*/
	@Override
	public ChunkData generateChunkData(World world, Random random, int cx, int cz, ChunkGenerator.BiomeGrid biome) {
		// Create the chunk data from method
		ChunkData data = this.createChunkData(world);
		
		int xRow;
		int xRowEnd = 0;
		int zColumn;
		int zColumnEnd = 0;
		int exitLoop = 0;
		
		int whichXZ;
		int xzStart;
		int xzEnd;
		int diRection;

		Integer[] xzArray = null;
		xzArray = new Integer[2];
		
		int currentX;
		int currentXend;
		int currentZ;
		int currentZend;
		
		int subDivide = BukkitMoon.getSubDivitions();
		int xOff = 0;
		int zOff = 0;

		for (int loopSubDiv = 0; loopSubDiv < (subDivide*subDivide); loopSubDiv++) {
			xRow = 0;
			zColumn = 0;
		    switch(subDivide) {
		    	case 4:
		        	xRowEnd = 3;
		        	zColumnEnd = 3;
		        	exitLoop = 1;
		        	if (loopSubDiv==0||loopSubDiv==4||loopSubDiv==8||loopSubDiv==12) xOff = 0;
		        	if (loopSubDiv==1||loopSubDiv==5||loopSubDiv==9||loopSubDiv==13) xOff = 4;
		        	if (loopSubDiv==2||loopSubDiv==6||loopSubDiv==10||loopSubDiv==14) xOff = 8;
		        	if (loopSubDiv==3||loopSubDiv==7||loopSubDiv==11||loopSubDiv==15) xOff = 12;
		        	if (loopSubDiv==0||loopSubDiv==1||loopSubDiv==2||loopSubDiv==3) zOff = 0;
		        	if (loopSubDiv==4||loopSubDiv==5||loopSubDiv==6||loopSubDiv==7) zOff = 4;
		        	if (loopSubDiv==8||loopSubDiv==9||loopSubDiv==10||loopSubDiv==11) zOff = 8;
		        	if (loopSubDiv==12||loopSubDiv==13||loopSubDiv==14||loopSubDiv==15) zOff = 12;
		        	break;
		    	case 2:
		        	xRowEnd = 7;
		        	zColumnEnd = 7;
		        	exitLoop = 3;
					 if (loopSubDiv==0||loopSubDiv==2) xOff = 0;
					 if (loopSubDiv==1||loopSubDiv==3) xOff = 8;
					 if (loopSubDiv==0||loopSubDiv==1) zOff = 0;
					 if (loopSubDiv==2||loopSubDiv==3) zOff = 8;
					 break;
		    	case 1:
		        	xRowEnd = 15;
		        	zColumnEnd = 15;
		        	exitLoop = 7;
					xOff = 0;
					zOff = 0;
					break;
		    }
		for (int loopChunk = 0; loopChunk < (8/subDivide); loopChunk++) {
			diRection = 1;
			currentX = xRow+xOff;
			currentXend = xRowEnd+xOff;
			currentZ = zColumn+zOff;
			currentZend = zColumnEnd+zOff;
			for (int loop01 = 0; loop01 < 2; loop01++){
				whichXZ = 0;
				xzStart = currentX;
				xzArray[whichXZ] = xzStart;
				xzEnd = currentXend;
				xzArray[1] = currentZ;
				
				for (int loop02 = 0; loop02 < 2; loop02++){
					for (int loopXZ = xzStart; (xzEnd >= xzStart ? loopXZ <= xzEnd : loopXZ >= xzEnd); loopXZ = loopXZ+diRection) {
						if (loopChunk==exitLoop && loop01==1 && ((loop02==0 && loopXZ == (exitLoop-1)) || (loop02==1)))break;
						int height = this.getHeight(world, cx + (xzArray[0] * 0.0625), cz + (xzArray[1] * 0.0625), BukkitMoon.noiseVariance) + 60;
						xzArray[whichXZ] = loopXZ;
						for (int y = 0; y <= height; y++) {
							if (y == 0) {
								data.setBlock(xzArray[0], y, xzArray[1], Material.BEDROCK);
							} else {
								if (y <= 2 && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) {
									data.setBlock(xzArray[0], y, xzArray[1], Material.BEDROCK);
								} else {
									data.setBlock(xzArray[0], y, xzArray[1], Material.END_STONE);}
							}
						}
						biome.setBiome(xzArray[0], xzArray[1], Biome.DESERT);
					}
					whichXZ = 1;
					xzStart = currentZ+diRection;
					xzArray[whichXZ] = xzStart;
					xzEnd = currentZend;
					xzArray[0] = currentXend;
				}
				diRection = -1;
				currentX = xRowEnd-1+xOff;
				currentZ = zColumnEnd+zOff;
				currentXend = xRow+xOff;
				currentZend = zColumn+1+zOff;
			}
			xRow++;
			zColumn++;
			xRowEnd--;
			zColumnEnd--;
		}}
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
