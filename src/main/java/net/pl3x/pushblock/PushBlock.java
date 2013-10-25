package net.pl3x.pushblock;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import net.pl3x.pushblock.configuration.ConfManager;
import net.pl3x.pushblock.listeners.BlokListener;
import net.pl3x.pushblock.listeners.PlayerListener;
import net.pl3x.pushblock.listeners.blok.Blok;
import net.pl3x.pushblock.listeners.blok.BlokManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class PushBlock extends JavaPlugin {
	private BlokManager blokManager;
	private ConfManager confManager;
	
	public void onEnable() {
		if (!new File(getDataFolder() + File.separator + "config.yml").exists())
			saveDefaultConfig();
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new BlokListener(this), this);
		
		blokManager = new BlokManager();
		confManager = ConfManager.getConfManager();
		
		loadAllBloks();
		
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			log("&4Failed to start Metrics: &e" + e.getMessage());
		}
		
		log(getName() + " v" + getDescription().getVersion() + " by BillyGalbreath enabled!");
	}
	
	public void onDisable() {
		confManager.forceSave();
		log(getName() + " Disabled.");
	}
	
	public void log (Object obj) {
		if (getConfig().getBoolean("color-logs", true)) {
			getServer().getConsoleSender().sendMessage(colorize("&3[&d" +  getName() + "&3]&r " + obj));
		} else {
			Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + ((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
		}
	}
	
	public void debug(Object obj) {
		if (getConfig().getBoolean("debug-mode", false))
			log(obj);
	}
	
	public String colorize(String str) {
		return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
	}
	
	public BlokManager getBlokManager() {
		return blokManager;
	}
	
	public void loadAllBloks() {
		ConfManager cm = ConfManager.getConfManager();
		if (!cm.exists() || cm.get("blocks") == null)
			return;
		Map<String, Object> opts = cm.getConfigurationSection("blocks").getValues(false);
		if (opts.keySet().isEmpty())
			return;
		for (String id : opts.keySet()) {
			Integer i = null;
			try {
				i = Integer.valueOf(id);
			} catch (Exception e) {
				debug("Malformed ID in blocks.yml: ID: " + id);
				continue;
			}
			Location loc = cm.getLocation(id);
			if (loc == null) {
				debug("Malformed location in blocks.yml: ID: " + id);
				continue;
			}
			Location originalLoc = cm.getLocation(id + ".original");
			Blok blok;
			if (originalLoc == null)
				blok = new Blok(originalLoc, loc, i);
			else
				blok = new Blok(loc, i);
			blokManager.addBlok(blok);
			debug("Loaded block from config. Id: " + id + " Location: " + loc.toString());
		}
	}
}
