package org.wvrobotics.logomotion.task;

import java.util.Hashtable;
import org.wvrobotics.logomotion.Robot;
import org.wvrobotics.logomotion.util.RobotThread;

/**
 * @author Vineel
 *
 * This class provides a sort of "Task Manager" that allows for queuing of tasks
 * that an object (such as the arm or driver) must execute.
 */
public class TaskManager extends RobotThread implements Slave {
    private static volatile TaskManager instance;

    // there's no LinkedList type, so a queue can be improvised with a vector
    private volatile TaskList queue;

    // list of slaves to callback
    private Hashtable slaves;

    private TaskManager() {
        queue = new TaskList();
        slaves = new Hashtable();

        addSlave("Pause", this); // TaskManager will handle Pause tasks

        start();
    }

    public static TaskManager getInstance() {
        if (instance == null) instance = new TaskManager();
        return instance;
    }

    public void addTask(Task task) {
        queue.add(task);
    }

    public void reset() {
        queue.clear();
    }

    public void addSlave(String name, Slave slave) {
        slaves.put(name, slave);
    }

    public static void pause(long time) {
        getInstance().addTask(new PauseTask(time));
    }

    public void execute(Task task) {
        PauseTask pause = (PauseTask) task;
        task.status = Task.PROCESSING;
        Robot.pause(pause.time);
        task.status = Task.COMPLETED;
    }

    public void run() {
        // continue until thread.interrupt() is called
        while (!interrupted()) {
            if (queue.hasTasks()) {
                Task task = queue.pop();
                Slave slave = (Slave) slaves.get(task.type());
                if (slave == null) continue; // go to next task
                slave.execute(task);

                // wait for task to finish before attempting next one
                if (task.blocking) task.finish();
            } else {
                Robot.pause(10); // don't hog cpu
            }
        }
    }
}
