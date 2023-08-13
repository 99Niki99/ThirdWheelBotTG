package com.example.ThirdWheelBotTG.model;

/**
 * This class represents a reminder in the database
 * It is used to store information about reminders
 * It is used to retrieve information about reminders
 * It is used to update information about reminders
 * It is used to delete information about reminders
 */
public interface ReminderRepository extends org.springframework.data.repository.CrudRepository<Reminder, Long>{
}
