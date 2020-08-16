package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
import io.github.lexikiq.minerancher.slimes.Tarr;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Random;
import java.util.UUID;

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
                SlimeType toRegister;
                int nextInt = rand.nextInt(3);
                switch (nextInt) {
                    case 0: toRegister = SlimeType.PINK_SLIME; break;
                    case 1: toRegister = SlimeType.HONEY_SLIME; break;
                    case 2: toRegister = SlimeType.ROCK_SLIME; break;
                    default: // make intellij not complain about unknown values
                        throw new IllegalStateException("Unexpected value??: " + nextInt);
                }
                plugin.registerSlime(slime, toRegister);
            }
        }
    }

    @EventHandler
    public void onMobTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity().getType() == EntityType.SLIME) {
            event.setCancelled(true);
        }
    }

    @EventHandler()
    public void onMobDeath(EntityDeathEvent event) {
        // properly remove slime instances on death
        UUID key = event.getEntity().getUniqueId();
        BaseSlime slime = plugin.loadedSlimes.get(key);
        if (slime != null) {
            slime.destroy();
            plugin.loadedSlimes.remove(key);
            slime.getSlime().remove(); // workaround for semicommon bug where slimes will ignore death
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.SLIME) {
            Slime slime = (Slime) event.getRightClicked();
            BaseSlime loadedSlime = plugin.loadedSlimes.get(slime.getUniqueId());
            if (loadedSlime != null && loadedSlime.getType() == SlimeType.TARR) return; // avoid duplicate entries
            plugin.registerSlime(slime, new Tarr(slime, plugin));
            //slime.setCustomName(ChatColor.AQUA.toString()+"Largo");
            //for (char i : Objects.requireNonNull(slime.getCustomName()).toCharArray()) {
            //    plugin.getLogger().info(String.valueOf(i));
            //}

        }
    }
}
