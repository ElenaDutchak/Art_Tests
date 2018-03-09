package net.artc_it.debug;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDebugWithoutBrowser {

    @Test
    public void testAssertionError() {
        Assert.assertTrue(false, "This is wrong test for debug only!");
    }

    @Test
    public void testAssertionSucces() {
        System.out.println("This is succes test for debug only!");
        Assert.assertTrue(true);
    }
}