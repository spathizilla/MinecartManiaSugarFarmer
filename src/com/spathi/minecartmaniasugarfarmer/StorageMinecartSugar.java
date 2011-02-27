package com.spathi.minecartmaniasugarfarmer;


import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class StorageMinecartSugar {
	public static Logger log;

	public static void doAutoSugarFarm(MinecartManiaStorageCart minecart) {
		log = Logger.getLogger("Minecraft");

		if((minecart.getDataValue("AutoSugar") == null) && (minecart.getDataValue("AutoPlant") == null)) {
			return;
		}

		if (MinecartManiaWorld.getIntValue(MinecartManiaWorld.getConfigurationValue("Nearby Collection Range")) < 1) {
			return;
		}

		Location loc = minecart.minecart.getLocation().clone();
		int range = minecart.getEntityDetectionRange();
		for (int dx = -(range); dx <= range; dx++){
			for (int dy = -(range); dy <= range; dy++){
				for (int dz = -(range); dz <= range; dz++){
					//Setup data
					int x = loc.getBlockX() + dx;
					int y = loc.getBlockY() + dy;
					int z = loc.getBlockZ() + dz;

					int id = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z);
					int aboveId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y+1, z); 
					int belowId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z);
					
					//Harvest Sugar
					if (minecart.getDataValue("AutoSugar") != null) {
					
						// Check for sugar blocks and ensure they're the top one in the stack. 
						// Breaking sugar below the top will result in cane on the track which can stop the cart
						// until autocollection is turned back on.

						if (id == Material.SUGAR_CANE_BLOCK.getId() && aboveId != Material.SUGAR_CANE_BLOCK.getId()) {
							if (belowId == Material.GRASS.getId() ||  belowId == Material.DIRT.getId()) {
								if(minecart.getDataValue("AutoPlant") == null) {
									minecart.addItem(Material.SUGAR_CANE.getId());
									MinecartManiaWorld.setBlockAt(minecart.minecart.getWorld(), Material.AIR.getId(), x, y, z);
								}
							} else {
								minecart.addItem(Material.SUGAR_CANE.getId());
								MinecartManiaWorld.setBlockAt(minecart.minecart.getWorld(), Material.AIR.getId(), x, y, z);
							}
						}
					}

					//update data
					id = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z);
					aboveId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y+1, z);
					belowId = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y-1, z);

					//Replant cane
					if (minecart.getDataValue("AutoPlant") != null) {
						if (id == Material.GRASS.getId() ||  id == Material.DIRT.getId()) {
							if (aboveId == Material.AIR.getId()) {

								// Need to check for water or the cane will not plant.
								int water1 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x+1, y, z);
								int water2 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x-1, y, z);
								int water3 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z+1);
								int water4 = MinecartManiaWorld.getBlockIdAt(minecart.minecart.getWorld(), x, y, z-1);

								boolean foundwater = false;

								if(water1 == Material.WATER.getId() || water1 == Material.STATIONARY_WATER.getId()) { foundwater = true; }
								if(water2 == Material.WATER.getId() || water2 == Material.STATIONARY_WATER.getId()) { foundwater = true; }
								if(water3 == Material.WATER.getId() || water3 == Material.STATIONARY_WATER.getId()) { foundwater = true; }
								if(water4 == Material.WATER.getId() || water4 == Material.STATIONARY_WATER.getId()) { foundwater = true; }

								if(foundwater == true) {

									if (minecart.removeItem(Material.SUGAR_CANE.getId())) {
										MinecartManiaWorld.setBlockAt(minecart.minecart.getWorld(), Material.SUGAR_CANE_BLOCK.getId(), x, y+1, z);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

