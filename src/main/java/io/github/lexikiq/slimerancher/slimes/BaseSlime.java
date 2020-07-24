package io.github.lexikiq.slimerancher.slimes;

import io.github.lexikiq.slimerancher.SlimeRancher;
import io.github.lexikiq.slimerancher.SlimeType;
import io.github.lexikiq.slimerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.slimerancher.slimes.behaviors.SlimeBehavior;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;

public abstract class BaseSlime { // should this be abstract ?
    protected final Slime slime;
    protected BukkitTask behaviorTask = null;
    public BaseSlime(Slime slime, SlimeRancher plugin) {
        slime.setSize(getSize());
        slime.setCustomName(getDisplayName());
        slime.setCustomNameVisible(false);
        this.slime = slime;
        try {
            BaseBehavior behavior = getBehavior().getConstructor(SlimeRancher.class, Slime.class).newInstance(plugin, slime);
            behaviorTask = behavior.runTaskTimer(plugin, 0, 20);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public SlimeType getType() {return SlimeType.SLIME;}

    public String getName() {return "";}

    public String getDisplayName() {return getName() + " " + getType();} // todo: string format ?

    public int getSize() {return 1;}

    protected Class<? extends BaseBehavior> getBehavior() {
        return SlimeBehavior.class;
    }

    public void destroy() {
        if (behaviorTask != null)
            behaviorTask.cancel();
    }
}