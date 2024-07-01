package org.abbafan.mineadminbot.bot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Handler extends BukkitRunnable {
    public static ArrayList<String> commandQueue = new ArrayList<>();
    public static Map<Player, String> kickedPlayers = new HashMap<>();

    @Override
    public void run() {
        if (commandQueue.size() != 0) {
            for (String command : commandQueue) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                System.out.println("Command: " + command + " executed");
            }
            commandQueue.clear();
        }

        if (kickedPlayers.size() != 0){
            for(Player player : kickedPlayers.keySet()){
                player.kickPlayer(kickedPlayers.get(player));
            }
            kickedPlayers.clear();
        }
    }
}
