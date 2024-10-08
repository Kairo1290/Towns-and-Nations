package org.tan.TownsAndNations.commands.playerSubCommand;

import org.bukkit.entity.Player;
import org.tan.TownsAndNations.DataClass.PlayerData;
import org.tan.TownsAndNations.DataClass.territoryData.TownData;
import org.tan.TownsAndNations.Lang.Lang;
import org.tan.TownsAndNations.commands.SubCommand;
import org.tan.TownsAndNations.enums.TownRolePermission;
import org.tan.TownsAndNations.storage.DataStorage.PlayerDataStorage;
import org.tan.TownsAndNations.storage.DataStorage.TownDataStorage;

import java.util.List;

import static org.tan.TownsAndNations.utils.ChatUtils.getTANString;

public class SetTownSpawnCommand extends SubCommand {
    @Override
    public String getName() {
        return "setspawn";
    }

    @Override
    public String getDescription() {
        return Lang.SET_SPAWN_COMMAND_DESC.get();
    }
    public int getArguments(){ return 1;}


    @Override
    public String getSyntax() {
        return "/tan setspawn";
    }
    @Override
    public List<String> getTabCompleteSuggestions(Player player, String lowerCase, String[] args){
        return null;
    }
    @Override
    public void perform(Player player, String[] args){

        //Incorrect syntax
        if (args.length != 1){
            player.sendMessage(getTANString() + Lang.CORRECT_SYNTAX_INFO.get(getSyntax()) );
            return;
        }

        //No town
        PlayerData playerStat = PlayerDataStorage.get(player.getUniqueId().toString());
        if(!playerStat.haveTown()){
            player.sendMessage(getTANString() + Lang.PLAYER_NO_TOWN.get());
            return;
        }

        //No permission

        if(!playerStat.hasPermission(TownRolePermission.TOWN_ADMINISTRATOR)){
            player.sendMessage(getTANString() + Lang.PLAYER_NO_PERMISSION.get());
            return;
        }

        TownData townData = TownDataStorage.get(player);

        //Spawn Unlocked
        if(townData.isSpawnLocked()){
            player.sendMessage(getTANString() + Lang.SPAWN_NOT_UNLOCKED.get());
            return;
        }

        townData.setSpawn(player.getLocation());
        player.sendMessage(getTANString() + Lang.SPAWN_SET_SUCCESS.get());
    }

}


