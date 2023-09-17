package com.example.ThirdWheelBotTG.service;


import com.example.ThirdWheelBotTG.config.BotConfig;
import com.example.ThirdWheelBotTG.model.User;
import com.example.ThirdWheelBotTG.model.UserRepository;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for receiving updates from Telegram API
 * and sending messages to users
 *
 * @author Nikita
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {



    static final String HELP_TXT = "This is a bot for ThirdWheel app. " +
            "You can use it to find a company for your trip. " +
            "To start using it, type /start.";
    static  final String REMINDER_TXT = "Enter the date of the event in the format dd.mm.yyyy, and remind text, and @username of the person you want to remind";
    private final UserRepository userRepository;
    private final BotConfig config;
    private final AutomatedReminderService automatedReminderService;

    /**
     * Constructor for TelegramBot
     * It sets bot's commands
     */
    @Inject
    public TelegramBot(UserRepository userRepository, BotConfig config, AutomatedReminderService automatedReminderService) {
        this.userRepository = userRepository;
        this.config = config;
        this.automatedReminderService = automatedReminderService;

    }

    public void TelegramBotComand() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Start using bot"));
        listOfCommands.add(new BotCommand("/mydata", "Get your data"));
        listOfCommands.add(new BotCommand("/deletedata", "Delete your data"));
        listOfCommands.add(new BotCommand("/help", "Get help"));
        listOfCommands.add(new BotCommand("/settings", "Change settings"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error while setting commands: " + e.getMessage());
        }

    }

    /**
     * This method returns bot's name
     *
     * @return bot's name
     */
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


    /**
     * This method returns bot's token
     *
     * @return bot's token
     */
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    /**
     * This method is called when receiving updates via GetUpdates method
     *
     * @param update Update received
     */

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                registerUser(update.getMessage());
                sendMessage( chatId, "Hello" + update.getMessage().getChat().getFirstName() + "!");
            } else if (messageText.equals("/help")) {
                sendMessage(chatId, HELP_TXT);
            } else if (messageText.equals("/makeReminder")) {
                sendMessage(chatId, REMINDER_TXT);
            } else  if (messageText.startsWith("Reminder")) {
                 automatedReminderService.checkPutAndSendReminder(update.getMessage());
            } else {
                sendMessage(chatId, "I dont understand you");
            }
        }
    }

    private void registerUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("User " + user.getFirstName() + " " + user.getLastName() + " registered");
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help");
        row.add("/mydata");
        keyboardRow.add(row);

        row = new KeyboardRow();

        row.add("/settings");
        row.add("/deletedata");
        keyboardRow.add(row);

        keyboardMarkup.setKeyboard(keyboardRow);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error while sending message: " + e.getMessage());
        }
    }

}
