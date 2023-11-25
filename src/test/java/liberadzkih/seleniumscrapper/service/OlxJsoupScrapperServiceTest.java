package liberadzkih.seleniumscrapper.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OlxJsoupScrapperServiceTest {
    private static final String NO_ITEMS_URL = "https://www.olx.pl/motoryzacja/samochody/toyota/mr2/?search%5Bfilter_float_price%3Afr"
        + "om%5D=99999&search%5Bfilter_float_price%3Ato%5D=999999";
    private static final String MANY_ITEMS_URL = "https://www.olx.pl/motoryzacja/samochody/toyota/";

    @Test
    void should_find_at_least_one_item() {
        final var olxScrapperService = new OlxJsoupScrapperService();
        final var items = olxScrapperService.scrap(MANY_ITEMS_URL);

        assertThat(items.size()).isGreaterThan(0);
    }

    @Test
    void should_not_find_any_item() {
        final var items = new OlxJsoupScrapperService().scrap(NO_ITEMS_URL);
        assertThat(items.size()).isEqualTo(0);
    }

    @Test
    void found_items_should_contain_necessary_data() {
        final var items = new OlxJsoupScrapperService().scrap(MANY_ITEMS_URL);
        assertThat(items.size()).isGreaterThan(0);

        items.forEach(olxItem -> {
            assertThat(olxItem.getId()).isNotEmpty();
            assertThat(olxItem.getUrl()).isNotEmpty();
            assertThat(olxItem.getSearchUrl()).isEqualTo(MANY_ITEMS_URL);
            assertThat(olxItem.getTitle()).isNotEmpty();
            //assertThat(olxItem.getDescription()).isNotEmpty();
            assertThat(olxItem.getAddress()).isNotEmpty();
            assertThat(olxItem.getPrice()).isGreaterThan(0);
        });
    }
}
