package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimes.BaseSlime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class SlimeRancher extends JavaPlugin {
    public HashMap<UUID, BaseSlime> loadedSlimes = new HashMap<UUID, BaseSlime>();

    @Override
    public void onEnable() {
        new BasicListeners(this);
        Bukkit.getServer().getLogger().info("Slime Rancher loaded!");
    }
}
