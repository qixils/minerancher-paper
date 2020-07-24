package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimes.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		if (entity == null || entity.getType() != EntityType.SLIME || entity.getCustomName() == null)
			return false;
		List<String> entityName = Arrays.asList(entity.getCustomName().split(" "));
		Collections.reverse(entityName);
		return entityName.get(0).equals(toString());
	}
}
