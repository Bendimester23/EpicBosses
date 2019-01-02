package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class NameMechanic implements IBossMechanic {

    private CustomBosses plugin = CustomBosses.get();

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            String customName = mainStatsElement.getDisplayName();

            if(livingEntity == null) return false;

            if(customName != null) {
                String formattedName = StringUtils.get().translateColor(customName);

                if(CustomBosses.get().getConfig().getBoolean("Hooks.HolographicDisplays.enabled", false) && this.plugin.getHolographicDisplayHelper().isConnected()) {
                    this.plugin.getHolographicDisplayHelper().createHologram(livingEntity, formattedName);
                    livingEntity.setCustomNameVisible(false);
                } else {
                    livingEntity.setCustomName(formattedName);
                    livingEntity.setCustomNameVisible(true);
                }
            }
        }

        return true;
    }
}
