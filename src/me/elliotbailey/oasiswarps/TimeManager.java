package me.elliotbailey.oasiswarps;

import java.util.*;

public class TimeManager {

    private static final Map<UUID, Integer> warpCooldown = new HashMap<>();
    private static final List<UUID> teleporting = new ArrayList<UUID>();

    public static void setCooldown(UUID player, int time){
        if(time < 1) {
            warpCooldown.remove(player);
        } else {
            warpCooldown.put(player, time);
        }
    }

    public static int getCooldown(UUID player){
        return warpCooldown.getOrDefault(player, 0);
    }

    public static void addTeleporting(UUID player) {
        if (!teleporting.contains(player)) teleporting.add(player);
    }

    public static void removeTeleporting(UUID player) {
        if (teleporting.contains(player)) teleporting.remove(player);
    }

    public static boolean isTeleporting(UUID player) {
        return teleporting.contains(player);
    }

}
