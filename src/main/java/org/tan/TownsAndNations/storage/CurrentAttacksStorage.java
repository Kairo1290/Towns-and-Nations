package org.tan.TownsAndNations.storage;

import org.tan.TownsAndNations.DataClass.wars.PlannedAttack;
import org.tan.TownsAndNations.DataClass.wars.CurrentAttacks;

import java.util.HashMap;
import java.util.Map;

public class CurrentAttacksStorage {
    private static final Map<String, CurrentAttacks> attackStatusMap = new HashMap<>();

    public static void startAttack(PlannedAttack plannedAttack){
        String newID = getNextID();
        attackStatusMap.put(newID, new CurrentAttacks(newID, plannedAttack));
    }

    public static void remove(CurrentAttacks currentAttacks){
        attackStatusMap.remove(currentAttacks.getID());
    }

    private static String getNextID(){
        int ID = 0;
        while(attackStatusMap.containsKey("A"+ID)){
            ID++;
        }
        return "A"+ID;
    }

    public static CurrentAttacks get(String ID) {
        return attackStatusMap.get(ID);
    }
}
