package org.abbafan.mineadminbot.events;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mute implements Listener {
    YamlConfiguration mutedPlayers = new YamlConfiguration();
    File mutedPlayersFile = new File("plugins/mineadminbot/mutedPlayers.yml");

    public Mute() {

        if (!mutedPlayersFile.exists()) {
            try {
                List playerstate = new ArrayList();
                playerstate.add("reason");
                playerstate.add(false);
                mutedPlayers.set("playername",playerstate);
                mutedPlayers.save(mutedPlayersFile);
            } catch (IOException e) {
                System.out.println("Error while creating new mutedPlayers file: " + e);
            }
        } else {
            try {
                mutedPlayers.load(mutedPlayersFile);
            } catch (IOException | YAMLException e) {
                System.out.println("Error while loading mutedPlayers file: " + e);
            } catch (InvalidConfigurationException e) {
                System.out.println("Error while loading mutedPlayers file: " + e);
            }
        }

    }

    public void mutePlayer(String playername, String reason){
        List playerstate = new ArrayList();
        playerstate.add(reason);
        playerstate.add(true);

        mutedPlayers.set(playername, playerstate);
        try{
            mutedPlayers.save(mutedPlayersFile);
        } catch (IOException e){
            System.out.println("Error while saving mutedPlayers file: " + e);
        }
    }

    public List getMutedPlayers(){
        List players = new ArrayList();
        for (String player : mutedPlayers.getKeys(false)) {
            if (mutedPlayers.getList(player).get(1).equals(true)) {
                players.add(player);
            }
        }
        return players;
    }

    public void unmutePlayer(String playername){
        List playerstate = new ArrayList();
        playerstate.add("reason");
        playerstate.add(false);
        mutedPlayers.set(playername, playerstate);
        try{
            mutedPlayers.save(mutedPlayersFile);
        } catch (IOException e){
            System.out.println("Error while saving mutedPlayers file: " + e);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (mutedPlayers.getList(event.getPlayer().getName().toString()).get(1).equals(true)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(mutedPlayers.getList(event.getPlayer().getName().toString()).get(0).toString());
        }
    }
}
