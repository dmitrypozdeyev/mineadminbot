package org.abbafan.mineadminbot;

import org.abbafan.mineadminbot.bot.BotTools;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mineadminbot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic


        BotTools.runbot();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
