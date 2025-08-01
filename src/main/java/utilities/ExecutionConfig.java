package config;

import java.util.List;

public class ExecutionConfig {

    private String userName;
    private String accessKey;
    private String framework;
    private BrowserStackConfig browserstack;
    private LocalConfig local;

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getFramework() {
        return framework;
    }

    public BrowserStackConfig getBrowserstack() {
        return browserstack;
    }

    public LocalConfig getLocal() {
        return local;
    }

    // ===============================
    // BrowserStack Section
    // ===============================
    public static class BrowserStackConfig {
        private List<BrowserStackPlatform> platforms;
        private boolean browserstackLocal;
        private String buildName;
        private String projectName;

        public List<BrowserStackPlatform> getPlatforms() {
            return platforms;
        }

        public boolean isBrowserstackLocal() {
            return browserstackLocal;
        }

        public String getBuildName() {
            return buildName;
        }

        public String getProjectName() {
            return projectName;
        }
    }

    public static class BrowserStackPlatform {
        // Desktop-related fields
        private String os;
        private String osVersion;
        private String browserName;
        private String browserVersion;

        // Mobile-related fields
        private String deviceName;
        private String deviceOrientation;

        // Getters
        public String getOs() {
            return os;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public String getBrowserName() {
            return browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public String getDeviceOrientation() {
            return deviceOrientation;
        }

        @Override
        public String toString() {
            return "BrowserStackPlatform{" +
                    "os='" + os + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    ", browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", deviceOrientation='" + deviceOrientation + '\'' +
                    '}';
        }
    }

    // ===============================
    // Local Section
    // ===============================
    public static class LocalConfig {
        private List<LocalBrowser> browsers;

        public List<LocalBrowser> getBrowsers() {
            return browsers;
        }
    }

    public static class LocalBrowser {
        private String browserName;
        private String browserVersion;

        public String getBrowserName() {
            return browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        @Override
        public String toString() {
            return "LocalBrowser{" +
                    "browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    '}';
        }
    }
}
