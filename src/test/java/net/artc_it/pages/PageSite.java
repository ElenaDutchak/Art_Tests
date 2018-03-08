package net.artc_it.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class PageSite {

    final String SITE_URL = "http://artc-it.net";

    private static final Logger LOGGER = LoggerFactory.getLogger(PageSite.class);

    private long timeLoadPage;

    public long getTimeLoadPage() {
        return timeLoadPage;
    }

    public WebDriver driver;

    public PageSite(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

        long startTime = System.currentTimeMillis();
        driver.get(SITE_URL);
        timeLoadPage = System.currentTimeMillis() - startTime;
    }

    @FindBy(id = "name")
    private WebElement name;

    @FindBy(id = "tel")
    private WebElement phone;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "msg")
    private WebElement message;

    @FindBy(xpath = "id(\"contacts\")/div[2]/form[1]/input[1]")
    private WebElement sendButton;

    @FindBy(xpath = "id(\"contacts\")/div[2]/form[1]/span[1]")
    private WebElement resultMessage;

    @FindBy(xpath = "id(\"main-nav\")/div[1]/a[2]")
    private WebElement langEn;

    public void setName(String name) {
        this.name.clear();
        this.name.sendKeys(name);
    }

    public void setPhone(String phone) {
        this.phone.clear();
        this.phone.sendKeys(phone);
    }

    public void setEmail(String email) {
        this.email.clear();
        this.email.sendKeys(email);
    }

    public void setMessage(String message) {
        this.message.clear();
        this.message.sendKeys(message);
    }

    public void clickSendButton() {
        sendButton.click();
    }

    public String getExpectedResultMessage() {
        String langEnColor = langEn.getCssValue("color"); //rgba(0, 27, 51, 1) - active
        if (langEnColor.equals("rgba(0, 27, 51, 1)"))
            return "Your message has been sent";
        else return "Сообщение отправлено";
    }

    public String getTextResultMessage(boolean isMustPresent) {
        final String state = isMustPresent ? "visible" : "hidden";
        final String textWaitOk = isMustPresent ? resultMessage.getText() : "";
        final String textWaitEr = isMustPresent ? "" : resultMessage.getText();
        LOGGER.debug("resultMessage.overflow: " + resultMessage.getCssValue("overflow"));
        try {
            // wait until element change visible property
            new WebDriverWait(driver, 2).until(Function -> {
                return resultMessage.getCssValue("overflow").equals(state);
            });
            return textWaitOk;
        } catch (TimeoutException e) {
            return textWaitEr;
        }
    }

    public void sendMessageForm(String name, String phone, String email, String message) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setMessage(message);
        clickSendButton();
    }

    public String getValidationMessage() {
        if (isInputValid(name))
            return "";
        else
            return name.getAttribute("validationMessage");
    }

    private Boolean isInputValid(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript("return arguments[0].checkValidity()", element);
    }

    private void LogDebugAboutElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        LOGGER.debug("executeScript: " +
                js.executeScript("var items = {}; " +
                                "for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;",
                        element));
    }
}


