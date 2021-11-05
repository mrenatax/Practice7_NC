package pr5;

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
    public void add(Task task) throws NullPointerException {
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
    public void remove(Task task) throws NullPointerException, ArithmeticException {
        if (task == null) {
            throw new NullPointerException("Task object can't be null");
        } else if (size() == 0) {
            throw new ArithmeticException("The list is empty");
        } else {
            Node current = first.getNext();
            Node temp_current = null;

            while (current != null) {
                if (current.getTask().
                        equals(task)) {
                    if (temp_current == null) {
                        first = first.getNext();
                    } else {
                        temp_current.setNext(current.getNext());
                    }
                    size--;
                }
                temp_current = current;
                current = current.getNext();
            }
        }
    }

    /**
     * Method for obtaining a task at a given number
     *
     * @param index given number (index of element)
     * @return Task object
     */
    @Override
    public Task getTask(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("Invalid index");
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
            if (current.getTask().getTime() > from && current.getTask().getTime() <= to) {
                incoming[countSize] = current.getTask();
                countSize++;
            }
            current = current.getNext();
        }
        Task[] incomingFinal = new Task[countSize];
        System.arraycopy(incoming, 0, incomingFinal, 0, countSize);
        return incomingFinal;
    }
}