package io.github.lexikiq.slimerancher.slimes;

import io.github.lexikiq.slimerancher.SlimeRancher;
import org.bukkit.entity.Slime;

public class HoneySlime extends BaseSlime {
    public HoneySlime(Slime slime, SlimeRancher plugin) {
        super(slime, plugin);
    }

    @Override
    public String getName() {return "Honey";}
}
