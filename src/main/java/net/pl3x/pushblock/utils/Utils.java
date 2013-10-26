package net.pl3x.pushblock.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	public static float getCloseness(BlockFace face, Location loc) {
		float closeness = 1;
		switch (face) {
			case NORTH:
				closeness = (float) (loc.getZ() % 1);
				break;
			case SOUTH:
				closeness = 1 - (float) (loc.getZ() % 1);
				break;
			case EAST:
				closeness = 1 - (float) (loc.getX() % 1);
				break;
			case WEST:
				closeness = (float) (loc.getX() % 1);
				break;
			default:
		}
		if (closeness < 0)
			closeness++;
		if (closeness > 1)
			closeness--;
		return closeness;
	}
	
	public static boolean hasLore(ItemMeta meta) {
		if (meta == null)
			return false;
		List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return false;
		if (lore.get(0).equals("Pushable"))
			return true;
		return false;
	}
	
	public static boolean hasName(ItemMeta meta) {
		if (meta == null || meta.getDisplayName() == null)
			return false;
		if (meta.getDisplayName().startsWith("Pushable"))
			return true;
		return false;
	}
	
	public static BlockFace getDirection(ItemMeta meta) {
		if (!hasName(meta))
			return null;
		String[] lore = meta.getDisplayName().split(" ");
		if (lore.length < 2)
			return null;
		if (lore[1].equalsIgnoreCase("N") || lore[1].equalsIgnoreCase("North"))
			return BlockFace.NORTH;
		if (lore[1].equalsIgnoreCase("S") || lore[1].equalsIgnoreCase("South"))
			return BlockFace.SOUTH;
		if (lore[1].equalsIgnoreCase("E") || lore[1].equalsIgnoreCase("East"))
			return BlockFace.EAST;
		if (lore[1].equalsIgnoreCase("W") || lore[1].equalsIgnoreCase("West"))
			return BlockFace.WEST;
		return null;
	}
	
	public static Integer getDistance(ItemMeta meta) {
		if (!hasName(meta))
			return null;
		String[] lore = meta.getDisplayName().split(" ");
		if (lore.length < 3)
			return null;
		Integer distance = null;
		try {
			distance = Integer.valueOf(lore[2]);
		} catch (Exception e) {
			return null;
		}
		return distance;
	}
}
