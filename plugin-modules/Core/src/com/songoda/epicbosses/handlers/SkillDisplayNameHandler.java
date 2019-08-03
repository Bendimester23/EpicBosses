package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class SkillDisplayNameHandler implements IHandler {

    @Getter private final IVariablePanelHandler<Skill> panelHandler;
    @Getter private final SkillsFileManager skillsFileManager;
    @Getter private final Player player;
    @Getter private final Skill skill;

    @Getter @Setter private boolean handled = false;
    private Listener listener;

    public SkillDisplayNameHandler(Player player, Skill skill, SkillsFileManager skillsFileManager, IVariablePanelHandler<Skill> panelHandler) {
        this.skill = skill;
        this.player = player;
        this.skillsFileManager = skillsFileManager;
        this.panelHandler = panelHandler;

        this.listener = getListener();
    }

    @Override
    public void handle() {
        ServerUtils.get().registerListener(this.listener);
    }

    private Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();

                if(!uuid.equals(getPlayer().getUniqueId())) return;
                if(isHandled()) return;

                String input = event.getMessage();

                if(input.equalsIgnoreCase("-")) {
                    input = null;
                }

                getSkill().setDisplayName(input);
                getSkillsFileManager().save();
                event.setCancelled(true);
                setHandled(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(CustomBosses.get(), SkillDisplayNameHandler.this::finish);
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getSkill());
    }
}