package net.artc_it.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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