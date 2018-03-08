/*
TRACE   Трассировка всех сообщений в указанный аппендер
DEBUG   Определяет подробно детализированные информационные события, которые наиболее полезны для отладки прикладной программы.
INFO    Определяет информационные сообщения, характеризующие ход работы прикладной программы на уровне крупной детализации.
WARN    Предупреждение в программе что-то не так
ERROR   В в программе произошла ошибка
 */
package net.artc_it;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverLogger extends AbstractWebDriverEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverLogger.class);

    private String getElementDescription(WebElement element) {
        String description = "tag=" + element.getTagName();
        if (element.getAttribute("id") != null && !element.getAttribute("id").isEmpty()) {
            description += " id=" + element.getAttribute("id");
        }
        if (element.getAttribute("name") != null && !element.getAttribute("name").isEmpty()) {
            description += " name=" + element.getAttribute("name");
        }
        if (element.getText() != null && !element.getText().isEmpty()) {
            description += " ('" + element.getText() + "')";
        }

        return description;
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        LOGGER.trace("WebDriver navigated to '" + url + "'");
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        LOGGER.trace("WebDriver click on element " + getElementDescription(element));
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER.trace("WebDriver changed value for element " + getElementDescription(element));
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.trace("WebDriver find by for element " + by);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        LOGGER.trace("WebDriver start Script " + script);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        LOGGER.trace("WebDriver stop script " + script);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        LOGGER.trace("WebDriver exception " + throwable.getMessage());
    }}
