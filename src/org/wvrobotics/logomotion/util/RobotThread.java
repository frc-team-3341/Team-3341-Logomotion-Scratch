package org.wvrobotics.logomotion.util;

/**
 * @author Vineel
 *
 * The WPILIBJ Thread class is outdated. This handles interrupts in a more
 * "elegant" way.
 */
public class RobotThread extends Thread {
    private volatile boolean interrupted = false;

    public void start() {
        interrupted = false;
        super.start();
    }

    public void interrupt() {
        interrupted = true;
    }

    public boolean interrupted() {
        return interrupted;
    }
}
