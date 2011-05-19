package org.wvrobotics.logomotion.control;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.Joystick;
import java.util.Vector;
import org.wvrobotics.logomotion.Robot;
import org.wvrobotics.logomotion.util.RobotThread;

/**
 *
 * @author Vineel
 */
public class Controller extends RobotThread {
    private static final int TRIGGER = 1; // trigger is button #1

    private Joystick joystick;
    private int port;

    private volatile boolean[] buttons;
    private volatile double x;
    private volatile double y;
    private volatile double throttle;

    private volatile Vector buttonListeners;
    private volatile Vector joystickListeners;

    public Controller(int port) {
        this.port = port;
        joystick = new Joystick(port);

        buttons = new boolean[13];
        Arrays.fill(buttons, false);

        x = joystick.getX();
        y = joystick.getY();
        throttle = joystick.getThrottle();

        buttonListeners = new Vector();
        joystickListeners = new Vector();

        start();
    }

    public void addButtonListener(ButtonListener listener) {
        buttonListeners.addElement(listener);
    }

    public void removeButtonListener(ButtonListener listener) {
        buttonListeners.removeElement(listener);
    }

    public void addJoystickListener(JoystickListener listener) {
        joystickListeners.addElement(listener);
    }

    public void removeJoystickListener(JoystickListener listener) {
        joystickListeners.removeElement(listener);
    }

    public int getPort() {
        return port;
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public boolean getButton(int button) {
        return buttons[button - 1];
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

    public void run() {
        while (!interrupted()) {
            // handle buttons
            for (int i = 0; i < buttons.length; i++) {
                boolean state = joystick.getRawButton(i + 1);

                if (buttons[i] != state) {
                    ButtonEvent e = new ButtonEvent(this, i + 1, state);
                    if (state) { // button is pressed
                        triggerButtonPressed(e);
                    } else {
                        triggerButtonReleased(e);
                        triggerButtonTyped(e);
                    }
                    buttons[i] = state;
                }
            }

            // handle joystick and throttle
            double newX = joystick.getX();
            double newY = joystick.getY();
            double newThrottle = joystick.getThrottle();
            JoystickEvent e = new JoystickEvent(this, newX, newY, newThrottle);
            if ((x != newX) || (y != newY)) {
                triggerJoystickMoved(e);
                x = newX;
                y = newY;
            }
            if (throttle != newThrottle) {
                triggerThrottleMoved(e);
                throttle = newThrottle;
            }

            Robot.pause(10); // don't hog cpu
        }
    }

    private void triggerButtonPressed(ButtonEvent e) {
        for (int i = 0; i < buttonListeners.size(); i++) {
            ButtonListener listener =
                    (ButtonListener) buttonListeners.elementAt(i);
            listener.buttonPressed(e);
        }
    }

    private void triggerButtonReleased(ButtonEvent e) {
        for (int i = 0; i < buttonListeners.size(); i++) {
            ButtonListener listener =
                    (ButtonListener) buttonListeners.elementAt(i);
            listener.buttonReleased(e);
        }
    }

    private void triggerButtonTyped(ButtonEvent e) {
        for (int i = 0; i < buttonListeners.size(); i++) {
            ButtonListener listener =
                    (ButtonListener) buttonListeners.elementAt(i);
            listener.buttonTyped(e);
        }
    }

    private void triggerJoystickMoved(JoystickEvent e) {
        for (int i = 0; i < joystickListeners.size(); i++) {
            JoystickListener listener =
                    (JoystickListener) joystickListeners.elementAt(i);
            listener.joystickMoved(e);
        }
    }

    private void triggerThrottleMoved(JoystickEvent e) {
        for (int i = 0; i < joystickListeners.size(); i++) {
            JoystickListener listener =
                    (JoystickListener) joystickListeners.elementAt(i);
            listener.throttleMoved(e);
        }
    }
}
