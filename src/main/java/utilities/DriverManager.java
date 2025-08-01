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

    public static WebDriver createDriver(Object platformObj) {
        try {
            if (platformObj instanceof BrowserStackPlatform) {
                return createBrowserStackDriver((BrowserStackPlatform) platformObj);
            } else if (platformObj instanceof LocalBrowser) {
                return createLocalDriver((LocalBrowser) platformObj);
            } else {
                throw new IllegalArgumentException("Unsupported platform object: " + platformObj);
            }
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
            capabilities.setCapability("browserName", platform.getBrowserName());
            capabilities.setCapability("deviceName", platform.getDeviceName());
            capabilities.setCapability("osVersion", platform.getOsVersion());
            capabilities.setCapability("realMobile", true);
            if (platform.getDeviceOrientation() != null) {
                capabilities.setCapability("deviceOrientation", platform.getDeviceOrientation());
            }
        } else {
            // Desktop
            capabilities.setCapability("browserName", platform.getBrowserName());
            if (platform.getDeviceOrientation() != null) {
                capabilities.setCapability("browserVersion", platform.getBrowserVersion());
            }
            bstackOptions.put("os", platform.getOs());
            bstackOptions.put("osVersion", platform.getOsVersion());
        }

        capabilities.setCapability("bstack:options", bstackOptions);

        System.out.println("Launching BrowserStack with capabilities: " + capabilities);
        return new RemoteWebDriver(
                new URL("https://" + config.getUserName() + ":" + config.getAccessKey() + "@hub.browserstack.com/wd/hub"),
                capabilities
        );
    }
}