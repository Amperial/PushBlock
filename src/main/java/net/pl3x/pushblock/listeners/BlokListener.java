package net.pl3x.pushblock.listeners;

import net.pl3x.pushblock.PushBlock;
import net.pl3x.pushblock.listeners.blok.Blok;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class BlokListener implements Listener {

    private PushBlock plugin;

    public BlokListener(PushBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        if (blocks.isEmpty())
            return;
        BlockFace direction = event.getDirection();
        for (int i = event.getLength() - 1; i >= 0; i--) { // MUST iterate backwards!!
            Blok blok = plugin.getBlokManager().getBlok(blocks.get(i));
            if (blok == null)
                continue;
            blok.setLocation(blocks.get(i).getRelative(direction).getLocation());
            plugin.debug("Moved block. ID: " + blok.getId() + " Location: " + blok.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        Block piston = event.getBlock();
        if (!piston.getType().equals(Material.PISTON_STICKY_BASE))
            return;
        Block block = piston.getRelative(event.getDirection());
        Blok blok = plugin.getBlokManager().getBlok(block.getRelative(event.getDirection()));
        if (blok == null) {
            plugin.debug("null");
            return;
        }
        blok.setLocation(block.getLocation());
        plugin.debug("Moved block. ID: " + blok.getId() + " Location: " + blok.getLocation());
    }

}
