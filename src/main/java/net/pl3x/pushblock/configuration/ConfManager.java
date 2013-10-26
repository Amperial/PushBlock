package net.pl3x.pushblock.configuration;

import java.io.File;
import java.io.IOException;

import net.pl3x.pushblock.PushBlock;

import org.bukkit.Bukkit;
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
}
