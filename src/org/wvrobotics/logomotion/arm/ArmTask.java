package org.wvrobotics.logomotion.arm;

import org.wvrobotics.logomotion.task.Task;

/**
 *
 * @author Vineel
 */
class ArmTask extends Task {
    public double speed;
    public long time;

    public ArmTask(double speed, long time, boolean blocking) {
        this.speed = speed;
        this.time = time;
        this.blocking = blocking;
    }

    public String type() {
        return "Arm";
    }
}
