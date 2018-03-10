package net.artc_it.tests;

import net.artc_it.pages.PageSite;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTestSite {

    private static final String PATH_SCREENSHOT = "./target/screenshots/";

    public static WebDriver driver;
    public static PageSite page;
    static String browser;
    private static int numOfTest;

    @BeforeSuite
    public static void OneSetupForAllTests() {
        // удалим старые скриншоты
        System.out.println("Delete " + PATH_SCREENSHOT);
        try {
            FileUtils.deleteDirectory(new File(PATH_SCREENSHOT));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeMethod
    public void handleTestMethodName(Method method) {
        System.out.println("Starting "+ ++numOfTest + " test (" + browser + "): " + method.getName());
    }

    @AfterMethod
    public void takeScreenshotWhenFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            captureScreenshot(result.getName() + "(" + browser + ").png");
        }
    }

    public File captureScreenshot(String fileName) {
        try {
            File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String path = PATH_SCREENSHOT + fileName;
            FileUtils.copyFile(source, new File(path));
            return source;
        }
        catch(IOException e) {
            System.out.println("Failed to capture screenshot (" + fileName + "): " + e.getMessage());
            return null;
        }
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
        else if(browser.equals("novisual")) {
            System.setProperty("phantomjs.binary.path", ".\\src\\test\\resources\\drivers\\phantomjs.exe");
            driver = new EventFiringWebDriver(new PhantomJSDriver());
        }
        else return;

        page = new PageSite(driver);

        System.out.println("title = " + driver.getTitle());
        if (!driver.getTitle().equals("Art Consulting")) {
            driver.quit();
            System.out.println("Title of site is wrong! Stoping testing!");
            System.exit(1);
        }
    }
    @AfterClass
    public static void tearDown() {
        driver.quit();
        numOfTest = 0;
    }
}

