package io.github.lexikiq.minerancher;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadSlimes extends BukkitRunnable {
	private final int TARGET_SEARCH_RANGE = 200; // loads slimes in a +- (x) box
	private final float TARGET_SEARCH_HEIGHT = (float) TARGET_SEARCH_RANGE / 2;
	private final Minerancher plugin;
	// private String rID;
	public LoadSlimes(Minerancher plugin) {this.plugin=plugin;}

	// private void debugLog(String message) {
	// 	plugin.getServer().broadcastMessage(String.format("%s%s: %s", ChatColor.LIGHT_PURPLE, rID, message));
	// }

	@Override
	public void run() {
		// ID of the current run (just a random string LOL)
		// rID = UUID.randomUUID().toString().substring(0, 8);

		List<UUID> checkedSlimes = new ArrayList<>();
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			// find nearby entities
			for (Entity entity : player.getNearbyEntities(TARGET_SEARCH_RANGE, TARGET_SEARCH_HEIGHT, TARGET_SEARCH_RANGE)) {
				UUID key = entity.getUniqueId();
				// we only want slimes who haven't already been loaded
				if (entity.getType() == EntityType.SLIME && !checkedSlimes.contains(key) && !plugin.loadedSlimes.containsKey(key)) {
					checkedSlimes.add(key);  // is checking checkedSlims actually faster? idk
					SlimeType enumType = SlimeType.getByName(entity.getCustomName());
					if (enumType != null) plugin.registerSlime((Slime) entity, enumType);
				}
			}
		}
	}
}
