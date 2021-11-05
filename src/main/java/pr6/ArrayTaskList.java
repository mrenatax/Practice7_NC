package pr6;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class for storing a list of tasks in an array
 */
public class ArrayTaskList extends AbstractTaskList {
    private Task[] tasks; //The array buffer into which the elements of the ArrayTaskList are stored
    private int index; //the index of the newly added item
    private static final int DEFAULT_CAPACITY = 10; //default initial capacity

    public ArrayTaskList() {
        tasks = new Task[DEFAULT_CAPACITY];
    }

    /**
     * Method for adding non-unique tasks
     *
     * @param task Task object
     */
    @Override
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("An empty task can't be added");
        } else {
            if (index == tasks.length) {
                Task[] newTasks = new Task[tasks.length * 2];
                System.arraycopy(tasks, 0, newTasks, 0, tasks.length);
                tasks = newTasks;
            }
            tasks[index] = task;
            index++;
            size++;
        }
    }

    /**
     * Method to remove all tasks equal to input
     *
     * @param task Task object
     */
    @Override
    public void remove(Task task) {
        if (task == null) {
            throw new NullPointerException("Empty task can't be removed");
        } else {
            int taskIndex = indexOf(task);
            if (taskIndex >= 0) {
                System.arraycopy(tasks, taskIndex + 1, tasks, taskIndex, this.index - taskIndex);
                size--;
                this.index--;
            } else {
                throw new IndexOutOfBoundsException("Index of array can't be negative");
            }
        }
    }

    /**
     * Method for finding the index of Task object
     *
     * @param task Task object
     * @return the index of the object, or -1 if the object wasn't found
     */
    public int indexOf(Task task) {
        int result = -1;
        for (int i = 0; i < index; i++) {
            if (tasks[i].equals(task)) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            throw new IllegalStateException("Object wasn't found");
        }
        return result;
    }

    /**
     * Method for obtaining a task at a given number (from scratch)
     *
     * @param index given number
     * @return Task object
     */
    @Override
    public Task getTask(int index) {
        if (index < 0 || index >= this.size) {
            throw new NoSuchElementException("Invalid index");
        } else {
            return tasks[index];
        }
    }

    @Override
    public Iterator<Task> iterator() {
        Iterator<Task> iterator = new Iterator<Task>() {
            private int index_it = 0;
            Task lastReturnedTask = null;

            @Override
            public boolean hasNext() {
                return index_it < size();
            }

            @Override
            public Task next() {
                lastReturnedTask = getTask(index_it++);
                return lastReturnedTask;
            }

            @Override
            public void remove() {
                ArrayTaskList.this.remove(lastReturnedTask);
            }
        };
        return iterator;
    }

    /**
     * @param from notification time
     * @param to   notification time
     * @return an array of tasks from the list, the notification time of which
     * is between from (exclusively) and to (inclusive)
     */
    public Task[] incoming(int from, int to) {
        Task[] incoming = new Task[tasks.length];
        int countSize = 0;
        for (int i = 0; i < size(); i++) {
            if (tasks[i].nextTimeAfter(from) <= to && tasks[i].nextTimeAfter(from) > from) {
                incoming[countSize] = tasks[i];
                countSize++;
            }
        }
        Task[] incomingFinal = new Task[countSize];
        System.arraycopy(incoming, 0, incomingFinal, 0, countSize);
        return incomingFinal;
    }

    @Override
    public AbstractTaskList clone() {
        ArrayTaskList clone = (ArrayTaskList) super.clone();
        clone.tasks = Arrays.copyOf(tasks, size);
        clone.toString();
        return clone;
    }
}