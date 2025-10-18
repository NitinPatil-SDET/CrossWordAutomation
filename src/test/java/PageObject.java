import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PageObject {

    WebDriver wd;

    // Constructor that takes driver from test class
    public PageObject(WebDriver driver) {
        this.wd = driver;
    }

    // Method to get the visible search box
    public WebElement getSearchBox() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='q' and contains(@class,'wizzy-search-input')]")
        ));
    }

    // Action method
    public void clickOnSearch() {
        WebElement searchbox = getSearchBox();
        searchbox.sendKeys("manifest");
    }

    // Method to get the visible view more button
    public WebElement viewMore() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(" //button[normalize-space()='View More']")
        ));
    }

    // Action method
    public void clickOnViewMore() {
        WebElement viewMore = viewMore();
        viewMore.click();
    }

    //Method to get sortBy
    public WebElement sortBy() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='wizzy-common-select-selector']")
        ));
    }

    // Action method
    public void clickonsortBy() {
        WebElement sortBy = sortBy();
        sortBy.click();
    }

    //Method to get price low to high
    public WebElement sortByprice() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class=\"wizzy-common-select-option \"][1]")
        ));
    }

    // Action method
    public void clickonsortByprice() {
        WebElement sortByprice = sortByprice();
        sortByprice.click();
    }

    // Method to get all non-discounted/original prices
    public List<WebElement> getitemOriginalPrice() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[@class='product-item-original-price']")
        ));
    }


    // Action method to extract and return numeric price list safely
    public List<Double> fetchOriginalPrices() {
        List<Double> prices = new ArrayList<>();

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                List<WebElement> priceElements = getitemOriginalPrice();
                prices.clear();

                for (WebElement priceEl : priceElements) {
                    String text = priceEl.getText().replaceAll("[^0-9.]", "");
                    if (!text.isEmpty()) {
                        prices.add(Double.parseDouble(text));
                    }
                }

                // If prices fetched successfully, break the retry loop
                break;

            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                System.out.println("⚠️ Attempt " + attempt + ": StaleElementReferenceException caught, retrying...");
                try {
                    Thread.sleep(1000); // small delay before retry
                } catch (InterruptedException ignored) {}
            }
        }

        return prices;
    }




    // Method to get names of books having discounted prices
    public List<String> getDiscountedBookNames() {
        List<String> discountedBooks = new ArrayList<>();
        List<WebElement> books = wd.findElements(
                By.xpath("//p[@class='product-item-title']")
        );
        for (WebElement book : books) {
            discountedBooks.add(book.getText().trim());
        }
        return discountedBooks;
    }

    // Method to Apply Price Filter (600-900)


    public WebElement sortByprice2() {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='wizzy-facet-head facet-head-sellingPrice']")
        ));
    }

    // Action method
    public void clickonsortByprice2() {
        WebElement sortByprice2 = sortByprice2();
        sortByprice2.click();
    }


    public void handlePriceSlider() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        Actions actions = new Actions(wd);

        By leftHandleLocator = By.xpath("//div[contains(@class,'noUi-handle-lower')]");
        By rightHandleLocator = By.xpath("//div[contains(@class,'noUi-handle-upper')]");

        int maxRetries = 3;  // in case of stale or failure

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Wait until both slider handles are clickable and refreshed
                WebElement leftHandle = wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.elementToBeClickable(leftHandleLocator)));
                WebElement rightHandle = wait.until(ExpectedConditions.refreshed(
                        ExpectedConditions.elementToBeClickable(rightHandleLocator)));

                // Log attempt
                System.out.println("🎯 Adjusting price slider (Attempt " + attempt + ")...");

                // Move left handle to right (to set ₹600)
                actions.clickAndHold(leftHandle)
                        .moveByOffset(47, 0)
                        .release()
                        .perform();
                Thread.sleep(1500);

                // Move right handle to left (to set ₹900)
                actions.clickAndHold(rightHandle)
                        .moveByOffset(-45, 0)
                        .release()
                        .perform();
                Thread.sleep(1500);

                System.out.println("✅ Price slider adjusted successfully (600 - 900 range)");
                break; // success, exit retry loop

            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ StaleElementReferenceException caught (Attempt " + attempt + "). Retrying...");
                Thread.sleep(1000);
            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ ElementClickInterceptedException caught (Attempt " + attempt + "). Retrying...");
                Thread.sleep(1000);
            } catch (MoveTargetOutOfBoundsException e) {
                System.out.println("⚠️ MoveTargetOutOfBoundsException caught (Attempt " + attempt + "). Check offsets.");
                Thread.sleep(1000);
            }
        }
    }

    public void handlePriceSlider2() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(10));
        Actions actions = new Actions(wd);

        By leftHandleLocator = By.xpath("//div[contains(@class,'noUi-handle-lower')]");
        By rightHandleLocator = By.xpath("//div[contains(@class,'noUi-handle-upper')]");

        // Wait until both slider handles are visible and clickable
        WebElement leftHandle = wait.until(ExpectedConditions.elementToBeClickable(leftHandleLocator));
        WebElement rightHandle = wait.until(ExpectedConditions.elementToBeClickable(rightHandleLocator));

        // Scroll into view (prevents MoveTargetOutOfBounds)
        ((JavascriptExecutor) wd).executeScript("arguments[0].scrollIntoView(true);", leftHandle);
        ((JavascriptExecutor) wd).executeScript("arguments[0].scrollIntoView(true);", rightHandle);

        System.out.println("🎯 Adjusting price slider...");

        // Move left handle to right (e.g., to set ₹600)
        actions.clickAndHold(leftHandle)
                .moveByOffset(47, 0)
                .release()
                .perform();
        Thread.sleep(1500);

        // Move right handle to left (e.g., to set ₹900)
        actions.clickAndHold(rightHandle)
                .moveByOffset(-45, 0)
                .release()
                .perform();
        Thread.sleep(1500);

        System.out.println("✅ Price slider adjusted successfully (600 - 900 range)");
    }


    public void getSliderLocation() throws InterruptedException {
        WebElement minSlider = wd.findElement(By.xpath("//div[@class=\"noUi-handle noUi-handle-lower\"]"));
        WebElement maxSlider = wd.findElement(By.xpath("//div[@class=\"noUi-handle noUi-handle-upper\"]"));

       // System.out.println("Initial location  -->>"+minSlider.getLocation());  //(144, 935)
        System.out.println("Initial location  -->>"+maxSlider.getLocation());  // (272, 935)
        Actions act = new Actions(wd);

       // act.dragAndDropBy(minSlider, 47, 0).perform();
        act.dragAndDropBy(maxSlider, -45, 0).perform();
        Thread.sleep(3000);
        //System.out.println("AfterMoving Position -->>"+minSlider.getLocation());
        System.out.println("AfterMoving Position -->>"+maxSlider.getLocation());

    }





}


//div[@class="noUi-handle noUi-handle-upper"]









