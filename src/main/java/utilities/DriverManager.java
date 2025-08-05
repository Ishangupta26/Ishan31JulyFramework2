package utilities;

import config.ExecutionConfig;
import config.ExecutionConfig.BrowserStackPlatform;
import config.ExecutionConfig.LocalBrowser;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver drv) {
        driver.set(drv);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static WebDriver createDriver(Object platformObj) {
        try {
            WebDriver createdDriver;

            if (platformObj instanceof BrowserStackPlatform) {
                createdDriver = createBrowserStackDriver((BrowserStackPlatform) platformObj);
                ThreadLogUtil.log("Creating WebDriver for platform: " + platformObj);


            } else if (platformObj instanceof LocalBrowser) {
                createdDriver = createLocalDriver((LocalBrowser) platformObj);
                ThreadLogUtil.log("Creating WebDriver for platform: " + platformObj);

            } else {
                throw new IllegalArgumentException("Unsupported platform object: " + platformObj);
            }

            return createdDriver;

        } catch (Exception e) {
            throw new RuntimeException("Driver creation failed: " + e.getMessage(), e);
        }
    }

    // ===== Local Browsers =====
    private static WebDriver createLocalDriver(LocalBrowser browser) {
        String browserName = browser.getBrowserName().toLowerCase();

        switch (browserName) {
            case "chrome":
                return new ChromeDriver(new ChromeOptions());
            case "firefox":
                return new FirefoxDriver(new FirefoxOptions());
            case "edge":
                return new EdgeDriver(new EdgeOptions());
            default:
                throw new RuntimeException("Unsupported local browser: " + browserName);
        }
    }

    // ===== BrowserStack =====
    private static WebDriver createBrowserStackDriver(BrowserStackPlatform platform) throws Exception {
        ExecutionConfig config = new ConfigReader().getExecutionConfig();

        MutableCapabilities capabilities = new MutableCapabilities();
        Map<String, Object> bstackOptions = new HashMap<>();

        bstackOptions.put("userName", config.getUserName());
        bstackOptions.put("accessKey", config.getAccessKey());
        bstackOptions.put("projectName", config.getBrowserstack().getProjectName());
        bstackOptions.put("buildName", config.getBrowserstack().getBuildName());
        bstackOptions.put("sessionName", "BrowserStack Session");
        bstackOptions.put("local", config.getBrowserstack().isBrowserstackLocal());

        // Mobile
        if (platform.getDeviceName() != null) {
            bstackOptions.put("deviceName", platform.getDeviceName());
            bstackOptions.put("osVersion", platform.getOsVersion());
            bstackOptions.put("realMobile", true);
            if (platform.getDeviceOrientation() != null) {
                bstackOptions.put("deviceOrientation", platform.getDeviceOrientation());
            }
            capabilities.setCapability("browserName", platform.getBrowserName());
        } else {
            // Desktop
            capabilities.setCapability("browserName", platform.getBrowserName());
            capabilities.setCapability("browserVersion", platform.getBrowserVersion());
            bstackOptions.put("os", platform.getOs());
            bstackOptions.put("osVersion", platform.getOsVersion());
        }

        capabilities.setCapability("bstack:options", bstackOptions);

        System.out.println("=== RAW CAPABILITIES ===");
        System.out.println("Capabilites as Map:" + capabilities.asMap());
        System.out.println("========================");

        return new RemoteWebDriver(
                new URL("https://" + config.getUserName() + ":" + config.getAccessKey() + "@hub.browserstack.com/wd/hub"),
                capabilities
        );
    }
}
