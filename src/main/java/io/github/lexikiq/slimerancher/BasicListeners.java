package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimes.HoneySlime;
import io.github.lexikiq.slimerancher.slimes.PinkSlime;
import io.github.lexikiq.slimerancher.slimes.Tarr;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;

public class BasicListeners implements Listener {
    private final Random rand = new Random();
    private final SlimeRancher plugin;

    public BasicListeners(SlimeRancher plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        // on world load, find slime entities
        for (Entity entity : event.getWorld().getEntitiesByClass(Slime.class)) {
            UUID key = entity.getUniqueId();
            if (!plugin.loadedSlimes.containsKey(key)) {
                for (SlimeType enumType : SlimeType.values()) {
                    if (enumType.isType(entity)) {
                        try {
                            plugin.loadedSlimes.put(key, enumType.getSlimeClass().getConstructor(Slime.class, SlimeRancher.class).newInstance((Slime) entity, plugin));
                        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.SLIME) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
                event.setCancelled(true);
            } else {
                Slime slime = (Slime) event.getEntity();
                switch (rand.nextInt(2)) {
                    case (0):
                        plugin.loadedSlimes.put(slime.getUniqueId(), new HoneySlime(slime, plugin));
                    default:
                        plugin.loadedSlimes.put(slime.getUniqueId(), new PinkSlime(slime, plugin));
                }
            }
        }
    }

    @EventHandler
    public void onMobTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntityType() == EntityType.SLIME) {
            Slime slime = (Slime) event.getEntity();
            if (!(slime.getCustomName() != null && slime.getCustomName().equals("Tarr"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.SLIME) {
            Slime slime = (Slime) event.getRightClicked();
            if (plugin.loadedSlimes.containsKey(slime.getUniqueId())) // this should always be true but just in case
                plugin.loadedSlimes.get(slime.getUniqueId()).destroy();
            plugin.loadedSlimes.put(slime.getUniqueId(), new Tarr(slime, plugin));
        }
    }

    @EventHandler
    public void onMobDeath(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.SLIME && event.getEntity().isDead()) {
            // Slime slime = (Slime) event.getEntity();
            event.setCancelled(true);
        }
    }
}
