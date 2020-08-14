package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import io.github.lexikiq.minerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.minerancher.slimes.behaviors.SlimeBehavior;
import lombok.Data;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;

public @Data abstract class BaseSlime {
    protected final Slime slime;
    protected int size = 1;
    protected SlimeType type = SlimeType.SLIME;
    protected Class<? extends BaseBehavior> behavior = SlimeBehavior.class; // class to run the behavior loop on

    protected long behaviorDelay = 2; // delay between runs of the behavior code in ticks
    protected BukkitTask behaviorTask = null; // current bukkit task
    protected boolean wild = true; // will be false if it has been in a corral ig

    public BaseSlime(Slime slime, Minerancher plugin) {
        this.slime = slime;
        initializeData(); // initialize variables from subclasses
        // set entity's properties
        slime.setSize(size);
        slime.setCustomName(getDisplayName());
        slime.setCustomNameVisible(false);
        // run the behavior loop (this is really jank i know)
        try {
            BaseBehavior behavior = getBehavior().getConstructor(Minerancher.class, Slime.class).newInstance(plugin, slime);
            behaviorTask = behavior.runTaskTimer(plugin, 0, behaviorDelay);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected abstract void initializeData(); // for subclasses to set variables like size, type, etc

    public String getDisplayName() {return getType().toString();}

    public void destroy() { // stops the current task. called when slime is being deleted
        if (behaviorTask != null)
            behaviorTask.cancel();
    }
}