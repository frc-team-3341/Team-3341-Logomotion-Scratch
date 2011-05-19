package org.wvrobotics.logomotion.control;

/**
 *
 * @author Vineel
 */
public class JoystickEvent {
    private Controller source;
    private double x;
    private double y;
    private double throttle;
    private long time;

    public JoystickEvent(Controller source, double x, double y, double throttle) {
        this.source = source;
        this.x = x;
        this.y = y;
        this.throttle = throttle;
        time = System.currentTimeMillis();
    }

    public Controller getSource() {
        return source;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getThrottle() {
        return throttle;
    }

    public long getTime() {
        return time;
    }
}
