package liberadzkih.seleniumscrapper.service;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import liberadzkih.seleniumscrapper.model.OlxItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OlxJsoupScrapperService {
    private static final List<String> NO_ITEMS_FOUND_INFO_LIST = List.of("Znaleźliśmy  0 ogłoszeń", "Znaleźliśmy 0 ogłoszeń",
        ">Nie znaleźliśmy żadnych", "Więcej ogłoszeń w kategorii Motoryzacja");

    private static final String ELEMENTS_SELECTOR_1 = "div[data-cy=l-card]";
    private static final int TIMEOUT_MILLIS = 10000;

    public List<OlxItem> scrap(final String searchUrl) {
        try {
            final var document = getDocumentFromUrl(searchUrl);
            final var elements = getElements(document);
            return elements.stream()
                .map(this::getOlxItemFromElement)
                .filter(Objects::nonNull)
                .peek(item -> item.setSearchUrl(searchUrl))
                .collect(Collectors.toList());
        } catch (final Exception ex) {
            log.error("Exception caught in OlxJsoupScrapperService: " + ex.getMessage());
            return null;
        }
    }

    private OlxItem getOlxItemFromElement(final Element element) {
        try {
            final var item = new OlxItem();
            item.setId(element.attr("id"));
            item.setTitle(element.select("h6").text().replace("\n", ""));
            item.setUrl(getAdUrlFromElement(element));
            item.setPrice(getPriceFromElement(element, "p[data-testid=ad-price]", "\\D+"));
            item.setAddress(element.select("p[data-testid=location-date]").first().text().split(" - ")[0]);
            return item;
        } catch (final Exception ex) {
            return null;
        }
    }

    List<Element> getElements(final Document document) {
        if (document == null) {
            return null;
        }
        if (containsAnyIllegalSentence(document)) {
            return List.of();
        }
        return document.select(ELEMENTS_SELECTOR_1);
    }

    private Document getDocumentFromUrl(final String url) {
        try {
            return Jsoup.parse(new URL(url), TIMEOUT_MILLIS);
        } catch (final Exception e) {
            log.error("Failed to create document from url={}", url);
            return null;
        }
    }

    private boolean containsAnyIllegalSentence(final Document document) {
        return NO_ITEMS_FOUND_INFO_LIST.stream().anyMatch(sentence -> document.toString().contains(sentence));
    }

    private String getAdUrlFromElement(final Element element) {
        final var href = element.select("a").first().attr("href");
        return href.contains("https://www.otomoto.pl") ? href : "https://www.olx.pl" + href;
    }

    int getPriceFromElement(final Element element, final String selector, final String regex) {
        try {
            final var priceString = element.select(selector).first().text().replace("\n", "");
            return Integer.parseInt(priceString.replaceAll(regex, ""));
        } catch (final Exception e) {
            return 0;
        }
    }
}
