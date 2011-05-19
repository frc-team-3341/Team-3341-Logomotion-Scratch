package org.wvrobotics.logomotion.control;

/**
 *
 * @author Vineel
 */
public interface JoystickListener {
    public void joystickMoved(JoystickEvent e);
    public void throttleMoved(JoystickEvent e);
}
