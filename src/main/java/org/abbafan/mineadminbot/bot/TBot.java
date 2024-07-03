package org.abbafan.mineadminbot.bot;

import org.abbafan.mineadminbot.Mineadminbot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TBot extends TelegramLongPollingBot {
    public boolean configured = false;
    private String token = "changeme";
    private String username = "changeme";
    private Map<String, String> mode = new HashMap<>();

    public TBot() {
        YamlConfiguration config = new YamlConfiguration();
        File configFile = new File("plugins/mineadminbot/config.yml");
        if (configFile.exists()) {
            try {
                config.load(configFile);
                token = config.getString("token");
                username = config.getString("username");
                System.out.println("Config file loaded, username: " + username);
            } catch (Exception e) {
                System.out.println("Error while loading config file: " + e);
            }
        }
        else {
            try {
                config.set("token", token);
                config.set("username", username);
                config.save(configFile);
                System.out.println("Created config file, change token and chatId in minebot.yml");
            } catch (Exception e) {
                System.out.println("Error while creating config file: " + e);
            }
        }
        if (!token.equals("changeme") && !username.equals("changeme")) {
            configured = true;
            System.out.println("Bot is ready to use");
        }
        else {
            System.out.println("Config error, chenge token and chatId in minebot.yml");
        }

    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
           if (update.getCallbackQuery().getData().toString().startsWith("ban")){
               String playername = update.getCallbackQuery().getData().toString().replace("ban", "");
               Admin admin = Admin.getAdmin(update.getCallbackQuery().getMessage().getChatId());
               admin.banPlayer(playername);
           }
           if (update.getCallbackQuery().getData().toString().startsWith("kick")){
               String playername = update.getCallbackQuery().getData().toString().replace("kick", "");
               Admin admin = Admin.getAdmin(update.getCallbackQuery().getMessage().getChatId());
               admin.kickPlayer(playername);
           }
           if (update.getCallbackQuery().getData().toString().startsWith("mute")){
               String playername = update.getCallbackQuery().getData().toString().replace("mute", "");
               Admin admin = Admin.getAdmin(update.getCallbackQuery().getMessage().getChatId());
               admin.mutePlayer(playername);
           }
           if (update.getCallbackQuery().getData().toString().startsWith("unm")){
               String playername = update.getCallbackQuery().getData().toString().replace("unm", "");
               Mineadminbot.mute.unmutePlayer(playername);
               BotTools.sendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), "Игрок " + playername + " размьючен!");
           }
           if (update.getCallbackQuery().getData().toString().startsWith("unb")){
               String playername = update.getCallbackQuery().getData().toString().replace("unb", "");
               Admin admin = Admin.getAdmin(update.getCallbackQuery().getMessage().getChatId());
               admin.unbanPlayer(playername);
               BotTools.sendMessage(update.getCallbackQuery().getMessage().getChatId().toString(), "Игрок " + playername + " разбанен!");
           }

        }
        if (update.hasMessage()) {
            if (update.getMessage().getText().startsWith("/")) {
                if (update.getMessage().getText().equals("/start")) {
                    BotTools.registeruser(update.getMessage());
                }
                if (update.getMessage().getText().equals("/mute")){
                    Admin admin = Admin.getAdmin(update.getMessage().getChatId());
                    if (admin!=null) admin.mutePlayerMessage();
                    else BotTools.sendMessage(update.getMessage().getChatId().toString(), "У вас не достаточно прав!");
                }
                if (update.getMessage().getText().equals("/unmute")){
                    Admin admin = Admin.getAdmin(update.getMessage().getChatId());
                    if (admin!=null) admin.unmutePlayerMessage();
                    else BotTools.sendMessage(update.getMessage().getChatId().toString(), "У вас не достаточно прав!");
                }
                if (update.getMessage().getText().equals("/ban")){
                    Admin admin = Admin.getAdmin(update.getMessage().getChatId());
                    if (admin!=null) admin.banPlayerMessage();
                    else BotTools.sendMessage(update.getMessage().getChatId().toString(), "У вас не достаточно прав!");
                }
                if (update.getMessage().getText().equals("/unban")){
                    Admin admin = Admin.getAdmin(update.getMessage().getChatId());
                    if (admin!=null) admin.unbanPlayerMessage();
                    else BotTools.sendMessage(update.getMessage().getChatId().toString(), "У вас не достаточно прав!");
                }
                if (update.getMessage().getText().equals("/kick")){
                    Admin admin = Admin.getAdmin(update.getMessage().getChatId());
                    if (admin!=null) admin.kickPlayerMessage();
                    else BotTools.sendMessage(update.getMessage().getChatId().toString(), "У вас не достаточно прав!");
                }
            } else {
                BotTools.addCommand(update.getMessage().getChatId(), update.getMessage().getText());
            }
        }

    }
}
