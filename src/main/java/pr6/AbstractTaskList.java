package pr6;

import java.util.Iterator;
import java.util.Objects;

public abstract class AbstractTaskList implements Cloneable, Iterable<Task> {

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

    /**
     * @return clone of AbstractTaskList
     */
    @Override
    public AbstractTaskList clone() {
        try {
            AbstractTaskList list = (AbstractTaskList) super.clone();
            return list;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public abstract Iterator<Task> iterator();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTaskList list = (AbstractTaskList) o;
        return size == list.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }

    /**
     * @return a list of tasks in the format “TaskListType [Task1, Task2]"
     */
    @Override
    public String toString() {
        String listToString = this.getClass().getSimpleName() + " [";
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                listToString += getTask(i).getTitle() + ", ";
            } else {
                listToString += getTask(i).getTitle() + "]";
            }
        }
        return listToString;
    }
}
