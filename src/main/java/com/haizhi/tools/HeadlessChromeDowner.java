package com.haizhi.tools;


import com.google.common.collect.Maps;
import com.haizhi.base.HttpProxy;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.Network;
import io.webfolder.cdp.event.Events;
import io.webfolder.cdp.listener.EventListener;
import io.webfolder.cdp.logger.CdpLoggerType;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HeadlessChromeDowner {

    private final static Logger logger = LoggerFactory.getLogger(HeadlessChromeDowner.class);

    private Launcher launcher;

    private SessionFactory factory;

    private Session session;

    private int downloadTimeout;

    public HeadlessChromeDowner(int downloadTimeout, HttpProxy proxy) {
        this.downloadTimeout = downloadTimeout;
        newChrome(proxy);
    }

    private void newChrome(HttpProxy proxy) {
        factory = new SessionFactory(getFreePort(), CdpLoggerType.Null);
        launcher = new Launcher(factory);
        List<String> launcherList = new ArrayList<>();
        launcherList.add("--headless");
        launcherList.add("--disable-gpu");
        launcherList.add("--enable-javascript");
        launcherList.add("--proxy-server=" + proxy.getHost() + ":" + proxy.getPort());

        launcher.launch(launcherList);
        session = factory.create();

        Network network = session.getCommand().getNetwork();
        String nameAndPass = proxy.getUserName() + ":" + proxy.getPassWord();
        String encoding = new String(Base64.getEncoder().encode(nameAndPass.getBytes()));
        Map<String, Object> headers = Maps.newHashMap();
        headers.put("Proxy-Authorization", "Basic " + encoding);
        network.setExtraHTTPHeaders(headers);
        network.enable();

        clearCookie();
    }

    private static int getFreePort() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(0);// 读取空闲的可用端口
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != serverSocket) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class EventListenerImpl implements EventListener<Object> {
        CountDownLatch cdl;

        EventListenerImpl(CountDownLatch cdl) {
            this.cdl = cdl;
        }

        @Override
        public void onEvent(Events event, Object value) {
            if (Events.NetworkLoadingFinished.equals(event)) {
                cdl.countDown();
            }
        }

    }

    protected void insideDown(final String requestUrl) {
        final CountDownLatch cdl = new CountDownLatch(1);
        EventListener<Object> eventListener = new EventListenerImpl(cdl);
        session.addEventListener(eventListener);
        session.navigate(requestUrl);
        try {
            cdl.await(downloadTimeout, TimeUnit.MILLISECONDS);
            session.waitDocumentReady();
        } catch (Exception e) {
            logger.error("", e);
        }
        session.removeEventEventListener(eventListener);
        try {
            String html = (String) session.getProperty("//html", "outerHTML");
            String location = session.getLocation();
            logger.info(location);
            logger.info(session.getTitle());
            logger.info(html);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public ChromeDriver getChromeDriver() {
        throw new UnsupportedOperationException();
    }


    public void setValue(String selector, String value) {
        session.focus(selector).sendKeys(value);
        sleep(1000);
    }

    private void sleep(long time) {
        if (time > 0) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }
    }


    public void click(String selector, long waitTime) {
        session.click(selector);
        sleep(waitTime);
    }

    public void click(String selector) {
        session.click(selector);
        sleep(1000);
    }


    private void clearCookie() {
        session.clearCache();
        session.clearCookies();
    }

    private void insideColose() {
        if (null != session) {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
        if (null != factory) {
            try {
                factory.close();
            } catch (Exception e) {
            }
        }
//        if (null != launcher) {
//            launcher.close();
//        }
    }

    protected void doSetProxy(HttpProxy httpProxy) {
        insideColose();
        newChrome(httpProxy);
    }
}
