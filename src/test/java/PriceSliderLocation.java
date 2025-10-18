import org.testng.annotations.Test;

public class PriceSliderLocation extends BaseClass {

    @Test
    void pricesliderlocation() throws InterruptedException {
        pj.clickOnSearch();
        pj.clickOnViewMore();
        pj.clickonsortByprice2();
        Thread.sleep(5000);
        pj.getSliderLocation();

    }

}
