package io.cucumber.skeleton;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;


public class StepDefinition {

    public StepDefinition() {
    }

    WebDriver driver;
    WebDriverWait wait;
    private String currentUrl;
    private final Logger logger = LoggerFactory.getLogger(StepDefinition.class);

    @Before
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User2\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }


        @Given("пользователь находится на главной странице сайта")
        public void userAreOnTheMainPage() {

            driver.get("https://www.dns-shop.ru/");
            logger.info("Пользователь открыл главную страницу сайта.");

            currentUrl = driver.getCurrentUrl();
        }


        @And("поисковая строка отображается на странице")
        public void theSearchPageIsShown () {

            By searchBoxLocator = By.cssSelector("input[placeholder='Поиск по сайту']");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));
            if (!searchBox.isDisplayed()) {
                throw new AssertionError("Поисковая строка не отображается.");
            }
            logger.info("Поисковая строка отображается на странице.");
        }

        @When("пользователь вводит в поисковую строку наименование существующего товара, например, {string}")
        public void userInputTheExistItemOnTheSearchHold (String itemExistName){
            By searchBoxLocator = By.cssSelector("input[placeholder='Поиск по сайту']");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));
            searchBox.sendKeys(itemExistName);
            logger.info("Пользователь ввёл в поисковую строку: {}", itemExistName);
        }

        @Then("система отображает список товаров, соответствующих запросу")
        public void systemDisplaysMatchingItems () {
            By searchButtonLocator = By.cssSelector("input[placeholder='Поиск по сайту']");
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(searchButtonLocator));
            searchButton.click();

            By itemListLocator = By.cssSelector("#search-results .products-list .catalog-product");
            List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemListLocator));

            if (items.isEmpty()) {
                throw new AssertionError("Список товаров отсутствует.");
            }
            logger.info("Система отображает список товаров.");
        }

        @And("в списке отображаются названия товаров, содержащие {string}")
        public void itemsContainKeyword (String keyword){

            By itemNameLocator = By.cssSelector("h1.product-card-top__title");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemNameLocator));

            boolean allItemsContainKeyword = true;
            for (WebElement item : items) {
                if (!item.getText().toLowerCase().contains(keyword.toLowerCase())) {
                    allItemsContainKeyword = false;
                    logger.info("Название товара, которое не соответствует ключевому слову: " + item.getText());
                }
            }

            if (allItemsContainKeyword) {
                logger.info("Все товары содержат ключевое слово: {}", keyword);
            } else {
                throw new AssertionError("Некоторые товары не содержат ключевое слово: " + keyword);
            }
        }

    @When("пользователь вводит в поисковую строку наименование товара, отсутствующего в ассортименте, например, {string}")
    public void userEntersNonExistentItem(String itemName) {
        By searchBoxLocator = By.cssSelector("input[placeholder='Поиск по сайту']");
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));
        searchBox.sendKeys(itemName);
        logger.info("Пользователь ввёл в поисковую строку: {}", itemName);
    }

    @Then("система отображает сообщение {string}")
    public void systemDisplaysMessage(String expectedMessage) {
        By noResultsMessageLocator = By.xpath("//div[contains(text(), 'По точному совпадению результатов не найдено')]");
        WebElement noResultsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessageLocator));
        String actualMessage = noResultsMessage.getText();
        if (!actualMessage.contains(expectedMessage)) {
            throw new AssertionError("Ожидалось сообщение: " + expectedMessage + ", но было: " + actualMessage);
        }
        logger.info("Система отображает сообщение: {}", expectedMessage);
    }

    @And("система предлагает список товаров с похожими ключевыми словами, например, {string}")
    public void systemSuggestsSimilarItems(String keyword) {
        By suggestedItemsLocator = By.cssSelector(".catalog-product");
        List<WebElement> suggestedItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(suggestedItemsLocator));
        if (suggestedItems.isEmpty()) {
            throw new AssertionError("Список товаров с похожими ключевыми словами отсутствует.");
        }
        logger.info("Система предлагает список товаров с похожими ключевыми словами:");
        for (WebElement item : suggestedItems) {
            String itemText = item.getText();
            System.out.println(" - " + itemText);
            if (!itemText.toLowerCase().contains(keyword.toLowerCase())) {
                throw new AssertionError("Название товара не содержит ключевого слова: " + keyword);
            }
        }
    }

    @When("пользователь нажимает кнопку поиска, не вводя текст в строку")
    public void userClicksSearchButtonWithoutText() {
        By searchBoxLocator = By.cssSelector("input[placeholder='Поиск по сайту']");
        WebElement searchBox = driver.findElement(searchBoxLocator);
        searchBox.sendKeys("");
        searchBox.sendKeys(Keys.ENTER);
        logger.info("Пользователь нажимает кнопку поиска, не вводя текст в строку.");
    }

    @Then("система остаётся на текущей странице")
    public void systemStaysOnCurrentPage() {
        String newUrl = driver.getCurrentUrl();
        if (!newUrl.equals(currentUrl)) {
            throw new AssertionError("Система перешла на другую страницу. Ожидалось: " + currentUrl + ", но было: " + newUrl);
        }
        logger.info("Система осталась на текущей странице: " + currentUrl);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
