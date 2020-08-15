package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

import java.util.ArrayList;

public class Largo extends BaseSlime {
    private String oldName;
    private ArrayList<SlimeType> bases = new ArrayList<>();

    public Largo(Slime slime, Minerancher plugin) {
        super(slime, plugin);
        // todo: this will be called if type is unknown, so find the type

    }

    public Largo(Slime slime, Minerancher plugin, SlimeType base1, SlimeType base2) {
        super(slime, plugin);
        // todo: rename entity and stuff
    }

    private Slime getBases(Slime slime) {
        /*
        why do these classes return slimes and why are they called here? because super() needs to be at the
        start of literally every statement so this is my hack around that. frick you java
        */
        String oldName = slime.getCustomName();
        if (oldName == null) {
            /* something has gone very wrong, just return a default largo?? */
            bases.add(SlimeType.PINK_SLIME);
            bases.add(SlimeType.HONEY_SLIME);
        } else {
            // tododododo
        }
        return slime;
    }

    @Override
    protected void initializeData() {
        size = 2;
        type = SlimeType.LARGO;

    }
}
