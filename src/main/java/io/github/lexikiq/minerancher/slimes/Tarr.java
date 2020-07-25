package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import io.github.lexikiq.minerancher.slimes.behaviors.BaseBehavior;
import io.github.lexikiq.minerancher.slimes.behaviors.TarrBehavior;
import org.bukkit.entity.Slime;

public class Tarr extends BaseLargo {
    public Tarr(Slime slime, Minerancher plugin) {
        super(slime, plugin);
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        name = "Tarr";
        type = SlimeType.TARR;
    }

    @Override
    public String getDisplayName() {return getName();}

    @Override
    protected Class<? extends BaseBehavior> getBehavior() {
        return TarrBehavior.class;
    }
}
