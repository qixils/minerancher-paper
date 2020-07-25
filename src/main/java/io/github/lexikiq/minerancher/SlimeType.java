package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public enum SlimeType {
	SLIME (BaseSlime.class),
	LARGO (BaseLargo.class),
	TARR (Tarr.class),
	PINK_SLIME (PinkSlime.class),
	HONEY_SLIME (HoneySlime.class)
	;

	private final Class<? extends BaseSlime> slimeClass;
	SlimeType(Class<? extends BaseSlime> slimeClass) {
		this.slimeClass = slimeClass;
	}

	public Class<? extends BaseSlime> getSlimeClass() {
		return slimeClass;
	}

	@Override
	public String toString() {
		return WordUtils.capitalizeFully(super.toString().replace('_', ' '));
	}

	public boolean isType(Entity entity) {
		// returns if the input entity is the same type as the enum
		// todo: largos
		if (entity == null || entity.getType() != EntityType.SLIME || entity.getCustomName() == null)
			return false;
		return entity.getCustomName().equals(toString());
	}
}
