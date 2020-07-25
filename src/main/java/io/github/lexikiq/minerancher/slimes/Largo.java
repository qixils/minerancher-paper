package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

public class Largo extends BaseSlime {
    public Largo(Slime slime, Minerancher plugin) {
        super(slime, plugin);
        // todo: this will be called if type is unknown, so find the type
    }

    public Largo(Slime slime, Minerancher plugin, SlimeType base1, SlimeType base2) {
        super(slime, plugin);
        // todo: rename entity and stuff
    }

    @Override
    protected void initializeData() {
        size = 2;
        type = SlimeType.LARGO;
    }
}
