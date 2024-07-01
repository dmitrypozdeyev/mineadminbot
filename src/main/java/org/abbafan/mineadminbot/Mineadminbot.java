package org.abbafan.mineadminbot;

import org.abbafan.mineadminbot.bot.BotTools;
import org.abbafan.mineadminbot.bot.ExecCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Mineadminbot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ExecCommand execCommand = new ExecCommand();
        execCommand.runTaskTimer(this, 0, 1);


        BotTools.runbot();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
