package tests;

import config.ExecutionConfig.BrowserStackPlatform;
import config.ExecutionConfig.LocalBrowser;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.ConfigReader;
import utilities.DriverManager;
import utilities.ThreadLogUtil;

import java.util.ArrayList;
import java.util.List;

public class SampleTest {

    @DataProvider(name = "platformProvider", parallel = true)
    public Object[][] platformProvider() {
        config.ExecutionConfig config = new ConfigReader().getExecutionConfig();
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
        WebDriver driver = DriverManager.createDriver(platform);
        DriverManager.setDriver(driver);

        try {
            ThreadLogUtil.log("Thread ID: " + Thread.currentThread().getId() + " - Platform: " + platform.toString());

            driver.get("https://www.google.com");
            String title = driver.getTitle();

            ThreadLogUtil.log("Page Title: " + title);

            assert title.toLowerCase().contains("google");

        } finally {
            DriverManager.quitDriver();
            ThreadLogUtil.printAndClear();  // Print grouped logs here after test ends
        }
    }
}
