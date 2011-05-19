package org.wvrobotics.logomotion.task;

/**
 * @author Vineel
 *
 * This is an interface that allows an object to receive tasks from the
 * TaskManager.
 */
public interface Slave {
    public void execute(Task task);
}
