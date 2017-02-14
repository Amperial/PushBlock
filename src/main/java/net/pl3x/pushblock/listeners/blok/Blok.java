package net.pl3x.pushblock.listeners.blok;

import net.pl3x.pushblock.configuration.ConfManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blok {

    private Integer id;
    private String worldName;
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer ox;
    private Integer oy;
    private Integer oz;

    public Blok(int id, String worldName, int x, int y, int z) {
        this(id, worldName, x, y, z, x, y, z);
    }

    public Blok(Integer id, String worldName, int x, int y, int z, int ox, int oy, int oz) {
        this.id = id;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ox = ox;
        this.oy = oy;
        this.oz = oz;
        String idStr = id.toString();
        ConfManager cm = ConfManager.getConfManager();
        cm.set("blocks." + idStr + ".w", worldName);
        cm.set("blocks." + idStr + ".x", x);
        cm.set("blocks." + idStr + ".y", y);
        cm.set("blocks." + idStr + ".z", z);
        cm.set("blocks." + idStr + ".ox", ox);
        cm.set("blocks." + idStr + ".oy", oy);
        cm.set("blocks." + idStr + ".oz", oz);
        cm.forceSave();
    }

    public Integer getId() {
        return id;
    }

    public String getWorldName() {
        return worldName;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Material getType() {
        return getLocation().getBlock().getType();
    }

    public void setType(Material type) {
        getLocation().getBlock().setType(type);
    }

    public Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null)
            return null;
        return new Location(world, x, y, z);
    }

    public Location getOriginalLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null)
            return null;
        return new Location(world, ox, oy, oz);
    }

    public void setLocation(Location location) {
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
        String idStr = id.toString();
        ConfManager cm = ConfManager.getConfManager();
        cm.set("blocks." + idStr + ".w", location.getWorld().getName());
        cm.set("blocks." + idStr + ".x", x);
        cm.set("blocks." + idStr + ".y", y);
        cm.set("blocks." + idStr + ".z", z);
        cm.forceSave();
    }

    public void remove() {
        ConfManager cm = ConfManager.getConfManager();
        cm.set("blocks." + id.toString(), null);
        cm.forceSave();
        id = null;
        worldName = null;
        x = null;
        y = null;
        z = null;
        ox = null;
        oy = null;
        oz = null;
    }

    public void reset() {
        Block block = getLocation().getBlock();
        Material type = block.getType();
        block.setType(Material.AIR);
        x = ox;
        y = oy;
        z = oz;
        getLocation().getBlock().setType(type);
        String idStr = id.toString();
        ConfManager cm = ConfManager.getConfManager();
        cm.set("blocks." + idStr + ".w", worldName);
        cm.set("blocks." + idStr + ".x", x);
        cm.set("blocks." + idStr + ".y", y);
        cm.set("blocks." + idStr + ".z", z);
        cm.forceSave();
    }

    public BlockFace checkNear(Block block) {
        Location location = getLocation();
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
        Location location = getLocation();
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
