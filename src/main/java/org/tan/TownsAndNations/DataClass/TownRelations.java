package org.tan.TownsAndNations.DataClass;

import org.tan.TownsAndNations.Lang.Lang;
import org.tan.TownsAndNations.enums.TownRelation;
import org.tan.TownsAndNations.storage.TownDataStorage;
import org.tan.TownsAndNations.utils.ChatUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TownRelations {

    private final LinkedHashMap<TownRelation, ArrayList<String>> townRelations = new LinkedHashMap<>();


    public TownRelations(){
        for(TownRelation relation : TownRelation.values()){
            this.townRelations.put(relation, new ArrayList<>());
        }
    }

    public void addRelation(TownRelation relation, String townID){
        this.townRelations.get(relation).add(townID);
    }
    public void removeRelation(TownRelation relation, String townID){
        townRelations.get(relation).remove(townID);
    }
    public ArrayList<String> getOne(TownRelation relation){
        return this.townRelations.get(relation);
    }
    public TownRelation getRelationWith(TownData Town) {
        return getRelationWith(Town.getID());
    }
    public TownRelation getRelationWith(String TownID) {
        for (Map.Entry<TownRelation, ArrayList<String>> entry : townRelations.entrySet()) {
            TownRelation relation = entry.getKey();
            ArrayList<String> list = entry.getValue();

            for (String townUUID : list) {
                if (TownID.equals(townUUID)) {
                    return relation;
                }
            }
        }
        return null;
    }

    public void removeAllRelationWith(String townID){
        for(TownRelation relation : TownRelation.values()){
            townRelations.get(relation).remove(townID);
        }
    }

    public void cleanAll(String ownTownID){
        for(TownRelation relation : TownRelation.values()){
            for (String townID : townRelations.get(relation)) {
                TownDataStorage.get(townID).getRelations().removeAllRelationWith(ownTownID);
                TownDataStorage.get(townID).broadCastMessage(ChatUtils.getTANString() +
                        Lang.WARNING_OTHER_TOWN_HAS_BEEN_DELETED.getTranslation(
                                TownDataStorage.get(ownTownID).getName(),
                                relation.getColor() + relation.getName())
                );
            }
        }

    }
}