package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import org.bukkit.entity.Slime;

public class PinkSlime extends BaseSlime {
    public PinkSlime(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    protected void initializeData() {
        name = "Pink";
    }
}
