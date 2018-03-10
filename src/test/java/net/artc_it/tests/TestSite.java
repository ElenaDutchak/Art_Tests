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
        Assert.assertEquals(page.getRealResultMessage(), page.getExpectedResultMessage());
    }

    @Test
    public void testSendEmptyName() {
        page.sendMessageForm("", "+380664952327", "gh@fg.com", "аофыарфоллд.");

        String validationMessage = page.getValidationMessage();
        System.out.println("validationMessage = " + validationMessage);
        Assert.assertFalse(validationMessage.isEmpty(), "No warning validation message about empty name!");

        Assert.assertTrue(page.isHideResultMessage(), "Mistaken message about sending is present!");
    }
}