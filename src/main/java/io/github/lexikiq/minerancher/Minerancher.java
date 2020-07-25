package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

public final class Minerancher extends JavaPlugin {
    public HashMap<UUID, BaseSlime> loadedSlimes = new HashMap<UUID, BaseSlime>();
    private LoadSlimes loadSlimes;

    @Override
    public void onEnable() {
        new BasicListeners(this); // event listener
        Bukkit.getServer().getLogger().info("Minerancher initialized!");
        loadSlimes = new LoadSlimes(this);
        loadSlimes.runTaskTimer(this, 0, 3*20);
    }

    public void removeSlime(UUID entityUUID) {
        BaseSlime slime = loadedSlimes.getOrDefault(entityUUID, null);
        if (slime == null)
            return;
        // no isWild check here, that should be done elsewhere
        loadedSlimes.remove(entityUUID);
        slime.destroy();
    }

    public void removeSlime(@Nullable Entity entity) {
        if (entity == null)
            return;
        if (loadedSlimes.containsKey(entity.getUniqueId()))
            removeSlime(entity.getUniqueId());
    }

    public void removeSlime(@Nullable BaseSlime slime) {
        if (slime == null)
            return;
        if (loadedSlimes.containsValue(slime))
            removeSlime(slime.getSlime().getUniqueId());
    }

    private boolean deleteOldSlime(Slime entity, boolean forceDelete) {
        BaseSlime oldSlime = loadedSlimes.getOrDefault(entity.getUniqueId(), null);
        if (oldSlime != null && !forceDelete) {
            return false;
        } else if (oldSlime != null) {
            oldSlime.destroy();
            loadedSlimes.remove(entity.getUniqueId());
        }
        return true;
    }

    public void registerSlime(Slime entity, SlimeType newType, boolean forceRegister) {
        if (deleteOldSlime(entity, forceRegister)) {
            try {
                loadedSlimes.put(entity.getUniqueId(), newType.getSlimeClass().getConstructor(Slime.class, Minerancher.class).newInstance((Slime) entity, this));
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerSlime(Slime entity, SlimeType newType) {
        registerSlime(entity, newType, false);
    }

    public void registerSlime(Slime entity, BaseSlime newSlime) {
        // is it a bad idea to initialize allow initializing a class beforehand? is there a slight chance tasks could overlap? i don't *think* so...
        deleteOldSlime(entity, true);
        loadedSlimes.put(entity.getUniqueId(), newSlime);
    }
}
