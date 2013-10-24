package net.pl3x.pushblock.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.pl3x.pushblock.PushBlock;
import net.pl3x.pushblock.listeners.blok.Blok;
import net.pl3x.pushblock.utils.Utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {
	private PushBlock plugin;
	private HashMap<Player,BlockFace> near = new HashMap<Player,BlockFace>();
	
	public PlayerListener(PushBlock plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("pushblock.push"))
			return;
		if (!near.containsKey(player) &&
				event.getTo().getBlockX() == event.getFrom().getBlockX() &&
				event.getTo().getBlockY() == event.getFrom().getBlockY() &&
				event.getTo().getBlockZ() == event.getFrom().getBlockZ())
			return;
		HashMap<BlockFace,BlockFace> nearbyBloks = plugin.getBlokManager().getNearbyBloks(event.getTo());
		if (nearbyBloks == null || nearbyBloks.isEmpty()) {
			if (near.containsKey(player))
				near.remove(player);
			return;
		}
		for (BlockFace face : nearbyBloks.keySet()) {
			BlockFace elevationCorrection = nearbyBloks.get(face);
			near.put(player, face);
			float closeness = Utils.getCloseness(face, event.getTo());
			if (closeness >= .31)
				continue;
			Block oldBlock = event.getTo().getBlock().getRelative(face).getRelative(elevationCorrection);
			Block newBlock = oldBlock.getRelative(face);
			Blok blok = plugin.getBlokManager().getBlok(oldBlock);
			if (!newBlock.getType().equals(Material.AIR))
				continue;
			Material type = oldBlock.getType();
			oldBlock.setType(Material.AIR);
			newBlock.setType(type);
			blok.setLocation(newBlock.getLocation());
			if (near.containsKey(player))
				near.remove(player);
		}
	}
	
	/*
	 * Fix stacking issue when player renames
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if(!(event.getInventory() instanceof AnvilInventory))
			return;
		HumanEntity ent = event.getWhoClicked();
		if (!(ent instanceof Player))
			return;
		final Player player = (Player) ent;
		int rawSlot = event.getRawSlot();
		if(rawSlot != event.getView().convertSlot(rawSlot))
			return;
		if(rawSlot != 2)
			return;
		ItemStack item = event.getCurrentItem();
		if(item == null)
			return;
		if (!Utils.hasName(item.getItemMeta()))
			return;
		if (!player.hasPermission("pushblock.create")) {
			player.sendMessage(plugin.colorize("&4You do not have permission to create this item!"));
			event.setCancelled(true);
			return;
		}
		List<String> lore = new ArrayList<String>();
		lore.add("Pushable");
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Repairable)
			((Repairable) meta).setRepairCost(0);
		meta.setDisplayName(null);
		meta.setLore(lore);
		item.setItemMeta(meta);
		event.setCurrentItem(item);
		plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.updateInventory();
				plugin.debug(player.getName() + " made block.");
			}
		},0);
	}
	
	/*
	 * Saves block data when placed
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (!Utils.hasLore(item.getItemMeta()))
			return;
		Player p = event.getPlayer();
		if (!p.hasPermission("pushblock.place")) {
			p.sendMessage(plugin.colorize("&4You do not have permission to place this item!"));
			event.setCancelled(true);
			return;
		}
		Block block = event.getBlock();
		Location loc = block.getLocation();
		Blok blok = plugin.getBlokManager().getBlok(loc.getBlock());
		if (blok == null)
			blok = plugin.getBlokManager().addBlok(new Blok(loc, plugin.getBlokManager().getNextId()));
		blok.setLocation(loc);
		plugin.debug(p.getName() + " placed block: " + (int) loc.getX() + "," + (int) loc.getY() + "," + (int) loc.getZ());
	}
	
	/*
	 * Restores name data when broke
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		Blok blok = plugin.getBlokManager().getBlok(loc.getBlock());
		if (blok == null) {
			plugin.debug("null");
			return;
		}
		Player p = event.getPlayer();
		if (!p.hasPermission("pushblock.break")) {
			p.sendMessage(plugin.colorize("&4You do not have permission to break this item!"));
			event.setCancelled(true);
			return;
		}
		blok.remove();
		plugin.getBlokManager().removeBlok(blok);
		plugin.debug(p.getName() + " broke block: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			return;
		ItemStack item = new ItemStack(blok.getType(), 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add("Pushable");
		meta.setLore(lore);
		if (meta instanceof Repairable)
			((Repairable) meta).setRepairCost(0);
		item.setItemMeta(meta);
		loc.getWorld().dropItemNaturally(loc, item);
		event.getBlock().setType(Material.AIR);
	}
}
