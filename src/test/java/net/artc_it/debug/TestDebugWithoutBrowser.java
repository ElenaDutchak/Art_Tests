package net.artc_it.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestDebugWithoutBrowser {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDebugWithoutBrowser.class);

    @Test
    public void testAssertionError() {
        Assert.assertTrue(false, "This is wrong test for debug only!");
    }

    @Test
    public void testAssertionSucces() {
        LOGGER.info("This is succes test for debug only!");
        Assert.assertTrue(true);
    }
}