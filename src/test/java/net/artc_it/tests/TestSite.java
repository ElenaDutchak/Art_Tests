package net.artc_it.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestSite extends BaseTestSite {

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
        System.out.println("validationMessage = " + validationMessage);
        Assert.assertFalse(validationMessage.isEmpty(), "No warning validation message about empty name!");
        Assert.assertTrue(page.getTextResultMessage(false).isEmpty(), "Mistaken message about sending is present!");
    }
}