package net.aminecraftdev.custombosses.panel;

import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.panel.base.PanelHandler;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 *
 * TODO
 */
public class CustomItemsPanel extends PanelHandler {

    public CustomItemsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel() {
        this.panel.setParentPanel(this.bossPanelManager.getMainMenu().getPanel());
    }
}
