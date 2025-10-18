import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseClass {


    WebDriver wd;
    PageObject pj;

    @BeforeClass
    public void setup() {
        wd = new ChromeDriver();
        wd.get("https://www.crossword.in/");
        wd.manage().window().maximize();
        pj = new PageObject(wd); // pass driver to page object
    }

    @AfterClass
    public void tearDown() {
        wd.quit();
    }
}
