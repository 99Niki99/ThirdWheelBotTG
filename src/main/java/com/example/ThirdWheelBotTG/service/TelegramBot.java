package com.example.ThirdWheelBotTG.service;


import com.example.ThirdWheelBotTG.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This class is responsible for receiving updates from Telegram API
 * and sending messages to users
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    /**
     * Constructor for TelegramBot
     * @param config BotConfig object
     */
    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    /**
     * This method returns bot's name
     * @return bot's name
     */
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


    /**
     * This method returns bot's token
     * @return bot's token
     */
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    /**
     *  This method is called when receiving updates via GetUpdates method
     * @param update Update received
     */

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    break;
                default: sendMessage(chatId, "I don't understand you");
                    break;
            }
        }
    }

    private void startCommandReceived( long chatId, String name){
        String answer = "Hello, " + name + "!";

        sendMessage(chatId, answer);


    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
