package com.spathi.minecartmaniasugarfarmer;

import java.lang.reflect.Method;

import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaStorageCart;
import com.afforess.minecartmaniacore.MinecartManiaTaskScheduler;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniasigncommands.SignCommands;
import com.spathi.minecartmaniasugarfarmer.StorageMinecartSugar;

public class MinecartManiaActionListener extends MinecartManiaListener {
	
	public void onMinecartActionEvent(MinecartActionEvent event) {
		boolean action = event.isActionTaken();
		if (!event.isActionTaken()) {
			MinecartManiaMinecart minecart = event.getMinecart();
						
			if (minecart.isStorageMinecart()) {
				SignCommands.doAutoSetting((MinecartManiaStorageCart) minecart, "AutoSugar");
				SignCommands.doAutoSetting((MinecartManiaStorageCart) minecart, "AutoPlant");
				SignCommands.doAutoSetting((MinecartManiaStorageCart) minecart, "Sugar Off", "AutoSugar", null);
				SignCommands.doAutoSetting((MinecartManiaStorageCart) minecart, "Plant Off", "AutoPlant", null);
				SignCommands.doAlterCollectRange((MinecartManiaStorageCart) minecart);
				
				event.setActionTaken(action);
				SignCommands.updateSensors(minecart);
				
				//Efficiency. Don't farm overlapping tiles repeatedly, waste of time
				int interval = MinecartManiaWorld.getIntValue(minecart.getDataValue("Farm Interval"));
				if (interval == 0) {
					minecart.setDataValue("Farm Interval", new Integer(interval - 1));
				}
				else {
					minecart.setDataValue("Farm Interval", new Integer(minecart.getEntityDetectionRange()));
				

					final Object[] param = { (MinecartManiaStorageCart)minecart };
					@SuppressWarnings("rawtypes")
					Class[] paramtype = { MinecartManiaStorageCart.class };
										
					int delay = 0;
					
					try {
						final Method m = StorageMinecartSugar.class.getDeclaredMethod("doAutoSugarFarm", paramtype);
						//StorageMinecartSugar.doAutoSugarFarm((MinecartManiaStorageCart) minecart);
						
						Runnable a = new Runnable() { public void run() { try { m.invoke(null, param); } catch (Exception e) { e.printStackTrace(); } } };
						MinecartManiaSugarFarmer.server.getScheduler().scheduleAsyncDelayedTask(MinecartManiaSugarFarmer.instance, a, delay);
					} catch (Exception e) {
					
					}
				}
			}
		}
	}
}
