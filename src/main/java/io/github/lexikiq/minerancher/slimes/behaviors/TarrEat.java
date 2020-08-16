package io.github.lexikiq.minerancher.slimes.behaviors;

import io.github.lexikiq.minerancher.Minerancher;
import io.github.lexikiq.minerancher.SlimeType;
import io.github.lexikiq.minerancher.slimes.Tarr;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.UUID;

public class TarrEat extends BukkitRunnable {

    private final Minerancher plugin;
    private final UUID tarr;
    private final UUID slime;
    private final int slimeDistanceLimit;
    private final int ANIMATION_FRAME_COUNT = 4;
    private int frameNumber = 0;
    private Location slimeLoc;
    private Vector slimeVec;
    private Vector destLocation;
    private Vector eatDirection;

    public TarrEat(Minerancher plugin, UUID tarr, UUID slime, int slimeDistanceLimit) {
        this.plugin = plugin;
        this.tarr = tarr;
        this.slime = slime;
        this.slimeDistanceLimit = slimeDistanceLimit;
    }

    private double entityDistance(Entity mob1, Entity mob2) {
        return mob1.getLocation().distance(mob2.getLocation());
    }
    private void animate(Slime slime, Slime tarr) {
        frameNumber += 1;
        slime.teleport(slimeLoc.subtract(eatDirection));

        if (frameNumber >= ANIMATION_FRAME_COUNT) {
            plugin.registerSlime(slime, new Tarr(slime, plugin));
            tarr.setTarget(null);
            tarr.setAI(true);
            slime.setAI(true);
            slime.setInvulnerable(false);
            slime.getWorld().playSound(slime.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.HOSTILE, (float) 1.0, (float) 1.0);
            this.cancel();
        }
    }

    @Override
    public void run() {
        plugin.getServer().broadcastMessage("help");
        // this function manages the Tarr's hunting behavior
        final double EAT_ANIMATION_DISTANCE = 2;

        Slime tarrEntity = (Slime) plugin.getServer().getEntity(tarr);
        LivingEntity livingEntity = (LivingEntity) plugin.getServer().getEntity(slime);
        if (tarrEntity == null || livingEntity == null || tarrEntity.isDead() || livingEntity.isDead()) {
            // if one of the mobs doesn't exist anymore, clear the target and cancel
            if (tarrEntity != null) {
                tarrEntity.setTarget(null);
            }
            this.cancel();
            return;
        }
        if (livingEntity.getType() == EntityType.PLAYER) {
            // no special behavior/animations needed if attacking a player
            LivingEntity entityTarget;
            if (livingEntity.isInvulnerable() || Arrays.asList(GameMode.CREATIVE, GameMode.SPECTATOR).contains(((Player) livingEntity).getGameMode())) {
                this.cancel();
                entityTarget = null;
            } else {
                entityTarget = livingEntity;
            }
            tarrEntity.setTarget(entityTarget);
            return;
        }
        Slime slimeEntity = (Slime) livingEntity;

        if (frameNumber > 0) { // continue animating
            animate(slimeEntity, tarrEntity);
            return;
        }
        Entity targetEntity = tarrEntity.getTargetEntity(25);
        if (targetEntity != slimeEntity && targetEntity != null) {
            // Tarr is no longer aggro'd at this Slime
            this.cancel();
            return;
        }
        if (SlimeType.getByName(slimeEntity) == SlimeType.TARR || !slimeEntity.hasAI() || entityDistance(tarrEntity, slimeEntity) > slimeDistanceLimit) {
            // cancel if slime is now a Tarr, was eaten by another Tarr first, or is too far away
            tarrEntity.setTarget(null);
            this.cancel();
            return;
        }
        if (entityDistance(tarrEntity, slimeEntity) <= EAT_ANIMATION_DISTANCE) {
            tarrEntity.setAI(false);
            slimeEntity.setAI(false);
            slimeEntity.setInvulnerable(true);
            slimeLoc = slimeEntity.getLocation();
            slimeVec = slimeLoc.toVector();
            destLocation = tarrEntity.getEyeLocation().toVector().midpoint(tarrEntity.getLocation().toVector());
            eatDirection = slimeVec.subtract(destLocation).multiply(1.0 / ANIMATION_FRAME_COUNT);
            animate(slimeEntity, tarrEntity);
            return;
        }
        tarrEntity.setTarget(slimeEntity);
    }
}