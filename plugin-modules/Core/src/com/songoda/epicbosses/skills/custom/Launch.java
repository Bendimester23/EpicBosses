package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.types.CustomSkill;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Launch extends CustomSkill implements ISkillHandler {

    public Launch(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        Location bossLocation = activeBossHolder.getLocation();

        nearbyEntities.forEach(livingEntity -> livingEntity.teleport(bossLocation.clone().add(0, 35, 0)));
    }
}
