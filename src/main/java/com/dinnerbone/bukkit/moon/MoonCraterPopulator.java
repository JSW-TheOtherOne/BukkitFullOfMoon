package com.dinnerbone.bukkit.moon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

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
		if (random.nextInt(100) <= BukkitMoon.CRATER_CHANCE) {
			int cx = chunk.getX();
			int cz = chunk.getZ();
			boolean upChLo = world.isChunkLoaded(cx+1, cz);
			boolean upChLLo = world.isChunkLoaded(cx+1, cz-1);
			boolean upChRLo = world.isChunkLoaded(cx+1, cz+1);
			boolean downChLo = world.isChunkLoaded(cx-1, cz);
			boolean downChLLo = world.isChunkLoaded(cx-1, cz-1);
			boolean downChRLo = world.isChunkLoaded(cx-1, cz+1);
			boolean leftChLo = world.isChunkLoaded(cx, cz-1);
			boolean rightChLo = world.isChunkLoaded(cx, cz+1);
			boolean[] dirArray;
			dirArray = new boolean[8];
			if (upChLo == true) {
				dirArray[0] = true;
				if (upChLLo == true && leftChLo ==true) {
					dirArray[4] = true;
					dirArray[0] = false;
				} else {
					if (upChRLo == true && rightChLo ==true) {
						dirArray[5] = true;
						dirArray[0] = false;
					}
				}
			}
			if (downChLo == true) {
				dirArray[3] = true;
				if (downChLLo == true && leftChLo == true) {
					dirArray[6] = true;
					dirArray[3] = false;
				} else {
					if (downChRLo == true && rightChLo == true) {
						dirArray[7] = true;
						dirArray[3] = false;
					}
				}
			}
			if (leftChLo == true && !dirArray[4] && !dirArray[6])
				dirArray[1] = true;
			if (rightChLo && !dirArray[5] && !dirArray[7])
				dirArray[2] = true;
			int dirCount = 0;
			for (int dirLoop = 0; dirLoop < 8; dirLoop++) {
				if (dirArray[dirLoop] == true)
					dirCount++;
			}
			int dirFound = -1;
			if (dirCount != 0) {
				int aRand = random.nextInt(dirCount);
				int trueCount = 0;
				for (int dirLoop = 0; dirLoop < 8; dirLoop++) {
					if (dirArray[dirLoop] == true) {
						trueCount++;
						if (trueCount == aRand) {
							dirFound = dirLoop;
							break;
						}
					}
				}
			}
			int radius = 0;
			if (random.nextInt(100) <= BukkitMoon.BIG_CRATER_CHANCE) {
				radius = this.randInt(random, BukkitMoon.BIG_CRATER_SIZE, BukkitMoon.SMALL_CRATER_SIZE)/2;
			} else {
				radius = this.randInt(random, BukkitMoon.SMALL_CRATER_SIZE, BukkitMoon.MIN_CRATER_SIZE)/2;
			}
			radius = radius--;
			if ((dirFound == -1 || dirArray[0] || dirArray[1] || dirArray[2] || dirArray[3]) && radius > 7)
				radius = 7;
			int xStart = chunk.getX() << 4;
			int zStart = chunk.getZ() << 4;
			int xLength = 15;
			int zLength = 15;
			if (dirFound==0 || dirFound==4 || dirFound==5)
				zLength = zLength + 16;
			if (dirFound==1 || dirFound==4 || dirFound==6)
				xStart = xStart - 16;
			if (dirFound==2 || dirFound==5 || dirFound==7)
				xLength = zLength + 16;
			if (dirFound==3 || dirFound==6 || dirFound==7)
				zStart = zStart - 16;
			int centreX = (xStart+radius) + random.nextInt(xLength-(radius*2));
			int centreZ = (zStart+radius) + random.nextInt(zLength-(radius*2));
			int centreY = world.getHighestBlockYAt(centreX, centreZ);
//			ArrayList<Integer> centreBlock = new ArrayList<Integer>();
			List<Integer> centreBlock = Collections.synchronizedList(new ArrayList<Integer>());
			centreBlock.add(centreX);
			centreBlock.add(centreY);
			centreBlock.add(centreZ);
			
			if (radius >= 5) {
				CreaterRim(centreBlock, radius, chunk, world);
			}
			
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					int highestY = world.getHighestBlockYAt(centreX+x, centreZ+z);
					highestY = highestY - centreY;
					for (int y = -radius; y <= highestY ; y++) {
//						ArrayList<Integer> positionBlock = new ArrayList<Integer>();
						List<Integer> positionBlock = Collections.synchronizedList(new ArrayList<Integer>());
						positionBlock.add(centreBlock.get(0) + x);
						positionBlock.add(centreBlock.get(1) + y);
						positionBlock.add(centreBlock.get(2) + z);
						int XX = positionBlock.get(0);
						int YY = positionBlock.get(1);
						int ZZ = positionBlock.get(2);
						Block block = world.getBlockAt(XX,YY,ZZ);
						Double checkBlock = MoonUtils.distanceArray(centreBlock,positionBlock);
						if ((checkBlock <= (radius + 0.5)||YY>=centreY) && block.getType().equals(Material.END_STONE)) {
							block.setType(Material.AIR);
						}
					}
				}
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
					int hightYY = YY;
					if (doThree == 1) hightYY = hightYY+1;					
					for (int loopYY = world.getHighestBlockYAt(xzArray[0], xzArray[1]); loopYY <= hightYY; loopYY++) {
						Block block = world.getBlockAt(xzArray[0],loopYY,xzArray[1]);
						if (block.getType().equals(Material.AIR)) block.setType(Material.END_STONE);
					}
					
				}
			}
		}}
	}
}