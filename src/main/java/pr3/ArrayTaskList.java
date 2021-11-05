package pr3;

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
     * @param task
     */
    @Override
    public void add(Task task) {
        if (task == null) {
            System.err.println("An empty task can't be added");
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
     * @param task
     */
    @Override
    public void remove(Task task) {
        if (task == null) {
            System.out.println("Empty task can't be removed");
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
     * @param task
     * @return the index of the object, or -1 if the object wasn't found
     */
    public int indexOf(Task task) {
        int result = -1;
        for (int i = 0; i < index; i++) {
            if (tasks[i] == task) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Method for obtaining a task at a given number (from scratch)
     *
     * @param index
     * @return Task object
     */
    @Override
    public Task getTask(int index) {
        if (index < 0 || index > this.size) {
            throw new IllegalArgumentException("Invalid index");
        } else {
            return tasks[index];
        }
    }

    /**
     * @param from
     * @param to
     * @return an array of tasks from the list, the notification time of which
     * is between from (exclusively) and to (inclusive)
     */
    public Task[] incoming(int from, int to) {
        Task[] incoming = new Task[tasks.length];
        int i = 0;
        for (Task t : tasks) {
            if (t.getTime() > from && t.getTime() <= to) {
                incoming[i] = t;
                i++;
            }
        }
        return incoming;
    }
}
