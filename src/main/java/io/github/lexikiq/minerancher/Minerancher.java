package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Minerancher extends JavaPlugin {
    public HashMap<UUID, BaseSlime> loadedSlimes = new HashMap<UUID, BaseSlime>();

    @Override
    public void onEnable() {
        new BasicListeners(this); // event listener
        Bukkit.getServer().getLogger().info("Minerancher initialized!");
    }
}
