package pr5;

import org.junit.Before;

public class LinkedTaskListTest extends TaskListTest {
    @Before
    public void createTaskList() {
        tasks = new LinkedTaskList();
    }
}