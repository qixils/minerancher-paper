package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

public abstract class BaseLargo extends BaseSlime {
    public BaseLargo(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    public SlimeType getType() {return SlimeType.LARGO;}

    @Override
    public int getSize() {
        return 2;
    }
}
