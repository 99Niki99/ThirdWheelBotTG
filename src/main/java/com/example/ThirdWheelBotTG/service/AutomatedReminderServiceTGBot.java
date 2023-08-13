package com.example.ThirdWheelBotTG.service;

import com.example.ThirdWheelBotTG.model.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * This class is responsible for sending reminders to users
 * It is scheduled to run every minute
 * It checks if there are any reminders to send
 * If there are, it sends them
 */
@Slf4j
@Service
public class AutomatedReminderServiceTGBot {


        private LocalDate reminderDate;
        private LocalTime reminderTime;

        String[] mesageTxt = new String[3];

        /**
         * This method is scheduled to run every minute
         * It checks if there are any reminders to send
         * If there are, it sends them
         */
        public void separateMsd(String msg) {
            ArrayList<String> words = new ArrayList<>(List.of(msg.split(" ")));
            for (String word : words) {
                if (word.matches("[0-9]{2}.[0-9]{2}")) {
                    mesageTxt[0] = word;
                } else if (word.matches("[A-z]")) {
                    mesageTxt[1] = word;
                } else if (word.matches("@[A-z]+")) {
                    mesageTxt[2] = word;
                }
            }
            words.clear();
        }

        /**
         * This method is scheduled to run every minute
         * It checks if there are any reminders to send
         * If there are, it sends them
         */

        public void putReminderInData() {
            if (mesageTxt[0] != null && mesageTxt[1] != null && mesageTxt[2] != null) {
                Reminder reminder = new Reminder();
                reminder.setTxtMesdsage(mesageTxt[1]);
                reminder.setTime(mesageTxt[0]);
                reminder.setUserName(mesageTxt[2]);
                log.info("Reminder was sent to " + mesageTxt[2] + " at " + mesageTxt[0] + " with text: " + mesageTxt[1]);
            }
        }

        /**
         * This method is scheduled to run every minute
         * It checks if there are any reminders to send
         * If there are, it sends them
         */

        @Scheduled(cron = "0 0 1 * * ?")
        public void sendReminder() {
            LocalDateTime now = LocalDateTime.now();
            if (reminderDate != null && reminderTime != null) {
                LocalDateTime reminderDateTime = LocalDateTime.of(reminderDate, reminderTime);

                if (now.isEqual(reminderDateTime)) {
                    SendMessage message = new SendMessage();
                    message.setText(mesageTxt[1]);
                    message.setChatId(mesageTxt[2]);
                    log.info("Reminder was sent to " + mesageTxt[2] + " at " + mesageTxt[0] + " with text: " + mesageTxt[1]);
                }
            }
        }
}
