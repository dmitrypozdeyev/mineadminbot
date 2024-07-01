package org.abbafan.mineadminbot.bot;

import org.bukkit.configuration.file.YamlConfiguration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;

public class BotTools {
    public  static TBot bot = null;

    public static void runbot(){
        bot = new TBot();
        if (bot.configured) {
            try {
                TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
                api.registerBot(bot = new TBot());
            } catch (TelegramApiException e) {
                System.out.println("Error while running bot: " + e);
            }
        }

    }
    public static boolean registeruser(Message message){
        YamlConfiguration user = new YamlConfiguration();
        File userFile = new File("plugins/mineadminbot/users/" + message.getChatId() + ".yml");
        if (!userFile.exists()) {
            try {
                user.set("username", message.getChat().getUserName());
                user.set("userid", message.getChatId());
                user.set("admin", false);
                user.set("firstname", message.getChat().getFirstName());
                user.set("lastname", message.getChat().getLastName());
                user.save(userFile);
                return true;
            } catch (Exception e) {
                System.out.println("Error while creating user file: " + e);
            }
        }
        return false;
    }
}
