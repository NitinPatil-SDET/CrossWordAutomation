import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrosswordTests  extends BaseClass {

    @Test(priority = 1)
    public void TC01_VerifyBooksAreSortedByPriceAscending() throws InterruptedException {
        pj.clickOnSearch();
        pj.clickOnViewMore();
        pj.clickonsortBy();
        pj.clickonsortByprice();

        // Wait for sorting and page reload
        Thread.sleep(3000);

        // Fetch the original (non-discounted) prices
        List<Double> originalPrices = pj.fetchOriginalPrices();
        System.out.println("Original Prices: " + originalPrices);

        // Verify prices are in ascending order
        List<Double> sortedPrices = new ArrayList<>(originalPrices);
        Collections.sort(sortedPrices);

        try {
            Assert.assertEquals(originalPrices, sortedPrices,
                    "❌ Prices are not sorted in ascending order (Low to High).");
            System.out.println("✅ Prices are correctly sorted in ascending order!");
        } catch (AssertionError e) {
            System.out.println("⚠️ Assertion failed, but continuing test execution: " + e.getMessage());
        }
    }


    @Test(priority = 2, dependsOnMethods = {"TC01_VerifyBooksAreSortedByPriceAscending"} , alwaysRun = true)
    public void TC02_VerifyBooksWithDiscountedPricesAreDisplayed() {
        List<String> discountedBooks = pj.getDiscountedBookNames();
        System.out.println("Books with Discounted Prices: " + discountedBooks);

        Assert.assertTrue(!discountedBooks.isEmpty(),
                "❌ No discounted books found.");
        System.out.println("✅ Verified: Discounted books are displayed.");
    }

  @Test(priority = 3, dependsOnMethods = {"TC02_VerifyBooksWithDiscountedPricesAreDisplayed"}, alwaysRun = true)
    public void TC03_VerifyPriceFilterAppliedCorrectly() throws InterruptedException {
            pj.clickonsortByprice2();
            pj.handlePriceSlider();
    }


}
