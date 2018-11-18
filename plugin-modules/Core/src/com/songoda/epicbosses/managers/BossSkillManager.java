package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.custom.*;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.skills.types.GroupSkillElement;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.BossesGson;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ILoadable;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class BossSkillManager implements ILoadable {

    private static final Set<CustomSkillHandler> SKILLS = new HashSet<>();

    @Override
    public void load() {
        registerCustomSkill(new Cage());
        registerCustomSkill(new Disarm());
        registerCustomSkill(new Fireball());
        registerCustomSkill(new Grapple());
        registerCustomSkill(new Insidious());
        registerCustomSkill(new Knockback());
        registerCustomSkill(new Launch());
        registerCustomSkill(new Lightning());
        registerCustomSkill(new Minions());
        registerCustomSkill(new Warp());
    }

    public void handleCustomSkillCasting(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        String type = customSkillElement.getCustom().getType();
        CustomSkillHandler customSkillHandler = getCustomSkillHandler(type);

        if(customSkillHandler == null) {
            Debug.FAILED_TO_OBTAIN_THE_SKILL_HANDLER.debug(type);
            return;
        }

        customSkillHandler.castSkill(skill, customSkillElement, activeBossHolder, nearbyEntities);
    }

    public PotionSkillElement getPotionSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("POTION")) {
            return BossesGson.get().fromJson(skill.getCustomData(), PotionSkillElement.class);
        }

        return null;
    }

    public CommandSkillElement getCommandSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("COMMAND")) {
            return BossesGson.get().fromJson(skill.getCustomData(), CommandSkillElement.class);
        }

        return null;
    }

    public GroupSkillElement getGroupSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("GROUP")) {
            return BossesGson.get().fromJson(skill.getCustomData(), GroupSkillElement.class);
        }

        return null;
    }

    public CustomSkillElement getCustomSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            return BossesGson.get().fromJson(skill.getCustomData(), CustomSkillElement.class);
        }

        return null;
    }

    public boolean registerCustomSkill(CustomSkillHandler customSkillHandler) {
        if(SKILLS.contains(customSkillHandler)) return false;

        SKILLS.add(customSkillHandler);
        return true;
    }

    public void removeCustomSkill(CustomSkillHandler customSkillHandler) {
        if(!SKILLS.contains(customSkillHandler)) return;

        SKILLS.remove(customSkillHandler);
    }

    public CustomSkillHandler getCustomSkillHandler(String name) {
        for(CustomSkillHandler customSkillHandler : new HashSet<>(SKILLS)) {
            String skillName = customSkillHandler.getClass().getSimpleName();

            if(skillName.equalsIgnoreCase(name)) return customSkillHandler;
        }

        return null;
    }
}
