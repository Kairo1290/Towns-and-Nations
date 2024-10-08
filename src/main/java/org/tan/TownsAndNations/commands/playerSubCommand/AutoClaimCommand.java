package org.tan.TownsAndNations.commands.playerSubCommand;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.tan.TownsAndNations.Lang.Lang;
import org.tan.TownsAndNations.commands.SubCommand;
import org.tan.TownsAndNations.enums.ChunkType;
import org.tan.TownsAndNations.storage.PlayerAutoClaimStorage;

import java.util.ArrayList;
import java.util.List;

import static org.tan.TownsAndNations.utils.ChatUtils.getTANString;

public class AutoClaimCommand extends SubCommand {
    @Override
    public String getName() {
        return "autoclaim";
    }

    @Override
    public String getDescription() {
        return Lang.TOWN_AUTO_CLAIM_DESC.get();
    }
    public int getArguments(){ return 1;}


    @Override
    public String getSyntax() {
        return "/tan autoclaim <chunk type>";
    }
    @Override
    public List<String> getTabCompleteSuggestions(Player player, String lowerCase, String[] args){
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            for(ChunkType chunkType : ChunkType.values()){
                suggestions.add(chunkType.getName());
            }
            suggestions.add("stop");
        }
        return suggestions;
    }

    @Override
    public void perform(Player player, String[] args){

        if (args.length != 2) {
            player.sendMessage(getTANString() + Lang.CORRECT_SYNTAX_INFO.get(getSyntax()));
            return;
        }

        String message = args[1];

        switch (message) {
            case "town" -> {
                PlayerAutoClaimStorage.addPlayer(player, ChunkType.TOWN);
                player.sendMessage(getTANString() + Lang.AUTO_CLAIM_ON_FOR.get(ChunkType.TOWN.getName()));
            }
            case "region" -> {
                PlayerAutoClaimStorage.addPlayer(player, ChunkType.REGION);
                player.sendMessage(getTANString() + Lang.AUTO_CLAIM_ON_FOR.get(ChunkType.REGION.getName()));
            }
            case "stop" -> {
                PlayerAutoClaimStorage.removePlayer(player);
                player.sendMessage(getTANString() + Lang.AUTO_CLAIM_OFF.get());
            }
            default -> player.sendMessage(getTANString() + Lang.CORRECT_SYNTAX_INFO.get(getSyntax()));
        }
    }
}


