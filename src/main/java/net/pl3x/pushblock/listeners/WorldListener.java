package net.pl3x.pushblock.listeners;

import net.pl3x.pushblock.PushBlock;
import net.pl3x.pushblock.listeners.blok.Blok;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {
	private PushBlock plugin;
	
	public WorldListener(PushBlock plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void worldUnload(WorldUnloadEvent event) {
		if (!plugin.getConfig().getBoolean("reset-on-unload-world", false))
			return;
		for (Blok blok : plugin.getBlokManager().getBloks()) {
			if (blok.getLocation().getWorld().equals(event.getWorld()))
				blok.reset();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void worldLoad(WorldLoadEvent event) {
		if (!plugin.getConfig().getBoolean("reset-on-unload-world", false))
			return;
		for (Blok blok : plugin.getBlokManager().getBloks()) {
			if (blok.getLocation().getWorld().equals(event.getWorld()))
				blok.reset();
		}
	}
}
