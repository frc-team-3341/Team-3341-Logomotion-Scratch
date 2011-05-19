package org.wvrobotics.logomotion.task;

import java.util.Vector;

/**
 * @author Vineel
 *
 * This class exists as a way for an object (such as an arm, or the driver) to
 * keep track of its tasks.
 */
public class TaskList {
    // a LinkedList type isn't available, so a Vector is used instead
    private volatile Vector queue;

    public TaskList() {
        queue = new Vector();
    }

    public void add(Task task) {
        queue.addElement(task);
    }

    public Task peek() {
        return (Task) queue.firstElement();
    }

    public Task pop() {
        Task task = (Task) queue.firstElement();
        queue.removeElementAt(0);
        return task;
    }

    public boolean hasTasks() {
        return !queue.isEmpty();
    }

    public void clear() {
        queue.removeAllElements();
    }
}
