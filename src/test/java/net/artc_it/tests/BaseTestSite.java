package net.artc_it.tests;

import net.artc_it.WebDriverLogger;
import net.artc_it.pages.PageSite;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

public class BaseTestSite {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSite.class);

    public static EventFiringWebDriver driver;
    public static WebDriverLogger webDriverLogger;
    public static PageSite page;
    static String browser;
    private static int numOfTest;

    @BeforeMethod
    public void handleTestMethodName(Method method) {
        LOGGER.info("Starting "+ ++numOfTest + " test (" + browser + "): " + method.getName());
    }

    @Parameters("browser")
    @BeforeClass
    public static void setup(String browser) {
        TestSite.browser = browser;
        if(browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", ".\\src\\test\\resources\\drivers\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--lang=ru");
            options.addArguments("--start-maximized");
            driver = new EventFiringWebDriver(new ChromeDriver(options));
        }
        else if(browser.equals("firefox")) {
            System.setProperty("webdriver.gecko.driver", ".\\src\\test\\resources\\drivers\\geckodriver.exe");
            driver = new EventFiringWebDriver(new FirefoxDriver());
            driver.manage().window().maximize();
        }
        else if(browser.equals("opera")) {
            System.setProperty("webdriver.opera.driver", ".\\src\\test\\resources\\drivers\\operadriver.exe");
            driver = new EventFiringWebDriver(new OperaDriver());
            driver.manage().window().maximize();
        }
        else if(browser.equals("novisual")) {
            System.setProperty("phantomjs.binary.path", ".\\src\\test\\resources\\drivers\\phantomjs.exe");
            driver = new EventFiringWebDriver(new PhantomJSDriver());
        }
        driver.register(webDriverLogger = new WebDriverLogger());

        page = new PageSite(driver);

        LOGGER.debug("title = " + driver.getTitle());
        if (!driver.getTitle().equals("Art Consulting")) {
            driver.quit();
            LOGGER.error("Title of site is wrong! Stoping testing!");
            System.exit(1);
        }
    }
    @AfterClass
    public static void tearDown() {
        driver.quit();
        numOfTest = 0;
    }
}

