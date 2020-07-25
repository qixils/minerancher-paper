package io.github.lexikiq.minerancher.slimes.behaviors;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class TarrBehavior extends BaseBehavior {
    // search range for
    final int TARGET_SEARCH_RANGE = 25;
    final float TARGET_SEARCH_HEIGHT = (float) TARGET_SEARCH_RANGE / 2;

    public TarrBehavior(Minerancher plugin, Slime slime) {
        super(plugin, slime);
    }

    private double entityDistance(Entity mob1, Entity mob2) {
        return mob1.getLocation().distance(mob2.getLocation());
    }

    private BukkitTask currentTask;
    @Override
    public void run() {
        // this function manages the Tarr's hunting behavior

        // first, we don't want to do any behavior if an eating task is in progress
        if (currentTask != null) {
            if (currentTask.isCancelled())
                currentTask = null;
            else
                return;
        }

        // get tarr's current target (may be null)
        Entity targetedEntity = slime.getTargetEntity(TARGET_SEARCH_RANGE);
        // clear target if it is/has become a Tarr
        if (SlimeType.TARR.isType(targetedEntity)) {
            slime.setTarget(null);
        }
        if (targetedEntity != null)
            return; // Tarr already has a target, ignore

        // find nearby mobs
        List<Entity> nearbyEntities = slime.getNearbyEntities(TARGET_SEARCH_RANGE, TARGET_SEARCH_HEIGHT, TARGET_SEARCH_RANGE);
        // sort them
        nearbyEntities.sort((Entity e1, Entity e2) -> (int) (entityDistance(e1, slime) - entityDistance(e2, slime)));
        for (Entity target : nearbyEntities) {
            // if target isn't a slime/is a Tarr/is in a cutscene (has no AI), skip it
            if (target.getType() != EntityType.SLIME || SlimeType.TARR.isType(target) || !((Slime) target).hasAI()) {
                continue;
            }
            currentTask = new TarrEat(plugin, slime.getUniqueId(), target.getUniqueId(), TARGET_SEARCH_RANGE).runTaskTimer(plugin, 0, 3);
        }
    }
}