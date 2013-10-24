package net.pl3x.pushblock.configuration;

import java.io.File;
import java.io.IOException;

import net.pl3x.pushblock.PushBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfManager extends YamlConfiguration {
	private static ConfManager cm;
	
	public static ConfManager getConfManager() {
		if (cm != null)
			return cm;
		cm = new ConfManager();
		return cm;
	}
	
	public static void removeConfManager() {
		cm.forceSave();
		cm = null;
	}
	
	private File pconfl = null;
	private Object saveLock = new Object();
	
	public ConfManager() {
		super();
		File dataFolder = ((PushBlock)Bukkit.getPluginManager().getPlugin("PushBlock")).getDataFolder();
		pconfl = new File(dataFolder + File.separator + "blocks.yml");
		try {
			load(pconfl);
		} catch (Exception ignored) {
			//
		}
	}
	
	public void reload() {
		try {
			load(pconfl);
		} catch (Exception ignored) {
			//
		}
	}
	
	public boolean exists() {
		return pconfl.exists();
	}
	
	public void forceSave() {
		synchronized (saveLock) {
			try {
				save(pconfl);
			} catch (IOException ignored) {
				//
			} catch (IllegalArgumentException ignored) {
				//
			}
		}
	}
	
	public Location getLocation(String id) {
		if (!isSet("blocks." + id))
			return null;
		String w = getString("blocks." + id + ".w");
		double x = getDouble("blocks." + id + ".x");
		double y = getDouble("blocks." + id + ".y");
		double z = getDouble("blocks." + id + ".z");
		return new Location(Bukkit.getWorld(w), x, y, z);
	}
	
	public void setLocation(String id, Location value) {
		set("blocks." + id + ".w", value.getWorld().getName());
		set("blocks." + id + ".x", value.getX());
		set("blocks." + id + ".y", value.getY());
		set("blocks." + id + ".z", value.getZ());
	}
}
