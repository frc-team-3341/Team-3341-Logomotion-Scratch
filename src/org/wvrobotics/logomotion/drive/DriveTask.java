package org.wvrobotics.logomotion.drive;

import org.wvrobotics.logomotion.task.Task;

/**
 *
 * @author Vineel
 */
class DriveTask extends Task {
    public double speed;
    public double curve;
    public long time;

    public DriveTask(double speed, double curve, long time, boolean blocking) {
        this.speed = speed;
        this.curve = curve;
        this.time = time;
        this.blocking = blocking;
    }

    public String type() {
        return "Driver";
    }
}
