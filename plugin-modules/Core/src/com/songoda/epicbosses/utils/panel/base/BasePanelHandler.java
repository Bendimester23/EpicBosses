package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public abstract class BasePanelHandler implements IBasicPanelHandler {

    protected final BossPanelManager bossPanelManager;

    private PanelBuilder panelBuilder;
    protected Panel panel = null;

    public BasePanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        this.bossPanelManager = bossPanelManager;
        this.panelBuilder = panelBuilder;

        initializePanel(panelBuilder);
    }

    @Override
    public Panel getPanel() {
        return this.panel;
    }

    @Override
    public PanelBuilder getPanelBuilder() {
        return this.panelBuilder;
    }
}
