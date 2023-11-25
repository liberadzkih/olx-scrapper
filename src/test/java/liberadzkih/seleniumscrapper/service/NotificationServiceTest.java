package liberadzkih.seleniumscrapper.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationServiceTest {

    @Test
    void sendTelegramMessage_should_receive_success_sendResponse_object() {
        final var notificationService = new NotificationService();
        final var messageText = "test message";
        final var sendResponse = notificationService.sendTelegramMessage(messageText);

        assertTrue(sendResponse.isOk());
        final var sendResponseMessage = sendResponse.message();
        assertTrue(sendResponseMessage.from().isBot());
        assertTrue(sendResponseMessage.from().id() == 6658320559L);
        assertTrue(sendResponseMessage.from().firstName() == "ToyotaBot");
        assertTrue(sendResponseMessage.from().username() == "LiberToyotaBot");

        assertTrue(sendResponseMessage.chat().id() == -980434455L);
        assertTrue(sendResponseMessage.chat().title() == "Toyoty");
        assertTrue(sendResponseMessage.text().equals(messageText));
    }
}
