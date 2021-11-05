package pr6;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class CloneTaskTest {

    @Test
    public void testClone() throws Exception {
        Task task = new Task("Title", 10);
        Task clone = (Task) task.clone();
        assertEquals("Clone must equal original", task, clone);
        assertNotSame("Clone must be different object", task, clone);
        clone.setTitle("Clone");
        assertEquals("Title", task.getTitle());
        assertEquals("Clone", clone.getTitle());
    }
}