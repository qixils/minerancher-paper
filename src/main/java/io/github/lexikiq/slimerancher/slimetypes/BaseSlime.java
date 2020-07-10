package io.github.lexikiq.slimerancher.slimetypes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

public class BaseSlime {
    public String getType() {
        return "Slime";
    }

    public String getName() {
        return "";
    }

    public int getSize() {
        return 1;
    }

    public void convertSlimeEntity(Slime slimeEntity) {
        slimeEntity.setSize(getSize());
        slimeEntity.setCustomName(getName());
        slimeEntity.setCustomNameVisible(false);
    }

    public boolean isSlimeEntity(Entity entity) {
        return entity.getType() == EntityType.SLIME && entity.getCustomName() != null && entity.getCustomName().equals(getName());
    }
}