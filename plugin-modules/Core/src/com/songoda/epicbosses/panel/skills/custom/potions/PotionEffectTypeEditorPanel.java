package com.songoda.epicbosses.panel.skills.custom.potions;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.PotionEffectFinder;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.potion.PotionEffectConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class PotionEffectTypeEditorPanel extends SubVariablePanelHandler<Skill, PotionEffectHolder> {

    private PotionEffectConverter potionEffectConverter = new PotionEffectConverter();
    private EpicBosses plugin;

    public PotionEffectTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, PotionEffectHolder potionEffectHolder) {
        List<PotionEffectType> list = Arrays.stream(PotionEffectType.values()).filter(potionEffectType -> potionEffectType != null).collect(Collectors.toList());
        int maxPage = panel.getMaxPage(list);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, list, skill, potionEffectHolder);
            return true;
        }));

        loadPage(panel, 0, list, skill, potionEffectHolder);
    }

    @Override
    public void openFor(Player player, Skill skill, PotionEffectHolder potionEffectHolder) {
        Panel panel = getPanelBuilder().getPanel()
                .setParentPanelHandler(this.bossPanelManager.getCreatePotionEffectMenu(), skill, potionEffectHolder);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, skill, potionEffectHolder));
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int requestedPage, List<PotionEffectType> potionEffectTypes, Skill skill, PotionEffectHolder potionEffectHolder) {
        String type = potionEffectHolder.getType();

        ServerUtils.get().runTaskAsync(() -> panel.loadPage(requestedPage, ((slot, realisticSlot) -> {
            if (slot >= potionEffectTypes.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                PotionEffectType potionEffectType = potionEffectTypes.get(slot);
                ItemStack itemStack = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
                    PotionType potionType = PotionType.getByEffect(potionEffectType);

                    if (potionType == null) potionType = PotionType.WATER;

                    potionMeta.setBasePotionData(new PotionData(potionType));
                } else {
                    potionMeta.addCustomEffect(this.potionEffectConverter.from(potionEffectHolder), true);
                }

                itemStack.setItemMeta(potionMeta);

                Map<String, String> replaceMap = new HashMap<>();
                boolean found = false;

                replaceMap.put("{effect}", StringUtils.get().formatString(potionEffectType.getName()));

                if (type != null && !type.isEmpty()) {
                    PotionEffectFinder potionEffectFinder = PotionEffectFinder.getByName(type);

                    if (potionEffectFinder != null) {
                        ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Skills.CreatePotion.selectedName"), replaceMap);
                        found = true;
                    }
                }

                if (!found) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Skills.CreatePotion.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, e -> {
                    PotionEffectFinder potionEffectFinder = PotionEffectFinder.getByEffect(potionEffectType);

                    if (potionEffectFinder != null) {
                        potionEffectHolder.setType(potionEffectFinder.getFancyName());

                        this.bossPanelManager.getCreatePotionEffectMenu().openFor((Player) e.getWhoClicked(), skill, potionEffectHolder);
                    }
                });
            }
        })));
    }
}
