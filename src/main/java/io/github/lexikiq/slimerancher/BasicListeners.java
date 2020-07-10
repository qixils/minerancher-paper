package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimetypes.BaseSlime;
import io.github.lexikiq.slimerancher.slimetypes.HoneySlime;
import io.github.lexikiq.slimerancher.slimetypes.PinkSlime;
import io.github.lexikiq.slimerancher.slimetypes.Tarr;
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
    private Random rand = new Random();

    public BasicListeners(SlimeRancher plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event)
    {
        if (event.getEntityType() == EntityType.SLIME) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
                event.setCancelled(true);
            } else {
                Slime slime = (Slime) event.getEntity();
                BaseSlime slimeType = rand.nextBoolean() ? new PinkSlime() : new HoneySlime();
                slimeType.convertSlimeEntity(slime);
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
            new Tarr().convertSlimeEntity(slime);
        }
    }

    @EventHandler
    public void onMobDeath(EntityDamageByEntityEvent event) {
        Tarr tarr = new Tarr();
        if (event.getEntityType() == EntityType.SLIME && event.getEntity().isDead() && tarr.isSlimeEntity(event.getDamager())) {
            Slime slime = (Slime) event.getEntity();
            event.setCancelled(true);
            tarr.convertSlimeEntity(slime);
        }
    }
}
