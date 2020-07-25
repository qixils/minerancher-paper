package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
import io.github.lexikiq.minerancher.slimes.HoneySlime;
import io.github.lexikiq.minerancher.slimes.PinkSlime;
import io.github.lexikiq.minerancher.slimes.Tarr;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Random;

public class BasicListeners implements Listener {
    private final Random rand = new Random();
    private final Minerancher plugin;

    public BasicListeners(Minerancher plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.SLIME) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
                event.setCancelled(true);
            } else {
                Slime slime = (Slime) event.getEntity();
                BaseSlime toRegister;
                if (rand.nextInt(2) == 0)
                    toRegister = new HoneySlime(slime, plugin);
                else
                    toRegister = new PinkSlime(slime, plugin);
                plugin.registerSlime(slime, toRegister);
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
            plugin.registerSlime(slime, new Tarr(slime, plugin));
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
