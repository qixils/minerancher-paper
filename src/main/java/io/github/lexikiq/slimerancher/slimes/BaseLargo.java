package io.github.lexikiq.slimerancher.slimes;

import io.github.lexikiq.slimerancher.SlimeRancher;
import io.github.lexikiq.slimerancher.SlimeType;
import org.bukkit.entity.Slime;

public abstract class BaseLargo extends BaseSlime {
    public BaseLargo(Slime slime, SlimeRancher plugin) {
        super(slime, plugin);
    }

    @Override
    public SlimeType getType() {return SlimeType.LARGO;}

    @Override
    public int getSize() {
        return 2;
    }
}
