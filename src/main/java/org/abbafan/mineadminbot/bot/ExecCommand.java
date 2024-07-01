package org.abbafan.mineadminbot.bot;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ExecCommand extends BukkitRunnable {
    @Override
    public void run() {
        if (BotTools.commandQueue.size() != 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), BotTools.commandQueue.get(0));
            System.out.println("Command: " + BotTools.commandQueue.get(0) + " executed");
            BotTools.commandQueue.remove(0);
        }
    }
}
