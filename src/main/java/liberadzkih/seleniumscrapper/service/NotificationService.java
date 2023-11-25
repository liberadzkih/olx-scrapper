package liberadzkih.seleniumscrapper.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import liberadzkih.seleniumscrapper.model.OlxItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final static String TOKEN = "6658320559:AAFkqFaFZtApxv-beQXQPeIxiVCIVOKgZJo";
    private final static String CHAT_ID = "-980434455";

    private final TelegramBot telegramBot = new TelegramBot(TOKEN);

    public void notifyByTelegram(final OlxItem item) {
        final var response = sendTelegramMessage(buildMessage(item));
        log.info("Telegram notification: " + item);
        if (!response.isOk()) {
            log.error("Telegram response failure: " + response + "\n" + item);
        }
    }

    public SendResponse sendTelegramMessage(final String message) {
        final var sendMessage = new SendMessage(CHAT_ID, message);
        return telegramBot.execute(sendMessage);
    }

    private String buildMessage(final OlxItem item) {
        return String.format("(%sPLN) %s\n~~~~~~~\n%s\n~~~~~~\n%s",
            item.getPrice(),
            item.getTitle(),
            ("id:" + item.getId()),
            item.getUrl());
    }
}
