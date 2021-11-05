package pr3;

/**
 * The Task class describes the data type "task" and contains information about the essence of the task,
 * its status (active / inactive), notification time
 * the time interval after which you need to repeat the notification about it
 */
public class Task {
    private String title;
    private int time;
    private int start;
    private int end;
    private int repeat;
    private boolean active;

    public Task(String title, int time) {
        if (checkTitle(title)) {
            this.title = title;
        }
        if (time < 0) {
            System.err.println("Time can't be negative");
        } else {
            this.time = time;
        }
        setActive(false);
    }

    public Task(String title, int start, int end, int repeat) {
        if (checkTitle(title)) {
            this.title = title;
        }
        if (checkTime(start, end, repeat)) {
            this.start = start;
            this.end = end;
            this.repeat = repeat;
        }
        setActive(false);
    }

    /**
     * Method for checking the title
     *
     * @param title of the task
     * @return true if title is ccrrect, false if title is null
     */
    public boolean checkTitle(String title) {
        if (title == null) {
            System.err.println("Title can't be null");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method for checking the start, end, repeat time
     *
     * @param start  start time of task notification
     * @param end    time to stop task alert
     * @param repeat time interval after which it is necessary to repeat the task notification
     * @return true if the start, end, repeat time is correct, false if not
     */
    public boolean checkTime(int start, int end, int repeat) {
        if (start < 0) {
            System.err.println("Invalid start time");
            return false;
        } else if (repeat <= 0) {
            System.err.println("Invalid repeat time");
            return false;
        } else if (end < start) {
            System.err.println("Invalid end time");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method for getting task title
     *
     * @return title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Task title setting method
     *
     * @param title of the task
     */
    public void setTitle(String title) {
        if (checkTitle(title)) {
            this.title = title;
        }
    }

    /**
     * Method for checking the status of the task (the task will be considered active if the notification about
     * it must occur at a given time, otherwise - we will consider the task inactive)
     *
     * @return true if active, false if not
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Method for setting task status
     *
     * @param active boolean indicator of task activity
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Method for setting the alert time for a one-time task
     *
     * @param time task alert time
     */
    public void setTime(int time) {
        if (time < 0) {
            System.err.println("Time can't be negative");
        } else {
            this.time = time;
            this.start = time;
        }
    }

    /**
     * Method for setting an alert for a recurring task
     *
     * @param start  start time of task notification
     * @param end    time to stop task alert
     * @param repeat time interval after which it is necessary to repeat the task notification
     */
    public void setTime(int start, int end, int repeat) {
        if (checkTime(start, end, repeat)) {
            this.time = 0;
            this.start = start;
            this.end = end;
            this.repeat = repeat;
        }
    }

    /**
     * @return the start time of the alert (for a recurring task) or the time
     * single notification (for a one-time task)
     */
    public int getTime() {
        return ((isRepeated()) ? start : time);
    }

    /**
     * @return the start time of the alert (for a recurring task), or
     * time of a single notification (for a one-time task)
     */
    public int getStartTime() {
        return ((isRepeated()) ? start : time);
    }

    /**
     * @return the end time of the alert (for a recurring task), or
     * time of a single notification (for a one-time task)
     */
    public int getEndTime() {
        return ((isRepeated()) ? end : time);
    }

    /**
     * @return time interval after which it is necessary to repeat
     * task notification (for a recurring task) or 0 (for a one-time
     * tasks)
     */
    public int getRepeatInterval() {
        return ((isRepeated()) ? repeat : 0);
    }

    /**
     * information about whether the task is repeated
     *
     * @return true if repeated, false if not
     */
    public boolean isRepeated() {
        return this.time == 0;
    }

    /**
     * A method that returns a description of the given task
     *
     * @return string with information
     */
    public String toString() {
        if (!isActive()) {
            return "Task " + "\"" + getTitle() + "\"" + " is inactive";
        } else if ((isActive()) && (!isRepeated())) {
            return "Task " + "\"" + getTitle() + "\"" + " at " + this.time;
        } else {
            return "Task " + "\"" + getTitle() + "\"" + " from " + this.start + " to "
                    + this.end + " every " + this.repeat + " seconds";
        }
    }

    /**
     * A method that returns the time of the next alert after the specified time (not including it).
     * If after the specified time there are no more notifications or the task is inactive, then the result should be -1
     *
     * @param time specified time
     * @return time of next alert
     */
    public int nextTimeAfter(int time) {
        if (isActive() && !isRepeated()) {
            if (this.time > time) {
                return this.time;
            } else {
                return -1;
            }
        } else if (isActive() && isRepeated()) {
            if (time < start) {
                return start;
            } else if ((time >= end) || (time + repeat >= end)) {
                return -1;
            } else {
                int nextRepeat = 0;
                int n = 1;
                while (nextRepeat <= time) {
                    nextRepeat = start + n * repeat;
                    n++;
                }
                return nextRepeat;
            }
        } else {
            return -1;
        }
    }
}
