package pr6;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class for linked list
 */
public class LinkedTaskList extends AbstractTaskList {
    private Node first;

    public LinkedTaskList() {
        first = new Node(null);
        size = 0;
    }

    /**
     * Class to set a link to the next element of LinkedList
     */
    private static class Node {
        Task task;
        Node next; //reference to the next Node

        Node(Task task) {
            this.task = task;
            this.next = null;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Task getTask() {
            return task;
        }
    }

    /**
     * Method for adding the specified element to the end of a LinkedList
     *
     * @param task Task object
     */
    @Override
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Task object can't be null");
        } else {
            Node temp = new Node(task);
            Node current = first;

            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(temp);
            size++;
        }
    }

    /**
     * Method for removing the element from a LinkedList
     *
     * @param task Task object
     */
    @Override
    public void remove(Task task) {
        int checkRemove = 0;
        if (task == null) {
            throw new NullPointerException("Task object can't be null");
        } else if (size() == 0) {
            System.err.println("The list is empty");
        } else {
            Node current = first.getNext();
            Node temp_current = null;

            while (current != null) {
                if (current.getTask().equals(task)) {
                    if (temp_current == null) {
                        first = first.getNext();
                    } else {
                        temp_current.setNext(current.getNext());
                    }
                    checkRemove++;
                    size--;
                }
                temp_current = current;
                current = current.getNext();
            }
            if (checkRemove == 0)
                throw new IllegalStateException("Object wasn't found");
        }
    }

    /**
     * Method for obtaining a task at a given number
     *
     * @param index given number (index of element)
     * @return Task object
     */
    @Override
    public Task getTask(int index) {
        if (index < 0 || index >= this.size) {
            throw new NoSuchElementException("Invalid index");
        } else {
            Node current = first.getNext();
            for (int i = 0; i < index; i++) {
                if (current.getNext() == null) {
                    return null;
                }
                current = current.getNext();
            }
            return current.getTask();
        }
    }

    /**
     * @param from notification time
     * @param to   notification time
     * @return an array of tasks from the list, the notification time of which
     * is between from (exclusively) and to (inclusive)
     */
    public Task[] incoming(int from, int to) {
        Task[] incoming = new Task[size()];
        Node current = first.getNext();
        int countSize = 0;
        while (current != null) {
            if (current.getTask().nextTimeAfter(from) <= to && current.getTask().nextTimeAfter(from) > from) {
                incoming[countSize] = current.getTask();
                countSize++;
            }
            current = current.getNext();
        }
        Task[] incomingFinal = new Task[countSize];
        System.arraycopy(incoming, 0, incomingFinal, 0, countSize);
        return incomingFinal;
    }

    @Override
    public AbstractTaskList clone() {
        Node current = first.getNext();
        LinkedTaskList clone = new LinkedTaskList();
        for (int i = 0; i < size; i++) {
            clone.add(current.getTask());
            current = current.getNext();
        }
        return clone;
    }

    @Override
    public Iterator<Task> iterator() {
        Iterator<Task> iterator = new Iterator<Task>() {
            private int index = 0;
            Task lastReturnedTask = null;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public Task next() {
                lastReturnedTask = getTask(index++);
                return lastReturnedTask;
            }

            @Override
            public void remove() {
                LinkedTaskList.this.remove(lastReturnedTask);
            }
        };
        return iterator;
    }
}