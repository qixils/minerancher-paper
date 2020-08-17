package io.github.lexikiq.minerancher.slimes;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Largo extends BaseSlime {
    private String oldName;
    private final ArrayList<SlimeType> bases = new ArrayList<>();
    private final ArrayList<Character> baseChars = new ArrayList<>();

    public Largo(Slime slime, Minerancher plugin) {
        super(slime, plugin);
        SlimeType base1 = SlimeType.getByCodepoint(oldName.charAt(0));
        SlimeType base2 = SlimeType.getByCodepoint(oldName.charAt(oldName.length()-1));
        if (base1 == null || base2 == null)
            throw new IllegalStateException(String.format("Couldn't process Largo bases: %s", oldName)); // im good programmer
        saveBases(base1, base2);
    }

    public Largo(Slime slime, Minerancher plugin, SlimeType base1, SlimeType base2) {
        super(slime, plugin);
        saveBases(base1, base2);
    }

    private void saveBases(SlimeType base1, SlimeType base2) {
        bases.addAll(Arrays.asList(base1, base2));
        baseChars.addAll(bases.stream().map(SlimeType::getCodepoint).sorted().collect(Collectors.toList()));
        slime.setCustomName(getDisplayName());
    }

    @Override
    protected void initializeData() {
        size = 2;
        type = SlimeType.LARGO;
        oldName = slime.getCustomName();  // store for later initialization
    }

    @Override
    public String getDisplayName() {
        if (baseChars != null) {
            return baseChars.get(0) + type.getName() + baseChars.get(1);
        } else {
            // return placeholder value if baseChars has not yet been defined
            return type.getName();
        }
    }
}
