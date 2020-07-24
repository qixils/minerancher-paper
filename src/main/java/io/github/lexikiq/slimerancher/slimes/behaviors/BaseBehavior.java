package io.github.lexikiq.slimerancher.slimes.behaviors;

import io.github.lexikiq.slimerancher.SlimeRancher;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseBehavior extends BukkitRunnable {
	protected final SlimeRancher plugin;
	protected final Slime slime;

	public BaseBehavior(SlimeRancher plugin, Slime slime) {
		this.slime = slime;
		this.plugin = plugin;
	}
}