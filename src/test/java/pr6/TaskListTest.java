package pr6;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TaskListTest extends AbstractTaskListTest {

    public TaskListTest(Class<? extends AbstractTaskList> tasksClass) {
        super(tasksClass);
    }

    // tests --------------------------------------------------------------

    @Test
    public void testSizeAddGet() {
        assertEquals(getTitle(), 0, tasks.size());
        Task[] ts = {task("A"), task("B"), task("C")};
        addAll(ts);
        assertEquals(getTitle(), ts.length, tasks.size());
        assertContains(ts);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongAdd() {
        tasks.add(null);
    }

    @Test
    public void testRemove() {
        Task[] ts = {task("A"), task("B"), task("C"), task("D"), task("E")};
        addAll(ts);

        // remove first
        tasks.remove(task("A"));
        assertContains(new Task[]{ts[1], ts[2], ts[3], ts[4]});

        // remove last
        tasks.remove(task("E"));
        assertContains(new Task[]{ts[1], ts[2], ts[3]});

        // remove middle
        tasks.remove(task("C"));
        assertContains(new Task[]{ts[1], ts[3]});
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRemove() {
        tasks.remove(null);
    }

    @Test
    public void testAddManyTasks() {
        Task[] ts = new Task[1000];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Task("Task#" + i, i);
            tasks.add(ts[i]);
        }
        assertContains(ts);
    }

    @Test
    public void testIncomingInactive() {
        Task[] ts = {task("A", 0, false), task("B", 1, false), task("C", 2, false)};
        addAll(ts);
        assertArrayEquals(getTitle(), new Task[0], tasks.incoming(0, 1000));
    }

    @Test
    public void testIncoming() {
        // range: 50 60
        Task[] ts = {
                task("Simple IN", 55, true),
                task("Simple OUT", 10, true),
                task("Inactive OUT", 0, 1000, 1, false),
                task("Simple bound OUT", 50, true),
                task("Simple bound IN", 60, true),
                task("Repeat inside IN", 51, 58, 2, true),
                task("Repeat outside IN", 0, 100, 5, true),
                task("Repeat outside OUT", 0, 100, 22, true),
                task("Repeat left OUT", 0, 40, 1, true),
                task("Repeat right OUT", 65, 100, 1, true),
                task("Repeat left intersect IN", 0, 55, 13, true),
                task("Repeat left intersect IN", 0, 60, 30, true),
                task("Repeat left intersect OUT", 0, 55, 22, true),
                task("Repeat right intersect IN", 55, 100, 20, true)
        };
        addAll(ts);
        List<Task> incoming = new ArrayList<Task>();
        for (Task t : ts)
            if (t.getTitle().contains("IN"))
                incoming.add(t);

        assertContains(incoming.toArray(new Task[0]), tasks.incoming(50, 60));
    }
}