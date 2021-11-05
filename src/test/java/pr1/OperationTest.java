package pr1;

import org.junit.*;

public class OperationTest {
    @Test
    public void testMultiply() {
        for (int a = 0; a < 100; a++)
            for (int b = 0; b < 100; b++)
                Assert.assertEquals(a * b, new Operation(a, b).getResult());
    }
} 