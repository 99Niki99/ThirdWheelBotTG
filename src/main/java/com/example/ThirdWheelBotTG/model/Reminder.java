package com.example.ThirdWheelBotTG.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a reminder in the database
 * It is used to store information about reminders
 * It is used to retrieve information about reminders
 * It is used to update information about reminders
 * It is used to delete information about reminders
 */

@Getter
@Setter
@Entity(name = "remindersDataTable")

public class Reminder {

    @Id
    private String userName;
    private String txtMessage;
    private String time;
}
