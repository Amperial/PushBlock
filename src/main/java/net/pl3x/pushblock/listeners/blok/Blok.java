package net.pl3x.pushblock.listeners.blok;

import net.pl3x.pushblock.configuration.ConfManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blok {
	private Location location;
	private Material type;
	private Integer id;
	
	public Blok(Location location, Material type, int id) {
		this.location = location;
		this.type = type;
		this.id = id;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Material getType() {
		return type;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setLocation(Location location) {
		this.location = location;
		ConfManager cm = ConfManager.getConfManager();
		cm.setLocation(id.toString(), location);
		cm.forceSave();
	}
	
	public void setType(Material type) {
		this.type = type;
	}
	
	public void remove() {
		ConfManager cm = ConfManager.getConfManager();
		cm.set("blocks." + id.toString(), null);
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
