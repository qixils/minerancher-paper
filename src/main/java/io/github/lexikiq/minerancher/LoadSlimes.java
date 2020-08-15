package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
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
	public LoadSlimes(Minerancher plugin) {this.plugin=plugin;}

	@Override
	public void run() {
		List<Entity> nearbyEntities = new ArrayList<Entity>();
		List<Entity> farawayEntities = new ArrayList<Entity>();
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			nearbyEntities.addAll(player.getNearbyEntities(TARGET_SEARCH_RANGE, TARGET_SEARCH_HEIGHT, TARGET_SEARCH_RANGE));
			farawayEntities.addAll(player.getNearbyEntities(TARGET_SEARCH_RANGE * 2, TARGET_SEARCH_HEIGHT * 2, TARGET_SEARCH_RANGE * 2));
		}

		List<Entity> scanEntities = new ArrayList<Entity>();
		scanEntities.addAll(nearbyEntities);
		scanEntities.addAll(farawayEntities);

		for (Entity entity : nearbyEntities) {
			farawayEntities.remove(entity);
		}

		for (Entity entity : scanEntities) {
			if (entity.getType() != EntityType.SLIME)
				continue;
			UUID key = entity.getUniqueId();
			if (plugin.loadedSlimes.containsKey(key))
				continue;
			SlimeType enumType = SlimeType.getByName(entity.getCustomName());
			if (enumType != null) plugin.registerSlime((Slime) entity, enumType);
		}

		// remove wild slimes far away from any player
		for (Entity entity : farawayEntities) {
			BaseSlime slime = plugin.loadedSlimes.getOrDefault(entity.getUniqueId(), null);
			if (slime == null)
				continue;
			if (!slime.isWild())
				continue;
			plugin.removeSlime(slime);
			slime.getSlime().remove();
		}
	}
}
