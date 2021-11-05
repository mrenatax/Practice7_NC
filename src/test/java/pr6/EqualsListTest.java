package pr6;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EqualsListTest extends AbstractTaskListTest {

    public EqualsListTest(Class<? extends AbstractTaskList> tasksClass) {
        super(tasksClass);
    }

    @Test
    public void testEquals() throws Exception {
        Task[] elements = {task("A"), task("B"), task("C")};

        AbstractTaskList list1 = createList();
        AbstractTaskList list2 = createList();
        for (Task task : elements) {
            list1.add(task);
            list2.add(task);
        }
        assertEquals(getTitle(), list1, list2);
        assertEquals(getTitle(), list2, list1);
    }

    @Test
    public void testEqualsNull() {
        assertFalse(getTitle(), tasks.equals(null));
    }

    @Test
    public void testIdentityEquals() {
        assertEquals(getTitle(), tasks, tasks);
    }

    @Test
    public void testSymmetricEquals() {
        AbstractTaskList dummy = new DummyList();
        assertEquals(getTitle(), tasks.equals(dummy), dummy.equals(tasks));
    }

    @Test
    public void testHashCode() throws Exception {
        Task[] elements = {task("A"), task("B"), task("C")};

        AbstractTaskList list1 = createList();
        AbstractTaskList list2 = createList();
        for (Task task : elements) {
            list1.add(task);
            list2.add(task);
        }

        assertEquals(getTitle(), list1.hashCode(), list2.hashCode());
    }
}

class DummyList extends AbstractTaskList {

    public void add(Task task) {
    }

    public void remove(Task task) {
    }

    public int size() {
        return 0;
    }

    public Task getTask(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Task[] incoming(int from, int to) {
        return new Task[0];
    }

    public Iterator<Task> iterator() {
        return Collections.<Task>emptyList().iterator();
    }

    public boolean equals(Object obj) {
        return false;
    }
}