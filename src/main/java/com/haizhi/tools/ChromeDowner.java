package com.haizhi.tools;

import com.haizhi.base.HttpProxy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ChromeDowner {

    protected final static Logger logger = LoggerFactory.getLogger(ChromeDowner.class);

    private ChromeDriver browser;

    private int downloadTimeout;

    public ChromeDowner(int downloadTimeout, String workName, HttpProxy proxy) throws Exception {

        this.downloadTimeout = downloadTimeout;
        this.browser = newChromeDriver(workName, proxy);
    }

    private static String getChromeProxyExtension(String path, String workName, String proxy) throws IOException {
        String chromeProxyExtensionsDir = path + File.separator + "chrome-proxy-extensions";
        String usernameAndPwd = StringUtils.substringBefore(proxy, "@");
        String username = StringUtils.substringBefore(usernameAndPwd, ":");
        String password = StringUtils.substringAfter(usernameAndPwd, ":");
        String ipAndPort = StringUtils.substringAfter(proxy, "@");
        String ip = StringUtils.substringBefore(ipAndPort, ":");
        String port = StringUtils.substringAfter(ipAndPort, ":");
        File extensionFileDir = FileUtils.getFile(chromeProxyExtensionsDir);
        if (!extensionFileDir.exists()) {
            extensionFileDir.mkdirs();
        }
        String extensionFilePath = chromeProxyExtensionsDir + File.separator + proxy.replace(':', '_') + ".zip";
        File extensionFile = FileUtils.getFile(extensionFilePath);
        if (extensionFile.exists()) {
            return extensionFilePath;
        }
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(extensionFile));

        String manifestPath = path + File.separator + "manifest.json";
        String manifest = FileUtils.readFileToString(FileUtils.getFile(manifestPath));
        String backgroundContentPath = path + File.separator + "background.js";
        String backgroundContent = FileUtils.readFileToString(FileUtils.getFile(backgroundContentPath));
        backgroundContent = backgroundContent.replace("%proxy_host", ip);
        backgroundContent = backgroundContent.replace("%proxy_port", port);
        backgroundContent = backgroundContent.replace("%username", username);
        backgroundContent = backgroundContent.replace("%password", password);

        ZipEntry manifestZipEntry = new ZipEntry("manifest.json");
        zipOut.putNextEntry(manifestZipEntry);
        zipOut.write(manifest.getBytes());

        ZipEntry backgroundZipEntry = new ZipEntry("background.js");
        zipOut.putNextEntry(backgroundZipEntry);
        zipOut.write(backgroundContent.getBytes());
        zipOut.close();
        logger.info("插件路径: {}", extensionFilePath);
        return extensionFilePath;
    }

    private ChromeDriver newChromeDriver(String workName, HttpProxy httpProxy) throws Exception {
        logger.info("当前任务名: {}", workName);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        HashMap<String, Object> settings = new HashMap<>();

        settings.put("images", 2); // 设置不加载图片

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings", settings);
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "/Users/you/IdeaProjects/gsxt-crawler/src/main/resources/webdriver/chrome/chromedriver_mac");
        String chromePath = "/Users/you/IdeaProjects/gsxt-crawler/src/main/resources/webdriver/chrome";


        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        options.setExperimentalOption("prefs", prefs);
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        if (StringUtils.isNotBlank(httpProxy.getUserName())) {
            try {
                String proxyExtensionPath = getChromeProxyExtension("/tmp", workName, httpProxy.toStringUserAndpwd());
                //options.addExtensions(new File(proxyExtensionPath));
            } catch (IOException e) {
                logger.error("", e);
                throw new Exception("chrome set proxy username err");
            }
        } else {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(httpProxy.toString());
            cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
            cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
            cap.setCapability(CapabilityType.PROXY, proxy);
        }

        ChromeDriver browser = new ChromeDriver(cap);
        browser.manage().timeouts().pageLoadTimeout(downloadTimeout, TimeUnit.MILLISECONDS);
        browser.manage().deleteAllCookies();
        return browser;
    }

    public void insideDown(final String originUrl) throws Exception {
        try {
            browser.get(originUrl);

            String title = browser.getTitle();
            logger.info(title);
            logger.info(browser.getCurrentUrl());
            String content = browser.getPageSource();
            if (StringUtils.contains(content, "ERR_PROXY_CONNECTION_FAILED")
                    || StringUtils.contains(content, "代理服务器出现问题，或者地址有误")) {
                throw new Exception("use proxy io err");
            } else if (StringUtils.contains(content, "ERR_INTERNET_DISCONNECTED")
                    || StringUtils.contains(content, "未连接到互联网")) {
                throw new Exception("local network io err");
            }
            logger.info(content);

        } catch (Exception e) {
            logger.error("", e);
        }
    }


    public ChromeDriver getChromeDriver() {
        return browser;
    }

    private void sleep(long time) {
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void setValue(String selector, String value) {
        WebElement element = browser.findElementByCssSelector(selector);
        if (null != element) {
            element.sendKeys(value);
            sleep(1000);
        }
    }


    public void click(String selector, long waitTime) {
        WebElement element = browser.findElementByCssSelector(selector);
        if (null != element) {
            element.click();
            sleep(waitTime);
        }
    }


    public void click(String selector) {
        WebElement element = browser.findElementByCssSelector(selector);
        if (null != element) {
            element.click();
            sleep(1000);
        }
    }

    public void clearCookie() {
        browser.manage().deleteAllCookies();
    }

    protected void doSetProxy(String workName, HttpProxy httpProxy) throws Exception {
        browser.close();
        browser.quit();

        browser = newChromeDriver(workName, httpProxy);
    }

    public void insideColose() {
        browser.close();
        browser.quit();
    }
}
