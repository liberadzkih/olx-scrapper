package liberadzkih.seleniumscrapper.config;

import java.util.Comparator;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import liberadzkih.seleniumscrapper.model.OlxItem;
import liberadzkih.seleniumscrapper.repository.OlxItemRepository;
import liberadzkih.seleniumscrapper.service.NotificationService;
import liberadzkih.seleniumscrapper.service.OlxJsoupScrapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableScheduling
class SchedulerConfiguration {

    private final OlxJsoupScrapperService olxJsoupScrapperService;
    private final OlxItemRepository olxItemRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 10000)
    void scrapOlx() {
        final var idsFromDb = olxItemRepository.findAll().stream().map(OlxItem::getId).toList();
        final var adUrlsFromDb = olxItemRepository.findAll().stream().map(OlxItem::getUrl).toList();

        ConfigurationVariables.OLX_URLS.forEach(url -> {
            CompletableFuture.runAsync(() -> {
                final var jsoupScrapped = olxJsoupScrapperService.scrap(url);
                jsoupScrapped.stream()
                    .filter(Objects::nonNull)
                    .filter(item -> isUrlValid(item.getUrl())) //validate item url
                    .filter(item -> !idsFromDb.contains(item.getId()) && !adUrlsFromDb.contains(item.getUrl())) // validate that item id and url not exists in db
                    .collect(Collectors.toCollection(() -> new TreeSet<>((Comparator.comparing(OlxItem::getId))))).stream() //remove duplicates
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OlxItem::getUrl)))) //remove duplicates
                    .forEach(item -> {
                        notificationService.notifyByTelegram(item);
                        olxItemRepository.save(item);
                    });
            });
        });
    }

    private static boolean isUrlValid(final String url) {
        return !Strings.isBlank(url) && !url.contains("reason=extended_search_no_results_last_resort");
    }
}
