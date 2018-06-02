package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.ReflectionUtil;
import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class ElderGuardianHandler implements ICustomEntityHandler {

    private String version = ReflectionUtil.get().getVersion();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.version.startsWith("v1_11_") || this.version.startsWith("v1_12_")) {
            return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
        } else {
            Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
            skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);

            return skeleton;
        }
    }
}
