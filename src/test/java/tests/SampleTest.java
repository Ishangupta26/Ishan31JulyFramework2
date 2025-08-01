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

    @Test(dataProvider = "platformProvider")
    public void testGoogleHomePage(Object platform) {
        // Create and set the driver per test thread
        WebDriver driver = DriverManager.createDriver(platform);
        DriverManager.setDriver(driver);

        try {
            driver.get("https://www.google.com");
            String title = driver.getTitle();
            System.out.println("Platform: " + platform.toString());
            System.out.println("Page Title: " + title);
            assert title.toLowerCase().contains("google");
        } finally {
            DriverManager.quitDriver(); // Ensure driver is closed even on failure
        }
    }
}
