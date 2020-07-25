package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import io.github.lexikiq.minerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.minerancher.slimes.behaviors.SlimeBehavior;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;

public abstract class BaseSlime { // should this be abstract ?
    protected final Slime slime;
    protected BukkitTask behaviorTask = null;
    public BaseSlime(Slime slime, Minerancher plugin) {
        slime.setSize(getSize());
        slime.setCustomName(getDisplayName());
        slime.setCustomNameVisible(false);
        this.slime = slime;
        try {
            BaseBehavior behavior = getBehavior().getConstructor(Minerancher.class, Slime.class).newInstance(plugin, slime);
            behaviorTask = behavior.runTaskTimer(plugin, 0, getBehaviorDelay());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected long getBehaviorDelay() {return 2;}

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