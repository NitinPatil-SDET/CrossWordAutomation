import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseClass {

    public WebDriver wd;
    public PageObject pj;
    public Properties prop;

    @BeforeClass
    public void setup() throws IOException {
        // Load configuration
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);

        String runOn = prop.getProperty("runOn");
        String browser = prop.getProperty("browser");
        String browserVersion = prop.getProperty("browserVersion");
        String platform = prop.getProperty("platform");
        String testUrl = prop.getProperty("testUrl");

        if (runOn.equalsIgnoreCase("local")) {
            // 🖥️ Local execution
            wd = new ChromeDriver();

        } else if (runOn.equalsIgnoreCase("browserstack")) {
            String username = prop.getProperty("browserstack.username");
            String accessKey = prop.getProperty("browserstack.key");

            // Base browser capability
            MutableCapabilities capabilities = new MutableCapabilities();
            capabilities.setCapability("browserName", prop.getProperty("browser"));
            capabilities.setCapability("browserVersion", prop.getProperty("browserVersion"));

            // W3C-compliant BrowserStack options
            Map<String, Object> bstackOptions = new HashMap<>();
            bstackOptions.put("os", "Windows");
            bstackOptions.put("osVersion", "11");
            bstackOptions.put("projectName", "Crossword Project");
            bstackOptions.put("buildName", "Build #1");
            bstackOptions.put("sessionName", "Crossword Test on BrowserStack");

            // Attach bstack:options to main capability
            capabilities.setCapability("bstack:options", bstackOptions);

            String hubURL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
            wd = new RemoteWebDriver(new URL(hubURL), capabilities);



    } else if (runOn.equalsIgnoreCase("lambdatest")) {
            // ☁️ LambdaTest setup
            String username = prop.getProperty("lambdatest.username");
            String accessKey = prop.getProperty("lambdatest.key");

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browserName", browser);
            caps.setCapability("browserVersion", browserVersion);
            caps.setCapability("platformName", platform);
            caps.setCapability("build", "Crossword Project Build");
            caps.setCapability("name", "Crossword Test on LambdaTest");

            String hubURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";
            wd = new RemoteWebDriver(new URL(hubURL), caps);
        }

        wd.get(testUrl);
        wd.manage().window().maximize();
        pj = new PageObject(wd); // pass driver to your page object
    }

    @AfterClass
    public void tearDown() {
        if (wd != null) {
            wd.quit();
        }
    }
}
