package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import org.bukkit.entity.Slime;

public class HoneySlime extends BaseSlime {
    public HoneySlime(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    protected void initializeData() {
        name = "Honey";
    }
}
