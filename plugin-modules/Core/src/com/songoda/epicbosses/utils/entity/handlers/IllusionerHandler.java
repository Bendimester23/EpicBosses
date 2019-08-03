package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * - * @author Charles Cullen
 * - * @version 1.0.0
 * - * @since 01-Jul-18
 * -
 */
public class IllusionerHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (this.versionHandler.getVersion().isLessThan(Versions.v1_11_R1)) {
            throw new NullPointerException("This feature is only implemented in version 1.11 and above of Minecraft.");
        }

        return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ILLUSIONER);
    }
}