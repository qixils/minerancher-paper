package io.github.lexikiq.slimerancher.slimes;

import io.github.lexikiq.slimerancher.SlimeRancher;
import io.github.lexikiq.slimerancher.SlimeType;
import io.github.lexikiq.slimerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.slimerancher.slimes.behaviors.TarrBehavior;
import org.bukkit.entity.Slime;

public class Tarr extends BaseLargo {
    public Tarr(Slime slime, SlimeRancher plugin) {
        super(slime, plugin);
    }

    @Override
    public String getName() {return "Tarr";}

    @Override
    public String getDisplayName() {return getName();}

    @Override
    public SlimeType getType() {return SlimeType.TARR;}

    @Override
    protected Class<? extends BaseBehavior> getBehavior() {
        return TarrBehavior.class;
    }
}
