package com.songoda.epicbosses.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.entity.elements.*;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class BossEntity {

    @Expose @Getter private final List<EntityStatsElement> entityStats;
    @Expose @Getter private final MessagesElement messages;
    @Expose @Getter private final CommandsElement commands;
    @Expose @Getter private final SkillsElement skills;
    @Expose @Getter private final DropsElement drops;

    @Expose @Getter @Setter private boolean editing;
    @Expose @Getter @Setter private String spawnItem, targeting;

    public BossEntity(boolean editing, String spawnItem, List<EntityStatsElement> entityStats, SkillsElement skills, DropsElement drops, MessagesElement messages, CommandsElement commands) {
        this.editing = editing;
        this.spawnItem = spawnItem;
        this.entityStats = entityStats;
        this.skills = skills;
        this.drops = drops;
        this.messages = messages;
        this.commands = commands;
    }
}
