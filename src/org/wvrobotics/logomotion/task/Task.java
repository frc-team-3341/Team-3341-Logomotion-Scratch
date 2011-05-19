/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wvrobotics.logomotion.task;

import org.wvrobotics.logomotion.Robot;

/**
 * @author Vineel
 *
 * This contains task information for slaves receiving tasks from the
 * TaskManager.
 */
public abstract class Task {
    public static final int NEW        = 0;
    public static final int PROCESSING = 1;
    public static final int COMPLETED  = 2;

    public volatile int status = NEW;

    /*
     * Blocking boolean indicates whether or not task allows other tasks to
     * execute in parallel. (True means no parallel tasks).
     */
    public volatile boolean blocking = false;

    public abstract String type();

    // make the tasking "blocking" - wait for it to finish
    public void finish() {
        while (status != COMPLETED) Robot.pause(10); // don't hog cpu
    }
}
