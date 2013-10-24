package net.pl3x.pushblock.listeners.blok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlokManager {
	private List<Blok> bloks = new ArrayList<Blok>();
	
	public Blok addBlok(Blok blok) {
		if (bloks.contains(bloks))
			return null;
		bloks.add(blok);
		return blok;
	}
	
	public void removeBlok(Blok blok) {
		if (!bloks.contains(blok))
			return;
		blok.remove();
		bloks.remove(blok);
	}
	
	public Blok getBlok(Block block) {
		for (Blok blok : bloks) {
			if (blok.getLocation().equals(block.getLocation()))
				return blok;
		}
		return null;
	}
	
	public Blok getBlokById(int id) {
		for (Blok blok : bloks)
			if (blok.getId() == id)
				return blok;
		return null;
	}
	
	public int getNextId() {
		for(int i = 0; i >= 0; i++)
			if(getBlokById(i) == null)
				return i;
		return 0;
	}
	
	public HashMap<BlockFace,BlockFace> getNearbyBloks(Location loc) {
		HashMap<BlockFace,BlockFace> nearbyBloks = new HashMap<BlockFace,BlockFace>();
		for (Blok blok : bloks) {
			BlockFace face = blok.checkNear(loc.getBlock());
			BlockFace elevationCorrection = BlockFace.SELF;
			if (face == null) {
				face = blok.checkNearUpper(loc.getBlock());
				if (face == null)
					continue;
				elevationCorrection = BlockFace.UP;
			}
			nearbyBloks.put(face, elevationCorrection);
		}
		return nearbyBloks;
	}
}
