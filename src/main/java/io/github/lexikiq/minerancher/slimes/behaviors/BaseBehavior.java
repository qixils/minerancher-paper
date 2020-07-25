package io.github.lexikiq.minerancher.slimes.behaviors;

import io.github.lexikiq.minerancher.Minerancher;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BaseBehavior extends BukkitRunnable {
	protected final Minerancher plugin;
	protected final Slime slime;

	public BaseBehavior(Minerancher plugin, Slime slime) {
		this.slime = slime;
		this.plugin = plugin;
	}
}