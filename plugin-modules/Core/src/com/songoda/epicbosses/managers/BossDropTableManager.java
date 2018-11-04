package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.handlers.IGetDropTableListItem;
import com.songoda.epicbosses.handlers.droptable.GetDropTableCommand;
import com.songoda.epicbosses.handlers.droptable.GetDropTableItemStack;
import com.songoda.epicbosses.holder.DeadBossHolder;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class BossDropTableManager {

    private final IGetDropTableListItem<ItemStack> getDropTableItemStack;
    private final IGetDropTableListItem<List<String>> getDropTableCommand;

    public BossDropTableManager(CustomBosses plugin) {
        this.getDropTableItemStack = new GetDropTableItemStack(plugin);
        this.getDropTableCommand = new GetDropTableCommand();
    }

    public List<ItemStack> getSprayItems(SprayTableElement sprayTableElement) {
        Map<String, Double> rewards = sprayTableElement.getSprayRewards();
        Integer maxDrops = sprayTableElement.getSprayMaxDrops();
        Boolean randomDrops = sprayTableElement.getRandomSprayDrops();

        if(maxDrops == null) maxDrops = -1;
        if(randomDrops == null) randomDrops = false;

        return getCustomRewards(randomDrops, maxDrops, rewards);
    }

    public List<ItemStack> getDropItems(DropTableElement dropTableElement) {
        Map<String, Double> rewards = dropTableElement.getDropRewards();
        Integer maxDrops = dropTableElement.getDropMaxDrops();
        Boolean randomDrops = dropTableElement.getRandomDrops();

        if(maxDrops == null) maxDrops = -1;
        if(randomDrops == null) randomDrops = false;

        return getCustomRewards(randomDrops, maxDrops, rewards);
    }

    public void handleGiveTable(GiveTableElement giveTableElement, DeadBossHolder deadBossHolder) {
        Map<String, Map<String, GiveTableSubElement>> rewards = giveTableElement.getGiveRewards();
        Map<UUID, Double> mapOfDamage = deadBossHolder.getSortedDamageMap();
        Map<UUID, Double> percentMap = deadBossHolder.getPercentageMap();
        List<UUID> positions = new ArrayList<>(mapOfDamage.keySet());

        rewards.forEach((positionString, lootMap) -> {
            if(!NumberUtils.get().isInt(positionString)) {
                Debug.DROP_TABLE_FAILED_INVALID_NUMBER.debug(positionString);
                return;
            }

            int position = NumberUtils.get().getInteger(positionString);

            if(positions.size() < position) return;

            UUID uuid = positions.get(position);
            Player player = Bukkit.getPlayer(uuid);
            double percentage = percentMap.getOrDefault(uuid, -1.0);
            List<ItemStack> totalRewards = new ArrayList<>();
            List<String> totalCommands = new ArrayList<>();

            if(player == null) return;

            lootMap.forEach((key, subElement) -> {
                Double requiredPercentage = subElement.getRequiredPercentage();
                Integer maxDrops = subElement.getMaxDrops(), maxCommands = subElement.getMaxCommands();
                Boolean randomDrops = subElement.getRandomDrops(), randomCommands = subElement.getRandomCommands();

                if(requiredPercentage == null) requiredPercentage = 0.0D;
                if(maxDrops == null) maxDrops = -1;
                if(maxCommands == null) maxCommands = -1;
                if(randomDrops == null) randomDrops = false;
                if(randomCommands == null) randomCommands = false;

                if(requiredPercentage > percentage) return;

                totalRewards.addAll(getCustomRewards(randomDrops, maxDrops, subElement.getItems()));
                totalCommands.addAll(getCommands(randomCommands, maxCommands, subElement.getCommands()));
            });

            totalCommands.replaceAll(s -> s.replace("%player%", player.getName()));

            totalCommands.forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
            totalRewards.forEach(itemStack -> {
                if(player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                } else {
                    player.getInventory().addItem(itemStack);
                }
            });
        });
    }


    private List<ItemStack> getCustomRewards(boolean random, int max, Map<String, Double> chanceMap) {
        List<ItemStack> newListToMerge = new ArrayList<>();

        if(chanceMap == null) return newListToMerge;

        List<String> keyList = new ArrayList<>(chanceMap.keySet());

        if(random) Collections.shuffle(keyList);

        for(String itemName : keyList) {
            Double chance = chanceMap.get(itemName);
            double randomNumber = RandomUtils.get().getRandomDecimalNumber();

            if(randomNumber > chance) continue;
            if((max > 0) && (newListToMerge.size() >= max)) break;

            ItemStack itemStack = this.getDropTableItemStack.getListItem(itemName);

            if(itemStack == null) {
                Debug.DROP_TABLE_FAILED_TO_GET_ITEM.debug();
                continue;
            }

            newListToMerge.add(itemStack);
        }

        return newListToMerge;
    }

    private List<String> getCommands(boolean random, int max, Map<String, Double> chanceMap) {
        List<String> newListToMerge = new ArrayList<>();

        if(chanceMap == null) return newListToMerge;

        List<String> keyList = new ArrayList<>(chanceMap.keySet());

        if(random) Collections.shuffle(keyList);

        for(String itemName : keyList) {
            Double chance = chanceMap.get(itemName);
            double randomNumber = RandomUtils.get().getRandomDecimalNumber();

            if(randomNumber > chance) continue;
            if((max > 0) && (newListToMerge.size() >= max)) break;

            List<String> commands = this.getDropTableCommand.getListItem(itemName);

            if(commands == null) {
                Debug.DROP_TABLE_FAILED_TO_GET_ITEM.debug();
                continue;
            }

            newListToMerge.addAll(commands);
        }

        return newListToMerge;
    }

}