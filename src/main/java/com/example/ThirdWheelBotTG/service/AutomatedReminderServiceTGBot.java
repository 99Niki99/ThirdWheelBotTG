package com.example.ThirdWheelBotTG.service;

import com.example.ThirdWheelBotTG.config.Variables;
import com.example.ThirdWheelBotTG.model.Reminder;
import com.example.ThirdWheelBotTG.model.ReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for sending reminders to users
 * It is scheduled to run every minute
 * It checks if there are any reminders to send
 * If there are, it sends them
 *
 * @author Nikita
 */
@Slf4j
@Service
public class AutomatedReminderServiceTGBot implements AutomatedReminderService {




    @Autowired
    private ReminderRepository reminderRepository;
    static Variables variables = new Variables();

    @Override
    public  void checkPutAndSendReminder(Message msg){
        separateMsg(msg.getText());
        putReminderInData(variables.messageTxt);
        sendReminder(variables.messageTxt);
    }

    /**
     * This method is scheduled to run every minute
     * It checks if there are any reminders to send
     * If there are, it sends them
     */
    private  ArrayList <String> separateMsg(String msg) {
        variables.messageTxt = new ArrayList<String>();
        if(msg.split("\\n+").length < 2) {
            throw new IllegalArgumentException("Not enough arguments");
        } else if (msg.split("\\n+").length > 3) {
            throw new IllegalArgumentException("Too many arguments");
        }else if(msg.split("\\n+").length == 0) {
            throw new IllegalArgumentException("Empty input");
        }else{
            for (String word : msg.split("\\n+")) {
                if (word.matches("[0-9]{2}.[0-9]{2}") || word.matches("[A-z]+") || word.matches("@[A-z]+")) {
                    variables.messageTxt.add(word);

                }
            }
        }
        return variables.messageTxt;
    }

    /**
     * This method is scheduled to run every minute
     * It checks if there are any reminders to send
     * If there are, it sends them
     */

    private  void putReminderInData(ArrayList<String> messageTxt) {
        if (messageTxt.size() == 3) {
            Reminder reminder = new Reminder();
            reminder.setTxtMessage(messageTxt.get(1));
            reminder.setTime(messageTxt.get(0));
            variables.reminderDate = LocalDate.parse(messageTxt.get(0), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            reminder.setUserName(messageTxt.get(2));
            reminderRepository.save(reminder);
            log.info("Reminder was sent to " + messageTxt.get(2) + " at " + messageTxt.get(0) + " with text: " + messageTxt.get(1));
        }
    }

    /**
     * This method is scheduled to run every minute
     * It checks if there are any reminders to send
     * If there are, it sends them
     */

    @Scheduled(cron = "0 0 0 * * ?")
    private void sendReminder(List<String> messageTxt) {

        LocalDateTime now = LocalDateTime.now();
        if (variables.reminderDate != null) {
            LocalDateTime reminderDateTime = LocalDateTime.of(variables.reminderDate, LocalTime.of(1, 0));

            if (now.isEqual(reminderDateTime)) {
                SendMessage message = new SendMessage();
                message.setText(messageTxt.get(1));
                message.setChatId(messageTxt.get(2));
                log.info("Reminder was sent to " + messageTxt.get(2) + " at " + messageTxt.get(0) + " with text: " + messageTxt.get(1));
            }
        }
    }


}
