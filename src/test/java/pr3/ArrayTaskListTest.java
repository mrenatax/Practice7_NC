package pr3;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public class ArrayTaskListTest {

    private static void assertEqualTasks(Task[] tasks, ArrayTaskList list) {
        Task[] actual = new Task[list.size()];
        for (int i = 0; i < list.size(); i++)
            actual[i] = list.getTask(i);
        assertArrayEquals(tasks, actual);
    }

    private static void assertContains(Task[] expected, Task[] actual) {
        List<Task> expectedList = new ArrayList<Task>(Arrays.asList(expected));
        for (Task task : actual)
            if (expectedList.contains(task))
                expectedList.remove(task);
            else
                fail("Expected elements: " + Arrays.toString(expected) +
                        ", actual elements: " + Arrays.toString(actual));
        if (!expectedList.isEmpty())
            fail("Expected elements: " + Arrays.toString(expected) +
                    ", actual elements: " + Arrays.toString(actual));
    }

    @Test
    public void testAdd() {
        ArrayTaskList tasks = new ArrayTaskList();
        assertEquals(0, tasks.size());
        Task[] ts = {new Task("a", 0), new Task("b", 1), new Task("c", 2)};
        for (Task t : ts)
            tasks.add(t);
        assertEqualTasks(ts, tasks);
    }

    @Test
    public void testRemove() {
        ArrayTaskList tasks = new ArrayTaskList();
        Task[] ts = {new Task("a", 0), new Task("b", 1), new Task("c", 2)};
        tasks.add(ts[0]);
        Task t = new Task("some", 0);
        tasks.add(t);
        tasks.add(ts[1]);
        tasks.add(ts[2]);
        tasks.remove(t);
        assertEqualTasks(ts, tasks);
    }


    @Test
    public void testAddMoreTasks() {
        ArrayTaskList tasks = new ArrayTaskList();
        Task[] ts = new Task[100];
        for (int i = 0; i < 100; i++) {
            ts[i] = new Task("Task#" + i, i);
            tasks.add(ts[i]);
        }
        assertEqualTasks(ts, tasks);
    }
}