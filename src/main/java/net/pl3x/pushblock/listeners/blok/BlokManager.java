package net.pl3x.pushblock.listeners.blok;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlokManager {

    private List<Blok> bloks = new ArrayList<>();

    public Blok addBlok(Blok blok) {
        bloks.add(blok);
        return blok;
    }

    public void removeBlok(Blok blok) {
        if (bloks.contains(blok)) {
            blok.remove();
            bloks.remove(blok);
        }
    }

    public Blok getBlok(Block block) {
        for (Blok blok : bloks) {
            if (blok.getLocation() == null)
                continue;
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

    public List<Blok> getBloks() {
        return bloks;
    }

    public int getNextId() {
        for (int i = 0; i >= 0; i++)
            if (getBlokById(i) == null)
                return i;
        return 0;
    }

    public HashMap<BlockFace, BlockFace> getNearbyBloks(Location loc) {
        HashMap<BlockFace, BlockFace> nearbyBloks = new HashMap<>();
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
