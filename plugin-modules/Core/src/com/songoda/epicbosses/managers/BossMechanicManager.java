package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
import com.songoda.epicbosses.utils.mechanics.IPrimaryMechanic;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class BossMechanicManager implements ILoadable {

    private final CustomBosses customBosses;
    private Queue<IOptionalMechanic> optionalMechanics;
    private Queue<IPrimaryMechanic> primaryMechanics;

    public BossMechanicManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        this.primaryMechanics = new LinkedList<>();
        this.optionalMechanics = new LinkedList<>();

        registerMechanic(new EntityTypeMechanic());
        registerMechanic(new NameMechanic());
        registerMechanic(new HealthMechanic());
        registerMechanic(new EquipmentMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new WeaponMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new PotionMechanic());
        registerMechanic(new SettingsMechanic());
    }

    public void registerMechanic(IMechanic mechanic) {
        if(mechanic instanceof IPrimaryMechanic) {
            this.primaryMechanics.add((IPrimaryMechanic) mechanic);
        } else if(mechanic instanceof IOptionalMechanic) {
            this.optionalMechanics.add((IOptionalMechanic) mechanic);
        } else {
            Debug.MECHANIC_TYPE_NOT_STORED.debug();
        }
    }

    public boolean handleMechanicApplication(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(bossEntity != null && activeBossHolder != null) {
//            if(bossEntity.isEditing()) {
//                Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
//                return false;
//            }

            Queue<IMechanic> queue = new LinkedList<>(this.primaryMechanics);

            while(!queue.isEmpty()) {
                IMechanic mechanic = queue.poll();

                if(mechanic == null) continue;

                ServerUtils.get().logDebug("Applying " + mechanic.getClass().getSimpleName());

                if(didMechanicApplicationFail(mechanic, bossEntity, activeBossHolder)) return false;
            }

            queue = new LinkedList<>(this.optionalMechanics);

            while(!queue.isEmpty()) {
                IMechanic mechanic = queue.poll();

                if(mechanic == null) continue;
                if(didMechanicApplicationFail(mechanic, bossEntity, activeBossHolder)) continue;
            }
        }

        return true;
    }

    private boolean didMechanicApplicationFail(IMechanic mechanic, BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(mechanic == null) return true;

        if(!mechanic.applyMechanic(bossEntity, activeBossHolder)) {
            Debug.MECHANIC_APPLICATION_FAILED.debug(mechanic.getClass().getSimpleName());
            return true;
        }

        return false;
    }
}