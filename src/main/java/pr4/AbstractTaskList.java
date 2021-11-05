package pr4;

public abstract class AbstractTaskList {

    public int size;

    /**
     * Method for adding non-unique tasks
     *
     * @param task
     */
    public abstract void add(Task task);

    /**
     * Method to remove all tasks equal to input
     *
     * @param task
     */
    public abstract void remove(Task task);

    /**
     * @return the number of tasks in the current list
     */
    public int size() {
        return size;
    }

    /**
     * Method for obtaining a task at a given number (from scratch)
     *
     * @param index
     * @return Task object
     */
    public abstract Task getTask(int index);

    /**
     * @param from notification time
     * @param to   notification time
     * @return an array of tasks from the list, the notification time of which
     * is between from (exclusively) and to (inclusive)
     */
    public abstract Task[] incoming(int from, int to);
}
