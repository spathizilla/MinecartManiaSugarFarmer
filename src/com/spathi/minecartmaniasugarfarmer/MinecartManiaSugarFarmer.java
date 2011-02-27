package com.spathi.minecartmaniasugarfarmer;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecartManiaSugarFarmer extends JavaPlugin {
	public static Logger log;
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile description;
	public static MinecartManiaActionListener listener = new MinecartManiaActionListener();

	public void onEnable() {
		server = this.getServer();
		description = this.getDescription();
		log = Logger.getLogger("Minecraft");
		instance = this;
		
		PluginDescriptionFile pdfFile = this.getDescription();
		Plugin MinecartMania = server.getPluginManager().getPlugin("Minecart Mania Core");
		Plugin MinecartManiaSigns = server.getPluginManager().getPlugin("Minecart Mania Sign Commands");

		if (MinecartMania == null || MinecartManiaSigns == null) {
			log.severe(pdfFile.getName() + " requires Minecart Mania Core and Sign Commands to function!");
			log.severe(pdfFile.getName() + " is disabled!");
			this.setEnabled(false);
		}
		else {	
	        getServer().getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, listener, Priority.Normal, this);
	        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		}
	}

	public void onDisable() {
		
	}

}