package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class MagmaCubeHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        int size = 4;
        if (entityType.contains(":")) {
            String[] split = entityType.split(":");
            size = Integer.parseInt(split[1]);
        }

        MagmaCube magmaCube = (MagmaCube) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.MAGMA_CUBE);
        magmaCube.setSize(size);

        return magmaCube;
    }
}
