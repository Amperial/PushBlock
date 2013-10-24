package net.pl3x.pushblock.listeners.blok;

import net.pl3x.pushblock.configuration.ConfManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blok {
	private Location location;
	private Integer id;
	
	public Blok(Location location, int id) {
		this.location = location;
		this.id = id;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Material getType() {
		return location.getBlock().getType();
	}
	
	public void setLocation(Location location) {
		this.location = location;
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
