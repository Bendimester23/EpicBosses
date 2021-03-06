package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.EntityFinder;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic implements IBossMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        for (EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();

            String bossEntityType = mainStatsElement.getEntityType();
            String input = bossEntityType.split(":")[0];
            EntityFinder entityFinder = EntityFinder.get(input);
            Integer position = mainStatsElement.getPosition();

            if (position == null) position = 1;
            if (entityFinder == null) return false;

            LivingEntity livingEntity = entityFinder.spawnNewLivingEntity(bossEntityType, activeBossHolder.getLocation());

            if (livingEntity == null) return false;

            activeBossHolder.setLivingEntity(position, livingEntity);

            if (position > 1) {
                int lowerPosition = position - 1;
                LivingEntity lowerLivingEntity = activeBossHolder.getLivingEntity(lowerPosition);

                if (lowerLivingEntity == null) {
                    Debug.FAILED_ATTEMPT_TO_STACK_BOSSES.debug(BossAPI.getBossEntityName(bossEntity));
                    return false;
                }

                lowerLivingEntity.setPassenger(livingEntity);
            }
        }

        return true;
    }
}
