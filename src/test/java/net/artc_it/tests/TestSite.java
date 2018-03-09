package net.artc_it.tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.*;

public class TestSite extends BaseTestSite {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSite.class);

    @Test
    public void testTimeForLoafPage() {
        Assert.assertTrue(page.getTimeLoadPage() < 30000,
                "Time for load page (" + page.getTimeLoadPage() / 1000 + " сек.) is too long!");
    }

    @Test
    public void testSendMessage() {
        page.sendMessageForm("tyty", "+380956432133", "gh@fg.com", "qwertyВАРП! +-*345 ПЫВАПап");
        // ожидаемый результат должен быть - 2-й параметр!
        Assert.assertEquals(page.getTextResultMessage(true), page.getExpectedResultMessage());
    }

    @Test
    public void testSendEmptyName() {
        page.sendMessageForm("", "+380664952327", "gh@fg.com", "аофыарфоллд.");
        String validationMessage = page.getValidationMessage();
        LOGGER.debug("validationMessage = " + validationMessage);
        Assert.assertFalse(validationMessage.isEmpty(), "No warning validation message about empty name!");
        Assert.assertTrue(page.getTextResultMessage(false).isEmpty(), "Mistaken message about sending is present!");
    }
}
//    File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//        try {
//                FileUtils.copyFile(src, new File("D:\\Screen01.png"));
//                } catch (IOException e) {
//                e.printStackTrace();
//                }