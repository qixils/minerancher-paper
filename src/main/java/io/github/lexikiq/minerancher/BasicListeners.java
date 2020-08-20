package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.BaseSlime;
import io.github.lexikiq.minerancher.slimes.Largo;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

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
        if (event.getEntityType() == EntityType.PHANTOM) {
            event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.SLIME);
            event.setCancelled(true);
        } else if (event.getEntityType() == EntityType.SLIME) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
                event.setCancelled(true);
            } else {
                Slime slime = (Slime) event.getEntity();
                SlimeType toRegister;
                int nextInt = rand.nextInt(100);
                if (nextInt < 10) {
                    toRegister = SlimeType.TARR;
                } else if (nextInt < 32) {
                    toRegister = SlimeType.PINK_SLIME;
                } else if (nextInt < 55) {
                    toRegister = SlimeType.TABBY_SLIME;
                } else if (nextInt < 77) {
                    toRegister = SlimeType.ROCK_SLIME;
                } else {
                    toRegister = SlimeType.PHOSPHOR_SLIME;
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
            Slime entity = slime.getSlime();
            int slimeballCount = rand.nextInt(4);
            if (slimeballCount > 0) {
                ItemStack slimeballs = new ItemStack(Material.SLIME_BALL, slimeballCount);
                entity.getWorld().dropItemNaturally(entity.getLocation(), slimeballs);
            }
            slime.destroy();
            plugin.loadedSlimes.remove(key);
            slime.getSlime().remove(); // workaround for semicommon bug where slimes will ignore death
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.SLIME) {
            Slime slime = (Slime) event.getRightClicked();

            // remove name tagged slimes from processing
            if (SlimeType.getByName(slime) == null) { plugin.removeSlime(slime); }

            // ignore non-lexi players from debug behavior
            if (!event.getPlayer().getUniqueId().equals(UUID.fromString("d1de9ca8-78f6-4aae-87a1-8c112f675f12"))) return;

            // debug behavior: convert slime to new type
            if (event.getPlayer().isSneaking()) {
                plugin.registerSlime(slime, SlimeType.TARR, true);
            } else {
                plugin.registerSlime(slime, new Largo(slime, plugin, SlimeType.PINK_SLIME, SlimeType.ROCK_SLIME));
            }
            // slime.setCustomName("\uF801Largo\uF802");
            // for (char c : slime.getCustomName().toCharArray()) System.out.println(c);
            //slime.setCustomName(ChatColor.AQUA.toString()+"Largo");
            //for (char i : Objects.requireNonNull(slime.getCustomName()).toCharArray()) {
            //    plugin.getLogger().info(String.valueOf(i));
            //}
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntityType() == EntityType.SLIME) {
            event.setCancelled(true);
        }
    }
}
