package tests;

import config.ExecutionConfig;
import config.ExecutionConfig.BrowserStackPlatform;
import config.ExecutionConfig.LocalBrowser;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import utilities.ConfigReader;
import utilities.DriverManager;

import java.util.ArrayList;
import java.util.List;

public class SampleTest {

    private WebDriver driver;

    @DataProvider(name = "platformProvider", parallel = true)
    public Object[][] platformProvider() {
        ExecutionConfig config = new ConfigReader().getExecutionConfig();
        List<Object[]> data = new ArrayList<>();

        if (config.getLocal() != null && config.getLocal().getBrowsers() != null) {
            for (LocalBrowser browser : config.getLocal().getBrowsers()) {
                data.add(new Object[]{browser});
            }
        }

        if (config.getBrowserstack() != null && config.getBrowserstack().getPlatforms() != null) {
            for (BrowserStackPlatform platform : config.getBrowserstack().getPlatforms()) {
                data.add(new Object[]{platform});
            }
        }

        return data.toArray(new Object[0][0]);
    }

    @BeforeMethod
    public void setUp(Object[] testData) {
        Object platform = testData[0];
        driver = DriverManager.createDriver(platform);
    }

    @Test(dataProvider = "platformProvider")
    public void testGoogleHomePage(Object platform) {
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        System.out.println("Platform: " + platform.toString());
        System.out.println("Page Title: " + title);
        assert title.toLowerCase().contains("google");
    }

    /*@AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/
}
