package net.pl3x.pushblock.listeners;

import java.util.ArrayList;
import java.util.List;

import net.pl3x.pushblock.PushBlock;
import net.pl3x.pushblock.listeners.blok.Blok;

import org.bukkit.Bukkit;
import org.bukkit.World;
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
		if (!plugin.getConfig().getBoolean("delete-instanced-blocks-on-unload", false))
			return;
		World world = event.getWorld();
		List<Blok> toRemove = new ArrayList<Blok>();
		for (String worldName : plugin.getConfig().getStringList("template-worlds")) {
			if (!world.getName().startsWith(worldName))
				continue;
			for (Blok blok : plugin.getBlokManager().getBloks()) {
				if (!world.getName().equals(blok.getWorldName()))
					continue;
				toRemove.add(blok);
				plugin.debug("Removed instanced block. Id: " + blok.getId());
			}
		}
		for (Blok blok : toRemove) {
			plugin.getBlokManager().removeBlok(blok);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void worldLoad(WorldLoadEvent event) {
		if (!plugin.getConfig().getBoolean("allow-instanced-worlds", false))
			return;
		World world = event.getWorld();
		final List<Blok> toAdd = new ArrayList<Blok>();
		for (String worldName : plugin.getConfig().getStringList("template-worlds")) {
			plugin.log(world.getName() + " == " + worldName);
			if (!world.getName().startsWith(worldName))
				continue;
			for (Blok blok : plugin.getBlokManager().getBloks()) {
				if (!worldName.equals(blok.getWorldName()))
					continue;
				Blok newBlok = new Blok(plugin.getBlokManager().getNextId(), world.getName(), blok.getX(), blok.getY(), blok.getZ());
				toAdd.add(newBlok);
				plugin.debug("Created instanced block. Id: " + newBlok.getId() + " Location: " + newBlok.getLocation().toString());
			}
		}
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				for (Blok blok : toAdd) {
					plugin.getBlokManager().addBlok(blok);
				}
			}
		}, 1);
	}
}
