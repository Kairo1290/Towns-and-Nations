package org.tan.TownsAndNations.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.tan.TownsAndNations.DataClass.PlayerData;
import org.tan.TownsAndNations.DataClass.TownData;
import org.tan.TownsAndNations.DataClass.TownRank;
import org.tan.TownsAndNations.GUI.GuiManager2;
import org.tan.TownsAndNations.Lang.Lang;
import org.tan.TownsAndNations.TownsAndNations;
import org.tan.TownsAndNations.storage.*;
import org.tan.TownsAndNations.utils.ChatUtils;
import org.tan.TownsAndNations.utils.ConfigUtil;
import org.tan.TownsAndNations.utils.TownUtil;

import java.time.LocalDate;
import java.util.*;

import static org.tan.TownsAndNations.utils.TownUtil.DonateToTown;


public class ChatListener implements Listener {

    @EventHandler
    public void OnPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        PlayerChatListenerStorage.PlayerChatData chatData = PlayerChatListenerStorage.getPlayerData(playerUUID);

        if(chatData == null)
            return;


        if(chatData.getCategory() == PlayerChatListenerStorage.ChatCategory.CREATE_CITY){

            int townPrice = Integer.parseInt(chatData.getData().get("town cost"));
            String townName = event.getMessage();

            TownUtil.CreateTown(player, townPrice, townName);

            event.setCancelled(true);
        }

        if(chatData.getCategory() == PlayerChatListenerStorage.ChatCategory.DONATION){

            String stringAmount = event.getMessage();

            int amount;
            try {amount = Integer.parseInt(stringAmount);}
            catch (NumberFormatException e) {
                player.sendMessage(ChatUtils.getTANString() + Lang.PAY_INVALID_SYNTAX.getTranslation());
                throw new RuntimeException(e);
            }

            DonateToTown(player, amount);

            event.setCancelled(true);
        }

        if(chatData.getCategory() == PlayerChatListenerStorage.ChatCategory.RANK_CREATION){
            PlayerChatListenerStorage.removePlayer(player);
            String rankName = event.getMessage();

            FileConfiguration config =  ConfigUtil.getCustomConfig("config.yml");
            int maxSize = config.getInt("RankNameSize");
            if(rankName.length() > maxSize){
                player.sendMessage(ChatUtils.getTANString() + Lang.MESSAGE_TOO_LONG.getTranslation(maxSize));
                event.setCancelled(true);
            }

            TownData playerTown = TownDataStorage.get(player);
            playerTown.createTownRank(rankName);
            Bukkit.getScheduler().runTask(TownsAndNations.getPlugin(), () -> GuiManager2.OpenTownMenuRoleManager(player, rankName));
            event.setCancelled(true);

        }

        if(chatData.getCategory() == PlayerChatListenerStorage.ChatCategory.RANK_RENAME){
            PlayerChatListenerStorage.PlayerChatData ChatData = PlayerChatListenerStorage.getPlayerData(playerUUID);

            String newRankName = event.getMessage();

            FileConfiguration config =  ConfigUtil.getCustomConfig("config.yml");
            int maxSize = config.getInt("RankNameSize");
            if(newRankName.length() > maxSize){
                player.sendMessage(ChatUtils.getTANString() + Lang.MESSAGE_TOO_LONG.getTranslation(maxSize));
                event.setCancelled(true);
            }

            TownData playerTown = TownDataStorage.get(player);
            String rankName = ChatData.getData().get("rankName");
            TownRank playerTownRank = playerTown.getRank(rankName);

            List<String> playerList = playerTownRank.getPlayers();
            for(String playerWithRoleUUID : playerList){
                Objects.requireNonNull(PlayerDataStorage.get(playerWithRoleUUID)).setRank(newRankName);
            }


            if(Objects.equals(playerTownRank.getName(), playerTown.getTownDefaultRank())){
                playerTown.setTownDefaultRank(newRankName);
            }

            playerTownRank.setName(newRankName);

            playerTown.addRank(newRankName,playerTownRank);
            playerTown.removeRank(rankName);

            Bukkit.getScheduler().runTask(TownsAndNations.getPlugin(), () -> GuiManager2.OpenTownMenuRoleManager(player, newRankName));

            PlayerChatListenerStorage.removePlayer(player);
            event.setCancelled(true);

        }

        if(chatData.getCategory() == PlayerChatListenerStorage.ChatCategory.CHANGE_DESCRIPTION){

            String newDesc = event.getMessage();

            FileConfiguration config =  ConfigUtil.getCustomConfig("config.yml");
            int maxSize = config.getInt("TownDescSize");
            if(newDesc.length() > maxSize){
                player.sendMessage(ChatUtils.getTANString() + Lang.MESSAGE_TOO_LONG.getTranslation(maxSize));
                event.setCancelled(true);
            }


            TownDataStorage.get(player).setDescription(newDesc);
            player.sendMessage(ChatUtils.getTANString() + Lang.GUI_TOWN_SETTINGS_CHANGE_TOWN_MESSAGE_IN_CHAT_SUCCESS.getTranslation());
            PlayerChatListenerStorage.removePlayer(player);
            event.setCancelled(true);

        }


    }
}