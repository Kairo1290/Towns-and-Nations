package org.tan.TownsAndNations.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.tan.TownsAndNations.DataClass.RegionData;
import org.tan.TownsAndNations.DataClass.TownData;
import org.tan.TownsAndNations.Lang.Lang;
import org.tan.TownsAndNations.storage.*;
import org.tan.TownsAndNations.storage.DataStorage.RegionDataStorage;
import org.tan.TownsAndNations.storage.DataStorage.TownDataStorage;


import static org.tan.TownsAndNations.enums.ChatCategory.CREATE_REGION;
import static org.tan.TownsAndNations.enums.SoundEnum.MINOR_LEVEL_UP;
import static org.tan.TownsAndNations.utils.ChatUtils.getTANString;
import static org.tan.TownsAndNations.utils.EconomyUtil.getBalance;
import static org.tan.TownsAndNations.utils.EconomyUtil.removeFromBalance;

public class RegionUtil {


    public static void createNewRegion(Player player, String regionName){


        TownData town = TownDataStorage.get(player);


        if(!town.isLeader(player)){
            player.sendMessage(getTANString() + Lang.PLAYER_ONLY_LEADER_CAN_PERFORM_ACTION.get());
            return;
        }

        int cost = ConfigUtil.getCustomConfig("config.yml").getInt("regionCost");

        if(town.getBalance() < cost){
            player.sendMessage(getTANString() + Lang.TOWN_NOT_ENOUGH_MONEY.get());
            return;
        }

        int maxSize = ConfigUtil.getCustomConfig("config.yml").getInt("RegionNameSize");
        if(regionName.length() > maxSize){
            player.sendMessage(ChatUtils.getTANString() + Lang.MESSAGE_TOO_LONG.get(maxSize));
            return;
        }

        Bukkit.broadcastMessage(ChatUtils.getTANString() + Lang.REGION_CREATE_SUCCESS_BROADCAST.get(town.getName(),regionName));
        PlayerChatListenerStorage.removePlayer(player);
        RegionData newRegion = RegionDataStorage.newRegion(regionName,player);
        town.setRegion(newRegion);
        town.removeToBalance(cost);
    }


    public static void registerNewRegion(Player player, int regionCost) {

        int townMoney = TownDataStorage.get(player).getBalance();
        if (townMoney < regionCost) {
            player.sendMessage(getTANString() + Lang.TOWN_NOT_ENOUGH_MONEY_EXTENDED.get(regionCost - townMoney));
        }
        else {
            player.sendMessage(getTANString() + Lang.WRITE_IN_CHAT_NEW_REGION_NAME.get());
            player.closeInventory();

            PlayerChatListenerStorage.addPlayer(CREATE_REGION,player);
        }
    }

    public static void donateToRegion(Player player, int amountDonated) {

        int playerBalance = getBalance(player);
        PlayerChatListenerStorage.removePlayer(player);

        if(playerBalance < amountDonated ){
            player.sendMessage(ChatUtils.getTANString() + Lang.PLAYER_NOT_ENOUGH_MONEY.get());
            return;
        }
        if(amountDonated <= 0 ){
            player.sendMessage(ChatUtils.getTANString() + Lang.PLAYER_NEED_1_OR_ABOVE.get());
            return;
        }

        RegionData playerRegion = RegionDataStorage.get(player);

        removeFromBalance(player, amountDonated);
        playerRegion.addBalance(amountDonated);

        playerRegion.getDonationHistory().add(player.getName(),player.getUniqueId().toString(),amountDonated);

        player.sendMessage(ChatUtils.getTANString() + Lang.PLAYER_SEND_MONEY_TO_REGION.get(amountDonated));
        SoundUtil.playSound(player, MINOR_LEVEL_UP);
    }
}