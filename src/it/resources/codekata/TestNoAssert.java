package codekata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestNoAssert {

    @Test
    public void testNoAssert() {

    }

    @Test
    public void testOkAssert() {
        assertEquals(2, 1 + 1);
    }
}
