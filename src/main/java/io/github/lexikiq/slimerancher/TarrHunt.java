package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimetypes.Tarr;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TarrHunt extends BukkitRunnable {

    private final JavaPlugin plugin;
    private Map<UUID, BukkitTask> runningTasks = new HashMap<UUID, BukkitTask>();

    public TarrHunt(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private double entityDistance(Entity mob1, Entity mob2) {
        return mob1.getLocation().distance(mob2.getLocation());
    }

    @Override
    public void run() {
        // this function manages the Tarr's hunting behavior

        final int ENTITY_SEARCH_RANGE = 200;
        final int TARGET_SEARCH_RANGE = 25;
        final float ENTITY_SEARCH_HEIGHT = (float) ENTITY_SEARCH_RANGE / 4;
        final float TARGET_SEARCH_HEIGHT = (float) TARGET_SEARCH_RANGE / 2;

        // clean out cancelled tasks
        List<UUID> keysToRemove = new ArrayList<>();
        // step 1- get keys to clean out
        for (Map.Entry<UUID, BukkitTask> entry : runningTasks.entrySet()) {
            if (entry.getValue().isCancelled()) {
                keysToRemove.add(entry.getKey());
            }
        }
        // step 2- clean em out
        for (UUID entry : keysToRemove) {
            runningTasks.remove(entry);
        }

        // first, start by finding all Tarrs around all online players
        for (Player player : plugin.getServer().getOnlinePlayers()) { // get all online players
            // search for every entity near them
            for (Entity entity : player.getNearbyEntities(ENTITY_SEARCH_RANGE, ENTITY_SEARCH_HEIGHT, ENTITY_SEARCH_RANGE)) {
                if (!new Tarr().isSlimeEntity(entity)) { continue; } // if it's not a Tarr, we don't care
                Slime mob = (Slime) entity; // (convert to slime)

                // get target
                Entity targetedEntity = mob.getTargetEntity(TARGET_SEARCH_RANGE);
                // clear target if it is/has become a Tarr
                if (targetedEntity != null && new Tarr().isSlimeEntity(targetedEntity)) {
                    mob.setTarget(null);
                    targetedEntity = null; // idk if this is necessary?
                }
                if (targetedEntity != null) { continue; } // Tarr already has a target, ignore

                // find nearby mobs
                List<Entity> nearbyEntities = mob.getNearbyEntities(TARGET_SEARCH_RANGE, TARGET_SEARCH_HEIGHT, TARGET_SEARCH_RANGE);
                // sort them
                nearbyEntities.sort((Entity e1, Entity e2) -> (int) (entityDistance(e1, mob) - entityDistance(e2, mob)));
                for (Entity target : nearbyEntities) {
                    // if target isn't a slime/is a Tarr/is in a cutscene (has no AI), skip it
                    if (target.getType() != EntityType.SLIME || new Tarr().isSlimeEntity(target) || !((Slime) target).hasAI()) {
                        continue;
                    }
                    if (!(runningTasks.containsKey(mob.getUniqueId()))) { // cant use putIfEmpty or whatever bc it generates the task anyway
                        mob.setTarget((Slime) target);
                        runningTasks.put(mob.getUniqueId(), new TarrEat(plugin, mob.getUniqueId(), target.getUniqueId(), TARGET_SEARCH_RANGE).runTaskTimer(plugin, 0, 3));
                    }
                    break; // target has been set, break loop
                }
            }
        }
    }
}