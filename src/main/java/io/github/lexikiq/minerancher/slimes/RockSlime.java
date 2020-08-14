package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

public class RockSlime extends BaseSlime {
    // TODO: rock slimes should hurt players
    public RockSlime(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    protected void initializeData() {
        type= SlimeType.ROCK_SLIME;
    }
}
