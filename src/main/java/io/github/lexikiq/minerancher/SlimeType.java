package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.*;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public enum SlimeType {
	LARGO (Largo.class, "Largo"),
	TARR (Tarr.class, "Tarr"),
	PINK_SLIME (PinkSlime.class, "Pink Slime", '\uF801'),
	ROCK_SLIME (RockSlime.class, "Rock Slime", '\uF802'),
	HONEY_SLIME (HoneySlime.class, "Honey Slime", '\uF808')
	;

	private final @Getter Class<? extends BaseSlime> slimeClass;
	private final @Getter String name;
	private final char codepoint; // for identifying largo types
	private static final Map<Class<? extends BaseSlime>, SlimeType> BY_CLASS = new HashMap<Class<? extends BaseSlime>, SlimeType>();
	private static final Map<Character, SlimeType> BY_CODEPOINT = new HashMap<Character, SlimeType>();
	private static final Map<String, SlimeType> BY_NAME = new HashMap<String, SlimeType>();
	private static final Pattern codepointRange = Pattern.compile("[\uF801-\uF81f]");
	private static final char nullCodepoint = '\uF800';

	SlimeType(Class<? extends BaseSlime> slimeClass, String name) {
		this(slimeClass, name, nullCodepoint);
	}

	SlimeType(Class<? extends BaseSlime> slimeClass, String name, char codepoint) {
		this.slimeClass = slimeClass;
		this.name = name;
		this.codepoint = codepoint;
	}

	public char getCodepoint() { // returns a string
		if (codepoint == nullCodepoint) throw new IllegalStateException(String.format("%s has no color ID value", name));
		return codepoint;
	}

	/* static lookups */

	@NotNull
	public static SlimeType getByClass(Class<? extends BaseSlime> slimeClass) {
		return BY_CLASS.get(slimeClass);
	}

	@Nullable
	public static SlimeType getByCodepoint(char uniCharacter) {
		return BY_CODEPOINT.get(uniCharacter);
	}

	@Nullable
	public static SlimeType getByName(String entityName) {
		// regex replace is a hack to return Largos properly
		return BY_NAME.get(codepointRange.matcher(entityName).replaceAll(""));
	}

	@Nullable
	public static SlimeType getByName(@Nullable Entity entity) {
		if (entity == null) return null;
		if (entity.getType() != EntityType.SLIME) return null;
		return getByName(entity.getCustomName());
	}

	static {
		for (SlimeType slime : values()) {
			BY_CLASS.put(slime.slimeClass, slime);
			BY_NAME.put(slime.name, slime);
			if (slime.codepoint != nullCodepoint) BY_CODEPOINT.put(slime.codepoint, slime);
		}
	}
}
