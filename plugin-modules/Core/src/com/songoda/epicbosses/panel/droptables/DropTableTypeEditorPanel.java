package com.songoda.epicbosses.panel.droptables;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 23-Dec-18
 */
public class DropTableTypeEditorPanel extends VariablePanelHandler<DropTable> {

    private EpicBosses plugin;

    public DropTableTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainDropTableEditMenu(), dropTable);

        ServerUtils.get().runTaskAsync(() -> {
            panelBuilderCounter.getSlotsWith("Spray").forEach(slot -> panel.setOnClick(slot, getSprayAction(dropTable)));
            panelBuilderCounter.getSlotsWith("Drop").forEach(slot -> panel.setOnClick(slot, getDropAction(dropTable)));
            panelBuilderCounter.getSlotsWith("Give").forEach(slot -> panel.setOnClick(slot, getGiveAction(dropTable)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getSprayAction(DropTable dropTable) {
        return event -> {
            dropTable.setDropType("SPRAY");
            dropTable.setRewards(BossAPI.createNewDropTableRewards(dropTable.getDropType()));
            save(dropTable, event);
        };
    }

    private ClickAction getDropAction(DropTable dropTable) {
        return event -> {
            dropTable.setDropType("DROP");
            dropTable.setRewards(BossAPI.createNewDropTableRewards(dropTable.getDropType()));
            save(dropTable, event);
        };
    }

    private ClickAction getGiveAction(DropTable dropTable) {
        return event -> {
            dropTable.setDropType("GIVE");
            dropTable.setRewards(BossAPI.createNewDropTableRewards(dropTable.getDropType()));
            save(dropTable, event);
        };
    }

    private void save(DropTable dropTable, InventoryClickEvent event) {
        this.plugin.getDropTableFileManager().save();
        this.bossPanelManager.getMainDropTableEditMenu().openFor((Player) event.getWhoClicked(), dropTable);
    }
}
