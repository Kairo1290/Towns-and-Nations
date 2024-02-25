package org.tan.TownsAndNations.API;

import org.tan.TownsAndNations.DataClass.TownData;
import org.tan.TownsAndNations.DataClass.newChunkData.ClaimedChunk2;
import org.tan.TownsAndNations.TownsAndNations;
import org.tan.TownsAndNations.storage.NewClaimedChunkStorage;
import org.tan.TownsAndNations.storage.TownDataStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class tanAPI {


    public static String getAPIVersion(){
        return "0.1.0";
    }

    public HashMap<String, TownData> getTownList(){
        return TownDataStorage.getTownList();
    }

    public Map<String, ClaimedChunk2> getChunkMap(){
        return NewClaimedChunkStorage.getClaimedChunksMap();
    }

    public Collection<ClaimedChunk2> getChunkList(){
        return NewClaimedChunkStorage.getClaimedChunksMap().values();
    }

    public int getChunkColor(String ID){
        if(ID.startsWith("T")){
            return TownDataStorage.get(ID).getChunkColor();
        }
        else if(ID.startsWith("R")){
            return 0x00FF00;
        }
        return 0x000000;
    }
    public int getChunkColor(ClaimedChunk2 chunk){
        return getChunkColor(chunk.getID());
    }

    public void setDynmapAddon(Boolean isLoaded){
        TownsAndNations.setDynmapAddonLoaded(isLoaded);
    }
}
