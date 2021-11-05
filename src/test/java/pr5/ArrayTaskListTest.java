package pr5;

import org.junit.Before;

public class ArrayTaskListTest extends TaskListTest {
    @Before
    public void createTaskList() {
        tasks = new ArrayTaskList();
    }
}