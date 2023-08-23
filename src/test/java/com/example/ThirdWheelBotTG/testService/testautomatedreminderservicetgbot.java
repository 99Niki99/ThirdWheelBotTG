package com.example.ThirdWheelBotTG.testService;

import com.example.ThirdWheelBotTG.model.Reminder;
import com.example.ThirdWheelBotTG.service.AutomatedReminderServiceTGBot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class testautomatedreminderservicetgbot {

    AutomatedReminderServiceTGBot automatedReminderServiceTGBot = new AutomatedReminderServiceTGBot();
    @Test
    public void testSeparateMsgWithValidInputs() {

        List<String> result = automatedReminderServiceTGBot.separateMsg("12.34\nHello\n@World");

        assertEquals(2, result.size());
        assertEquals("12.34", result.get(0));
        assertEquals("Hello", result.get(1));
        assertEquals("@World", result.get(2));
    }

    @Test
    public void testSeparateMsgWithInvalidInputs() {

        // Test for not enough arguments
        assertThrows(IllegalArgumentException.class, () -> {
            automatedReminderServiceTGBot.separateMsg("12.34");
        });

        // Test for too many arguments
        assertThrows(IllegalArgumentException.class, () -> {
            automatedReminderServiceTGBot.separateMsg("12.34\nHello\n@World\nExtraArg");
        });
    }

    @Test
    public void testSeparateMsgWithEmptyInput() {

        assertThrows(IllegalArgumentException.class, () -> {
            automatedReminderServiceTGBot.separateMsg("");
        });
    }

    @Test
    public void testPutReminderInDataWithValidInput() {
        Reminder mockReminder = mock(Reminder.class);

        List<String> messageTxt = new ArrayList<>();
        messageTxt.add("01.01.2023");
        messageTxt.add("Test reminder");
        messageTxt.add("User123");

        automatedReminderServiceTGBot.putReminderInData(messageTxt, mockReminder);

        verify(mockReminder, times(1)).setTxtMessage("Test reminder");
        verify(mockReminder, times(1)).setTime("01.01.2023");
        verify(mockReminder, times(1)).setUserName("User123");
    }


}
