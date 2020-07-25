package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

public class HoneySlime extends BaseSlime {
    public HoneySlime(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    protected void initializeData() {
        type= SlimeType.HONEY_SLIME;
    }
}
