package org.abbafan.mineadminbot.bot;

import org.abbafan.mineadminbot.Mineadminbot;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;

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
    public List getPlayerNamesList(){
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }
    public List getBannedPlayerNamesLisrt(){
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Bukkit.getBanList(BanList.Type.NAME).isBanned(player.getUniqueId().toString())) {
                players.add(player.getName());
            }
        }
        return players;
    }
    public  void banPlayer (String playername){
        Player player = Bukkit.getPlayer(playername);
        if (player != null) {
            Handler.kickedPlayers.put(player, "Ваc забанил " + this.firstname + " " + this.lastname);
            UUID playerid = player.getUniqueId();
            Bukkit.getBanList(BanList.Type.NAME).addBan(playerid.toString(), "Ваc забанил " + this.firstname + " " + this.lastname, null, null);

            BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был забанен");
        }
        else BotTools.sendMessage(this.chatId.toString(), "Такого игрока нет");

    }
    public void unbanPlayer (String playername){
        Player player = Bukkit.getPlayer(playername);
        if (player != null) {
            UUID playerid = player.getUniqueId();
            Bukkit.getBanList(BanList.Type.NAME).pardon(playerid.toString());
            BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был разбанен");
        }
        else BotTools.sendMessage(this.chatId.toString(), "Такого игрока нет");
    }
    public void mutePlayer (String playername){;
        Mineadminbot.mute.mutePlayer(playername, "Ваc замьютил " + this.firstname + " " + this.lastname);
        BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был замьючен");
    }
    public void mutePlayerMessage(){
        InlineKeyboardMarkup onlinePlayersKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keybcol = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()){
            InlineKeyboardButton playerbutton = new InlineKeyboardButton();
            playerbutton.setText(player.getName());
            playerbutton.setCallbackData("mute" + player.getName());
            keybcol.add(playerbutton);
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keybcol);
        onlinePlayersKeyboard.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Выберите игрока");
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(onlinePlayersKeyboard);
        try {
            BotTools.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error while sending message: " + e);
        }
    }
    public void unmutePlayerMessage (){
         InlineKeyboardMarkup mutedPlayers = new InlineKeyboardMarkup();
         List<InlineKeyboardButton> keybcol = new ArrayList<>();
         List<String> players = Mineadminbot.mute.getMutedPlayers();
         for (String playername : players){
             InlineKeyboardButton unmplayer = new InlineKeyboardButton();
             unmplayer.setText(playername);
             unmplayer.setCallbackData("unm" + playername);
             keybcol.add(unmplayer);
         }
         List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
         keyboard.add(keybcol);
         mutedPlayers.setKeyboard(keyboard);
         SendMessage sendMessage = new SendMessage();
         sendMessage.setText("Выберите игрока");
         sendMessage.setChatId(this.chatId);
         sendMessage.setReplyMarkup(mutedPlayers);
         try {
             BotTools.bot.execute(sendMessage);
         } catch (TelegramApiException e) {
             System.out.println("Error while sending message: " + e);
         }
    }
    public void banPlayerMessage(){
        InlineKeyboardMarkup onlinePlayersKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keybcols = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()){
            InlineKeyboardButton playerbutton = new InlineKeyboardButton();
            playerbutton.setText(player.getName());
            playerbutton.setCallbackData("ban" + player.getName());
            keybcols.add(playerbutton);
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keybcols);
        onlinePlayersKeyboard.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Выберите игрока");
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(onlinePlayersKeyboard);
        try {
            BotTools.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error while sending message: " + e);
        }
    }
    public void unbanPlayerMessage(){
        InlineKeyboardMarkup onlinePlayersKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keybcols = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getServer().getBannedPlayers()){
            InlineKeyboardButton playerbutton = new InlineKeyboardButton();
            playerbutton.setText(player.getName());
            playerbutton.setCallbackData("unb" + player.getName());
            keybcols.add(playerbutton);
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keybcols);
        onlinePlayersKeyboard.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Выберите игрока");
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(onlinePlayersKeyboard);
        try {
            BotTools.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error while sending message: " + e);
        }
    }

    public void kickPlayerMessage(){
        InlineKeyboardMarkup onlinePlayersKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keybcols = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()){
            InlineKeyboardButton playerbutton = new InlineKeyboardButton();
            playerbutton.setText(player.getName());
            playerbutton.setCallbackData("kick" + player.getName());
            keybcols.add(playerbutton);
        }
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keybcols);
        onlinePlayersKeyboard.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Выберите игрока");
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(onlinePlayersKeyboard);
        try {
            BotTools.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error while sending message: " + e);
        }
    }

    public void kickPlayer(String playername){
        Handler.kickedPlayers.put(Bukkit.getPlayer(playername), "Ваc кикнул " + this.firstname + " " + this.lastname);
        BotTools.sendMessage(this.chatId.toString(), "Игрок " + playername + " был кикнут");
    }



}
