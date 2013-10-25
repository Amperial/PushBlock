package net.pl3x.pushblock.listeners;

import java.util.ArrayList;
import java.util.List;

import net.pl3x.pushblock.PushBlock;
import net.pl3x.pushblock.listeners.blok.Blok;

import org.bukkit.Location;
import org.bukkit.Material;
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
		if (!plugin.getConfig().getBoolean("allow-instanced-worlds", false))
			return;
		World world = event.getWorld();
		String name = world.getName();
		if (!name.contains("."))
			return;
		List<Blok> toRemove = new ArrayList<Blok>();
		for (Blok blok : plugin.getBlokManager().getBloks()) {
			if (!blok.getLocation().getWorld().equals(world))
				continue;
			toRemove.add(blok);
			blok.setType(Material.AIR);
			plugin.debug("Removed instanced block. Id: " + blok.getId());
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
		String name = world.getName();
		if (!name.contains("."))
			return;
		String[] str = name.split("\\.");
		name = str[0];
		List<Blok> toAdd = new ArrayList<Blok>();
		for (Blok blok : plugin.getBlokManager().getBloks()) {
			if (!blok.getLocation().getWorld().getName().equals(name))
				continue;
			Location originalLoc = blok.getOriginalLocation().clone();
			if (originalLoc == null)
				continue;
			originalLoc.setWorld(world);
			Blok newBlok = new Blok(originalLoc, plugin.getBlokManager().getNextId());
			toAdd.add(newBlok);
			newBlok.setType(blok.getType());
			plugin.debug("Created instanced block. Id: " + newBlok.getId() + " Location: " + newBlok.getLocation().toString());
		}
		for (Blok blok : toAdd) {
			plugin.getBlokManager().addBlok(blok);
		}
	}
}
