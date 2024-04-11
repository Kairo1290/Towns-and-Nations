package org.tan.TownsAndNations.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.tan.TownsAndNations.DataClass.newChunkData.ClaimedChunk2;
import org.tan.TownsAndNations.storage.DataStorage.NewClaimedChunkStorage;

public class MobSpawnListener implements Listener {

    @EventHandler
    public void EntitySpawn(EntitySpawnEvent e){
        Chunk currentChunk = e.getEntity().getLocation().getChunk();
        if(!NewClaimedChunkStorage.isChunkClaimed(currentChunk)){
            return;
        }
        ClaimedChunk2 claimedChunk2 = NewClaimedChunkStorage.get(currentChunk);
        EntityType entityType = e.getEntity().getType();

        if(!claimedChunk2.canEntitySpawn(entityType)){
            e.setCancelled(true);
        }
    }
}
