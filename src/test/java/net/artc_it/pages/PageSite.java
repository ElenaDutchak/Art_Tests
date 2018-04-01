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
        if (!langEnColor.equals("rgba(0, 27, 51, 0.5)"))
            return "Your message has been sent";
        else return "Сообщение отправлено";
    }

    /**
     * Этот метод возвращает текст веб-элемента, содержащего сообщение об успешной отправке формы, которое появляется
     * после нажатия на кнопку отправки. Если сообщение не отображается на экране - то вернёт пустую строку "".
     * @param isMustPresent формальный параметр, через который передаётся true - когда по логике сообщение должно быть
     *                     видимым на странице, fasle - когда сообщение должно отсутствовать на станице
     * @return "Сообщение отправлено" или ""
     */
    public String getTextResultMessage(boolean isMustPresent) {
        // если ожидаем сообщение - то константа state будет = "visible", чтоб ожидать появление видимости веб-элемента
        // иначе = "hidden"
        final String state = isMustPresent ? "visible" : "hidden";
        // если ожидаем сообщение - то константа textWaitOk будет = тексту веб-элемента, чтобы вернуть его как успещный результат
        // иначе = ""
        final String textWaitOk = isMustPresent ? resultMessage.getText() : "";
        // если ожидаем сообщение - то константа textWaitEr будет = "",  чтобы вернуть его как НЕ успещный результат
        // иначе = тексту веб-элемента
        final String textWaitEr = isMustPresent ? "" : resultMessage.getText();
        // пишем в лог текущее значение свойства overflow веб-элемента resultMessage
        LOGGER.debug("resultMessage.overflow: " + resultMessage.getCssValue("overflow"));
        try {
            // ожидаем (явно) когда свойство overflow веб-элемента resultMessage станет = значению константы state
            new WebDriverWait(driver, 2).until(Function -> {
                return resultMessage.getCssValue("overflow").equals(state);
            });
            return textWaitOk;
        } catch (TimeoutException e) {
            return textWaitEr;
        }
    }

/* Вместо getTextResultMessage(boolean isMustPresent) могли быть 2 процедуры без параметров:

    // это вариант процедуры вызвать если ожидаем увидеть сообщение
    public String getTextResultMessage() {
        LOGGER.debug("resultMessage.overflow: " + resultMessage.getCssValue("overflow"));
        try {
            // ожидаем (явно) когда свойство overflow веб-элемента resultMessage станет = "visible"
            new WebDriverWait(driver, 2).until(Function -> {
                return resultMessage.getCssValue("overflow").equals("visible");
            });
            return resultMessage.getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    // это вариант процедуры вызвать для провеки что сообщение не отображается
    public boolean isHideResultMessage() {
        LOGGER.debug("resultMessage.overflow: " + resultMessage.getCssValue("overflow"));
        try {
            // ожидаем (явно) когда свойство overflow веб-элемента resultMessage станет = "visible"
            return new WebDriverWait(driver, 2).until(Function -> {
                return resultMessage.getCssValue("overflow").equals("hidden");
            });
        } catch (TimeoutException e) {
            return false;
        }
    }
*/

    public void sendMessageForm(String name, String phone, String email, String message) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setMessage(message);
        clickSendButton();
    }

    /**
     * Этот метод возвращает текст ошибки валидации обязательного к заполнению веб-элемента ввода "name"
     * млм пустую строку, если валидация прошла успешно (когда строка ввода веб-элемента "name" была не пустой)
     * @return текст ошибки валидации или ""(когда нет ошибки)
     */
    public String getValidationMessage() {
        if (isInputValid(name))
            return "";
        else
            return name.getAttribute("validationMessage");
    }

    /**
     * Проверка валидации веб-элемента средствами DOM страницы (выполняется через JS)
     * @param element  формальный параметр, которому передаётся фактический веб-элемент страницы
     * @return true - если ошибки нет, false - если валидация не пройдена
     */
    private Boolean isInputValid(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript("return arguments[0].checkValidity()", element);
    }

    /**
     * Метод позволяет выдать в лог текущие атрибуты веб-элемента для отладки скрипта.
     * @param element формальный параметр, которому передаётся фактический веб-элемент страницы
     */
    private void LogDebugAboutElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        LOGGER.debug("executeScript: " +
                js.executeScript("var items = {}; " +
                                "for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;",
                        element));
    }
}


