package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import io.github.lexikiq.minerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.minerancher.slimes.behaviors.SlimeBehavior;
import lombok.Data;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;

public @Data abstract class BaseSlime { // should this be abstract ?
    protected final Slime slime;
    protected BukkitTask behaviorTask = null;
    protected long behaviorDelay = 2;
    protected SlimeType type = SlimeType.SLIME;
    protected String name;
    protected int size = 1;
    protected Class<? extends BaseBehavior> behavior;
    protected boolean wild = true;

    public BaseSlime(Slime slime, Minerancher plugin) {
        initializeData();
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

    protected abstract void initializeData();

    public String getDisplayName() {return getName() + " " + getType();} // todo: string format ?

    protected Class<? extends BaseBehavior> getBehavior() {
        return SlimeBehavior.class;
    }

    public void destroy() {
        if (behaviorTask != null)
            behaviorTask.cancel();
    }
}