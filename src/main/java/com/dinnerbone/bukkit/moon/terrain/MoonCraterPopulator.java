package com.dinnerbone.bukkit.moon.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import com.dinnerbone.bukkit.moon.BukkitMoon;
import com.dinnerbone.bukkit.moon.MoonUtils;

public class MoonCraterPopulator extends BlockPopulator {
	// Returns a random integer within the bounds (inclusive)
	private int randInt(Random random, int hi, int lo) {
		return random.nextInt(hi - lo + 1) + lo;
	}

/**
* @author JSW-TheOtherOne
*/
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		//Decide to create a crater or not
		if (random.nextInt(100) <= BukkitMoon.getCraterChance()) {
			int cx = chunk.getX();
			int cz = chunk.getZ();
			// Create random radius
			int radius = 0;
			if (random.nextInt(100) <= BukkitMoon.getBigCraterChance()) {
				radius = this.randInt(random, BukkitMoon.getBigCraterSize(), BukkitMoon.getSmallCraterSize())/2;
			} else {
				radius = this.randInt(random, BukkitMoon.getSmallCraterSize(), BukkitMoon.getMinCraterSize())/2;
			}
			radius = radius--;
			// Create random X/Z position in safe coords
			int centreX = (cx << 4) + random.nextInt(15);
			int centreZ = (cz << 4) + random.nextInt(15);
			int centreY = world.getHighestBlockYAt(centreX, centreZ);
			ArrayList<Integer> centreBlock = new ArrayList<Integer>();
			centreBlock.add(centreX);
			centreBlock.add(centreY);
			centreBlock.add(centreZ);
			// Loop through all the X and Z and Y (local)
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					//Is this block loaded?
					int cxCheck = (centreX + x) >> 4;
					int czCheck = (centreZ + z) >> 4;
					if (world.isChunkLoaded(cxCheck, czCheck)) {
						//Find highest block in column
						int highestY = world.getHighestBlockYAt(centreX+x, centreZ+z);
						highestY = highestY - centreY;
						for (int y = -radius; y <= highestY ; y++) {
							ArrayList<Integer> positionBlock = new ArrayList<Integer>();
							positionBlock.add(centreBlock.get(0) + x);
							positionBlock.add(centreBlock.get(1) + y);
							positionBlock.add(centreBlock.get(2) + z);
							// Check radius
							int XX = positionBlock.get(0);
							int YY = positionBlock.get(1);
							int ZZ = positionBlock.get(2);
							Block block = world.getBlockAt(XX,YY,ZZ);
							Double checkBlock = MoonUtils.distanceArray(centreBlock,positionBlock);
							if ((checkBlock <= (radius + 0.5)||YY>centreY) && block.getType().equals(Material.END_STONE)) {
								block.setType(Material.AIR);
							}
						}
					}
				}
			}
			//create a rim
			if (BukkitMoon.getCraterRim() && radius >= 5) {
				CreaterRim(centreBlock, radius, chunk, world);
			}
		}
	}

/**
* @author JSW-TheOtherOne
*/
	private void CreaterRim(List<Integer> centreBlock, int rad, Chunk chunk, World world) {
		int XX = centreBlock.get(0);
		int YY = centreBlock.get(1);
		int ZZ = centreBlock.get(2);
		int rim = rad+1;
		
		Integer[] xzArray = null;
		xzArray = new Integer[2];
		for (int doThree = 0; doThree < 3; doThree++) {
			rim = rad+1+doThree;
			for (int loopRim = 0; loopRim < 4; loopRim++) {
				int xORz = 0;
				switch(loopRim) {
				case 0:
					xORz = 0;
					xzArray[0] = XX;
					xzArray[1] = ZZ - rim;
					break;
				case 1:
					xORz = 0;
					xzArray[0] = XX;
					xzArray[1] = ZZ + rim;
					break;
				case 2:
					xORz = 1;
					xzArray[0] = XX - rim;
					xzArray[1] = ZZ;
					break;
				case 3:
					xORz = 1;
					xzArray[0] = XX + rim;
					xzArray[1] = ZZ;
					break;
				}
				int centreXZ = xzArray[xORz];
				for (int xz = -rim; xz < rim; xz++) {
					xzArray[xORz] = centreXZ + xz;
					int cx = xzArray[0] >> 4;
					int cz = xzArray[1] >> 4;
					if (world.isChunkLoaded(cx, cz)) {
						int heightYY = YY;
						if (doThree == 1) heightYY = heightYY+1;
						if (world.getHighestBlockYAt(xzArray[0], xzArray[1]) >= YY) {
							for (int loopYY = world.getHighestBlockYAt(xzArray[0], xzArray[1]); loopYY <= heightYY; loopYY++) {
								Block block = world.getBlockAt(xzArray[0],loopYY,xzArray[1]);
								if (block.getType().equals(Material.AIR)) block.setType(Material.END_STONE);	
							}
						}
					}
				}
			}
		}
	}
}