package com.example.ThirdWheelBotTG.model;

import jakarta.persistence.Entity;
import org.springframework.data.repository.CrudRepository;

/**
 * This class represents a user in the database
 * It is used to store information about users
 * It is used to retrieve information about users
 * It is used to update information about users
 * It is used to delete information about users
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
