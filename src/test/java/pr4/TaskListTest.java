package pr4;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public class TaskListTest {

    protected AbstractTaskList tasks;

    private static void assertEqualTasks(Task[] tasks, AbstractTaskList list) {
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
        assertEquals(0, tasks.size());
        Task[] ts = {new Task("a", 0), new Task("b", 1), new Task("c", 2)};
        for (Task t : ts)
            tasks.add(t);
        assertEqualTasks(ts, tasks);
    }

    @Test
    public void testRemove() {
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
        Task[] ts = new Task[100];
        for (int i = 0; i < 100; i++) {
            ts[i] = new Task("Task#" + i, i);
            tasks.add(ts[i]);
        }
        assertEqualTasks(ts, tasks);
    }

    @Test
    public void testTaskIncoming() {
        assertEquals(0, tasks.size());
        Task task1 = new Task("a", 5);
        Task task2 = new Task("b", 10);
        Task task3 = new Task("c", 15);
        Task[] ts = {task1, task2, task3};
        for (Task t : ts)
            tasks.add(t);
        Task[] testIncoming1 = {task2, task3};
        Task[] testIncoming2 = {task2};
        Task[] testIncoming3 = {};
        assertArrayEquals(ts, tasks.incoming(0, 15));
        assertArrayEquals(testIncoming1, tasks.incoming(5, 15));
        assertArrayEquals(testIncoming2, tasks.incoming(9, 14));
        assertArrayEquals(testIncoming2, tasks.incoming(9, 14));
        assertArrayEquals(testIncoming3, tasks.incoming(50, 140));

    }
}