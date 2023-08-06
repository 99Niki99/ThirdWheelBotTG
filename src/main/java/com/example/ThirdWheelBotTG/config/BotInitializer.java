package com.example.ThirdWheelBotTG.config;

import com.example.ThirdWheelBotTG.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This class is responsible for initializing the bot
 * @author Nikita
 */
@Slf4j
@Component
public class BotInitializer {
    @Autowired
    TelegramBot bot;

    /**
     * This method is called when the application is ready to service requests
     * @throws TelegramApiException
     */

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error while occured: " + e.getMessage());
        }
    }

}
