package liberadzkih.seleniumscrapper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.github.bonigarcia.wdm.WebDriverManager;
import liberadzkih.seleniumscrapper.model.OlxItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OlxSeleniumScrapperService {

    private static final List<String> NO_ITEMS_FOUND_INFO_LIST = List.of("Znaleźliśmy  0 ogłoszeń", "Znaleźliśmy 0 ogłoszeń",
        ">Nie znaleźliśmy żadnych", "Więcej ogłoszeń w kategorii Motoryzacja");

    public List<OlxItem> scrap(final String searchUrl) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        System.setProperty("webdriver.chrome.silentOutput", "true");
        chromeOptions.addArguments("headless");
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        List<OlxItem> items = new ArrayList<>();
        try {
            webDriver.get(searchUrl);
            if (shouldPageBeScrapped(webDriver)) {
                items = webDriver.findElements(By.cssSelector("div[data-cy='l-card']")).stream()
                    .map(element -> {
                        final var car = new OlxItem();
                        car.setSearchUrl(searchUrl);
                        car.setId(element.getAttribute("id"));
                        final var element1 = element.findElement(By.tagName("a"));
                        car.setUrl(element1.getAttribute("href"));
                        car.setTitle(element1.findElement(By.tagName("h6")).getText());
                        car.setPrice(getPriceFromElement(element1.findElement(By.cssSelector("p[data-testid='ad-price']")).getText()));
                        car.setAddress(element1.findElement(By.cssSelector("p[data-testid='location-date']")).getText());
                        return car;
                    }).collect(Collectors.toList());
            }
            webDriver.quit();
            return items;
        } catch (final Exception ex) {
            log.info("Exception caught: " + ex.getMessage());
            webDriver.quit();
            return List.of();
        }
    }

    private boolean shouldPageBeScrapped(final WebDriver webDriver) {
        final var adsCountText = webDriver.findElement(By.cssSelector("span[data-testid='total-count']")).getText();
        return !NO_ITEMS_FOUND_INFO_LIST.contains(adsCountText);
    }

    int getPriceFromElement(final String priceString) {
        try {
            return Integer.parseInt(priceString.replaceAll("\\D+", ""));
        } catch (final Exception e) {
            return 0;
        }
    }
}
