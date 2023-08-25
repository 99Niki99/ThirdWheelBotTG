package com.example.ThirdWheelBotTG.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


/**
 * This class represents a user in the database
 * It is used to store information about users
 * It is used to retrieve information about users
 * It is used to update information about users
 * It is used to delete information about users
 */
@Getter
@Setter
@Entity(name = "usersDataTable")
public class User {

    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;

    /**
     * @return String return the userName
     */
    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }


}

