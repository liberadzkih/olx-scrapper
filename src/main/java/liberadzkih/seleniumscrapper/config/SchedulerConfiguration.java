package liberadzkih.seleniumscrapper.config;

import java.util.List;
import java.util.Objects;
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

    @Scheduled(fixedRate = 3000)
    void scrapOlxMain() {
        scrap(ConfigurationVariables.OLX_URLS_MAIN);
    }

    @Scheduled(fixedRate = 15000)
    void scrapOlxAdditional() {
        scrap(ConfigurationVariables.OLX_URLS_ADDITIONAL);
    }

    private void scrap(final List<String> urls) {
        var allOlxItem = olxItemRepository.findAll();
        var idsFromDb = allOlxItem.stream().map(OlxItem::getId).collect(Collectors.toList());
        var adUrlsFromDb = allOlxItem.stream().map(OlxItem::getUrl).collect(Collectors.toList());

        urls.forEach(url -> {
            CompletableFuture.runAsync(() -> {
            final var jsoupScrapped = olxJsoupScrapperService.scrap(url);
            jsoupScrapped.stream()
                .filter(Objects::nonNull)
                .filter(item -> isUrlValid(item.getUrl())) //validate item url
                .filter(item -> !idsFromDb.contains(item.getId()) && !adUrlsFromDb.contains(item.getUrl())) // validate that item id and url not exists in db
                //.collect(Collectors.toCollection(() -> new TreeSet<>((Comparator.comparing(OlxItem::getId))))).stream() //remove duplicates
                //.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OlxItem::getUrl)))) //remove duplicates
                .forEach(item -> {
                    olxItemRepository.save(item);
                    idsFromDb.add(item.getId());
                    adUrlsFromDb.add(item.getUrl());
                    notificationService.notifyByTelegram(item);
                });
            });
        });
    }

    private static boolean isUrlValid(final String url) {
        return !Strings.isBlank(url) && !url.contains("reason=extended_search_no_results_last_resort");
    }
}
