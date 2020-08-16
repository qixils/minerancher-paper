package io.github.lexikiq.minerancher;

import io.github.lexikiq.minerancher.slimes.*;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum SlimeType {
	LARGO (Largo.class, "Largo"),
	TARR (Tarr.class, "Tarr"),
	PINK_SLIME (PinkSlime.class, "Pink Slime", 0x01),
	ROCK_SLIME (RockSlime.class, "Rock Slime", 0x02),
	HONEY_SLIME (HoneySlime.class, "Honey Slime", 0x08)
	;

	private final @Getter Class<? extends BaseSlime> slimeClass;
	private final @Getter String name;
	private final int colorID; // i swear i have IDs for a good reason, it's for proper textures for largos
	private static final Map<Class<? extends BaseSlime>, SlimeType> BY_CLASS = new HashMap<Class<? extends BaseSlime>, SlimeType>();
	private static final Map<Integer, SlimeType> BY_ID = new HashMap<Integer, SlimeType>();
	private static final Map<String, SlimeType> BY_NAME = new HashMap<String, SlimeType>();

	SlimeType(Class<? extends BaseSlime> slimeClass, String name) {
		this(slimeClass, name, 0x00);
	}

	SlimeType(Class<? extends BaseSlime> slimeClass, String name, int colorID) {
		this.slimeClass = slimeClass;
		this.name = name;
		this.colorID = colorID;
	}

	public String getLargoNameColor() { // returns a string
		if (colorID == 0x00) return null;
		String hexValue = String.format("%02x", colorID);
		StringBuilder output = new StringBuilder();
		for (char c : hexValue.toCharArray()) {
			output.append(ChatColor.COLOR_CHAR);
			output.append(c);
		}
		return output.toString();
	}

	/* static lookups */

	@NotNull
	public static SlimeType getByClass(Class<? extends BaseSlime> slimeClass) {
		return BY_CLASS.get(slimeClass);
	}

	@Nullable
	public static SlimeType getByID(int slimeID) {
		return BY_ID.get(slimeID);
	}

	@Nullable
	public static SlimeType getByLargoNameColor(String colors) {
		colors = colors.replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "");
		int getID = Integer.parseInt(colors, 16);
		return BY_ID.get(getID);
	}

	@Nullable
	public static SlimeType getByName(String entityName) {
		return BY_NAME.get(entityName);
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
			if (slime.colorID != 0x00) BY_ID.put(slime.colorID, slime);
		}
	}
}
