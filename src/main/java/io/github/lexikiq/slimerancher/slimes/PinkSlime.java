package io.github.lexikiq.slimerancher.slimes;

import io.github.lexikiq.slimerancher.SlimeRancher;
import org.bukkit.entity.Slime;

public class PinkSlime extends BaseSlime {
    public PinkSlime(Slime slime, SlimeRancher plugin) {
        super(slime, plugin);
    }

    @Override
    public String getName() {return "Pink";}
}
