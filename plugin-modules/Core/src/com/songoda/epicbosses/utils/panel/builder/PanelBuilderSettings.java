package com.songoda.epicbosses.utils.panel.builder;

import com.songoda.epicbosses.utils.itemstack.ItemStackHolderConverter;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
public class PanelBuilderSettings {

    private boolean emptySpaceFiller, backButton, exitButton;
    private int fillTo, backButtonSlot, exitButtonSlot;
    private ItemStackHolder emptySpaceFillerItem;

    public PanelBuilderSettings(ConfigurationSection configurationSection) {
        ItemStackHolderConverter itemStackHolderConverter = new ItemStackHolderConverter();
        ConfigurationSection settingsSection = configurationSection.getConfigurationSection("Settings");
        ConfigurationSection buttonsSection = configurationSection.getConfigurationSection("Buttons");

        this.emptySpaceFiller = settingsSection != null && settingsSection.getBoolean("emptySpaceFiller", false);
        this.backButton = settingsSection != null && settingsSection.getBoolean("backButton", false);
        this.exitButton = settingsSection != null && settingsSection.getBoolean("exitButton", false);
        this.fillTo = settingsSection == null ? 0 : settingsSection.getInt("fillTo", 0);
        this.backButtonSlot = buttonsSection == null ? -1 : buttonsSection.getInt("backButton", -1);
        this.exitButtonSlot = buttonsSection == null ? -1 : buttonsSection.getInt("exitButton", -1);
        this.emptySpaceFillerItem = itemStackHolderConverter.to(configurationSection.getConfigurationSection("EmptySpaceFiller"));
    }

    public boolean isEmptySpaceFiller() {
        return this.emptySpaceFiller;
    }

    public boolean isBackButton() {
        return this.backButton;
    }

    public boolean isExitButton() {
        return this.exitButton;
    }

    public int getFillTo() {
        return this.fillTo;
    }

    public int getBackButtonSlot() {
        return this.backButtonSlot;
    }

    public int getExitButtonSlot() {
        return this.exitButtonSlot;
    }

    public ItemStackHolder getEmptySpaceFillerItem() {
        return this.emptySpaceFillerItem;
    }
}
