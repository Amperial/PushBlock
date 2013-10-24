package net.pl3x.pushblock.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
	public static float getCloseness(BlockFace face, Location loc) {
		switch (face) {
			case NORTH:
				return (float) (loc.getZ() % 1);
			case SOUTH:
				return 1 - (float) (loc.getZ() % 1);
			case EAST:
				return 1 - (float) (loc.getX() % 1);
			case WEST:
				return (float) (loc.getX() % 1);
			default:
				return 1;
		}
	}
	
	public static boolean hasLore(ItemMeta meta) {
		if (meta == null)
			return false;
		List<String> lore = meta.getLore();
		if (lore == null || lore.isEmpty())
			return false;
		if (lore.get(0).equalsIgnoreCase("Pushable"))
			return true;
		return false;
	}
	
	public static boolean hasName(ItemMeta meta) {
		if (meta == null)
			return false;
		if (meta.getDisplayName() == null)
			return false;
		if (meta.getDisplayName().equals("Pushable"))
			return true;
		return false;
	}
}
