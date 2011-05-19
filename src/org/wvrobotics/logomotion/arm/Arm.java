package org.wvrobotics.logomotion.arm;

import edu.wpi.first.wpilibj.DigitalInput;
import org.wvrobotics.logomotion.Robot;
import org.wvrobotics.logomotion.control.Controller;
import org.wvrobotics.logomotion.control.ControllerManager;
import org.wvrobotics.logomotion.control.JoystickEvent;
import org.wvrobotics.logomotion.control.JoystickListener;
import org.wvrobotics.logomotion.motor.Motor;
import org.wvrobotics.logomotion.task.Slave;
import org.wvrobotics.logomotion.task.Task;
import org.wvrobotics.logomotion.task.TaskList;
import org.wvrobotics.logomotion.util.RobotThread;

/**
 * @author Vineel
 *
 * This class represents the Arm subsystem. It handles autonomous and teleop
 * arm movements.
 */
public class Arm extends RobotThread implements JoystickListener, Slave {
    private static volatile Arm instance;
    private volatile TaskList queue;

    private Motor motor;

    private DigitalInput lowerLimit;
    private DigitalInput upperLimit;

    private Arm() {
        queue = new TaskList();
        motor = new Motor(6);

        ControllerManager cm = ControllerManager.getInstance();
        Controller c = cm.getController(2);
        c.addJoystickListener(this);

        lowerLimit = new DigitalInput(1);
        upperLimit = new DigitalInput(2);

        start();
    }

    public static Arm getInstance() {
        if (instance == null) instance = new Arm();
        return instance;
    }

    public void lower() {
        lower(0, false);
    }

    public void lower(boolean blocking) {
        lower(0, blocking);
    }

    public void lower(long time) {
        lower(time, false);
    }

    public void lower(long time, boolean blocking) {
        Robot.addTask(new ArmTask(-6, time, blocking));
    }

    public void raise() {
        raise(0, false);
    }

    public void raise(boolean blocking) {
        raise(0, blocking);
    }

    public void raise(long time) {
        raise(time, false);
    }

    public void raise(long time, boolean blocking) {
        Robot.addTask(new ArmTask(.7, time, blocking));
    }

    public void reset() {
        queue.clear();
    }

    public void joystickMoved(JoystickEvent e) {
        double speed = (e.getThrottle() + 1.0) / 2.0;
        if (Robot.isTeleop()) motor.setX(speed * e.getY());
        System.out.println(speed + " * " + e.getY() + " = " + speed * e.getY());
    }

    public void throttleMoved(JoystickEvent e) {}

    public void execute(Task task) {
        queue.add(task);
    }

    public void run() {
        while (!interrupted()) {
            if (Robot.isAutonomous() && queue.hasTasks()) {
                ArmTask task = (ArmTask) queue.pop();
                task.status = Task.PROCESSING; // start doing task

                if (task.time > 0) { // move for a certain time
                    long start = System.currentTimeMillis();
                    long dt = 0;
                    motor.setX(task.speed);
                    if (task.speed > 0) { // going up!
                        while (!upperLimit.get() && dt < task.time) {
                            dt = System.currentTimeMillis() - start;
                            Robot.pause(10); // don't hog cpu
                        }
                    } else if (task.speed < 0) { // going down!
                        while (!lowerLimit.get() && dt < task.time) {
                            dt = System.currentTimeMillis() - start;
                            Robot.pause(10); // don't hog cpu
                        }
                    }
                } else { // move indefinitely
                    if (task.speed > 0) // going up!
                        while (!upperLimit.get()) Robot.pause(10);
                    else if (task.speed < 0) // going down!
                        while (!lowerLimit.get()) Robot.pause(10);

                }
                motor.setX(0); // stop motor
                task.status = Task.COMPLETED; // done!
            } else {
                Robot.pause(10); // don't hog cpu
            }
        }
    }
}
