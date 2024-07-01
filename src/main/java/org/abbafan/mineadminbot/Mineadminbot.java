package org.abbafan.mineadminbot;

import org.abbafan.mineadminbot.bot.BotTools;
import org.abbafan.mineadminbot.bot.Handler;
import org.abbafan.mineadminbot.commands.ReportCMD;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mineadminbot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Handler execCommand = new Handler();
        execCommand.runTaskTimer(this, 0, 1);
        getCommand("report").setExecutor(new ReportCMD());


        BotTools.runbot();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
