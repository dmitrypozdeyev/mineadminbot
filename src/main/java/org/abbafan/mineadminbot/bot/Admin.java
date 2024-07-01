package org.abbafan.mineadminbot.bot;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private Long chatId;
    private String username;
    private String firstname;
    private String lastname;

    private Admin(Long chatId){
        YamlConfiguration user = new YamlConfiguration();
        File userFile = new File("plugins/mineadminbot/users/" + chatId + ".yml");
        try {
            user.load(userFile);
            this.chatId = user.getLong("userid");
            this.username = user.getString("username");
            this.firstname = user.getString("firstname");
            this.lastname = user.getString("lastname");
        } catch (Exception e) {
            System.out.println("Error while loading user file: " + e);
        }
    }

    public static Admin getAdmin(Long chatId){
        if (BotTools.isAdmin(chatId)) {
            return new Admin(chatId);
        }
        else return null;
    }
    public Long getChatId(){
        return chatId;
    }

    public String getUsername(){
        return username;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public void sendReport(String playername, String report){

        InlineKeyboardMarkup adminActs = new InlineKeyboardMarkup();
        InlineKeyboardButton ban = new InlineKeyboardButton();
        ban.setText("Забанить");
        ban.setCallbackData("ban"+ playername);

        InlineKeyboardButton kick = new InlineKeyboardButton();
        kick.setText("Кикнуть");
        kick.setCallbackData("kick"+ playername);

        InlineKeyboardButton mute = new InlineKeyboardButton();
        mute.setText("Замьютить");
        mute.setCallbackData("mute"+ playername);


        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(new ArrayList<>());
        keyboard.get(0).add(ban);
        keyboard.get(0).add(kick);
        keyboard.get(0).add(mute);
        adminActs.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.chatId);
        sendMessage.setText("На игорнка " + playername + " " + " поступила жалоба. \nТекст жалобы: " + report + "\nВыберите действие:" );
        sendMessage.setReplyMarkup(adminActs);

        try {
            BotTools.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error while sending message: " + e);
        }


    }
    public  void banPlayer (String playername){
        Player player = Bukkit.getPlayer(playername);
        if (player != null) {
            Handler.kickedPlayers.put(player, "Ваc забанил " + this.firstname + " " + this.lastname);
            Bukkit.getServer().getBannedPlayers().add(player);
            BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был забанен");
        }
        else BotTools.sendMessage(this.chatId.toString(), "Такого игрока нет");

    }
    public void kickPlayer (String playername){
        Player player = Bukkit.getPlayer(playername);
        if (player != null) {
            Handler.kickedPlayers.put(player, "Ваc кикнул " + this.firstname + " " + this.lastname);
            BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был кикнут");
        }
        else BotTools.sendMessage(this.chatId.toString(), "Такого игрока нет");
    }

}
