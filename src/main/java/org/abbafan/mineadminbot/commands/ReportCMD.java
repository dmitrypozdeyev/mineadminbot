package org.abbafan.mineadminbot.commands;

import org.abbafan.mineadminbot.bot.Admin;
import org.abbafan.mineadminbot.bot.BotTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReportCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (Admin admin : BotTools.getAdmins()){
            admin.sendReport(args[0], args[1]);
        }
        return true;
    }
}
