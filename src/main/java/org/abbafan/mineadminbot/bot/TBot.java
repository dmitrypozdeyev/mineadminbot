package org.abbafan.mineadminbot.bot;

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
        if (update.getMessage().getText().equals("/start")){
            if (BotTools.registeruser(update.getMessage())){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(update.getMessage().getChatId());
                sendMessage.setText("Вы зарегистрированы");
                try {
                    execute(sendMessage);
                } catch (Exception e) {
                    System.out.println("Error while sending message: " + e);
                }
            }
        }


    }
}
