package net.pl3x.pushblock.listeners.blok;

import net.pl3x.pushblock.configuration.ConfManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blok {
	private Location originalLoc;
	private Location location;
	private Integer id;
	
	public Blok(Location location, int id) {
		this(location, location, id);
	}
	
	public Blok(Location originalLoc, Location location, Integer id) {
		this.id = id;
		this.originalLoc = originalLoc.clone();
		this.location = location.clone();
		ConfManager cm = ConfManager.getConfManager();
		cm.setLocation(id.toString(), location);
		cm.setLocation(id.toString() + ".original", location);
		cm.forceSave();
	}
	
	public Integer getId() {
		return id;
	}
	
	public Material getType() {
		return location.getBlock().getType();
	}
	
	public void setType(Material type) {
		location.getBlock().setType(type);
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Location getOriginalLocation() {
		return originalLoc;
	}
	
	public void setLocation(Location location) {
		this.location = location.clone();
		ConfManager cm = ConfManager.getConfManager();
		cm.setLocation(id.toString(), location);
		cm.forceSave();
	}
	
	public void remove() {
		ConfManager cm = ConfManager.getConfManager();
		cm.set("blocks." + id.toString(), null);
		cm.forceSave();
		id = null;
		location = null;
	}
	
	public void reset() {
		Block block = location.getBlock();
		Material type = block.getType();
		block.setType(Material.AIR);
		location = originalLoc.clone();
		location.getBlock().setType(type);
		ConfManager cm = ConfManager.getConfManager();
		cm.setLocation(id.toString(), location);
		cm.forceSave();
	}
	
	public BlockFace checkNear(Block block) {
		if (block.getRelative(BlockFace.NORTH).getLocation().equals(location))
			return BlockFace.NORTH;
		if (block.getRelative(BlockFace.SOUTH).getLocation().equals(location))
			return BlockFace.SOUTH;
		if (block.getRelative(BlockFace.EAST).getLocation().equals(location))
			return BlockFace.EAST;
		if (block.getRelative(BlockFace.WEST).getLocation().equals(location))
			return BlockFace.WEST;
		return null;
	}
	
	public BlockFace checkNearUpper(Block block) {
		block = block.getRelative(BlockFace.UP);
		if (block.getRelative(BlockFace.NORTH).getLocation().equals(location))
			return BlockFace.NORTH;
		if (block.getRelative(BlockFace.SOUTH).getLocation().equals(location))
			return BlockFace.SOUTH;
		if (block.getRelative(BlockFace.EAST).getLocation().equals(location))
			return BlockFace.EAST;
		if (block.getRelative(BlockFace.WEST).getLocation().equals(location))
			return BlockFace.WEST;
		return null;
	}
}
