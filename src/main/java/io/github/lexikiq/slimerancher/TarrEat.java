package io.github.lexikiq.slimerancher;

import io.github.lexikiq.slimerancher.slimetypes.Tarr;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class TarrEat extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final UUID tarr;
    private final UUID slime;
    private final int slimeDistanceLimit;
    private final int ANIMATION_FRAME_COUNT = 4;
    private int frameNumber = 0;
    private Location slimeLoc;
    private Vector slimeVec;
    private Vector destLocation;
    private Vector eatDirection;

    public TarrEat(JavaPlugin plugin, UUID tarr, UUID slime, int slimeDistanceLimit) {
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
            new Tarr().convertSlimeEntity(slime);
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
        // this function manages the Tarr's hunting behavior
        final double EAT_ANIMATION_DISTANCE = 2;

        Slime tarrEntity = (Slime) plugin.getServer().getEntity(tarr);
        Slime slimeEntity = (Slime) plugin.getServer().getEntity(slime);
        if (tarrEntity == null || slimeEntity == null) { // if one of the mobs doesn't exist anymore,
            if (tarrEntity != null) { // clear target
                tarrEntity.setTarget(null);
            }
            // and cancel
            this.cancel();
            return;
        }
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
        if (new Tarr().isSlimeEntity(slimeEntity) || !slimeEntity.hasAI() || entityDistance(tarrEntity, slimeEntity) > slimeDistanceLimit) {
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
        }
    }
}