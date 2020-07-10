package io.github.lexikiq.slimerancher;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SlimeRancher extends JavaPlugin {
    @Override
    public void onEnable() {
        new BasicListeners(this);
        new TarrHunt(this).runTaskTimer(this, 0, 20);
        Bukkit.getServer().getLogger().info("Slime Rancher loaded!");
    }
}
