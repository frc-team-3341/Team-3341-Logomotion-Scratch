package org.wvrobotics.logomotion.drive;

import edu.wpi.first.wpilibj.RobotDrive;
import org.wvrobotics.logomotion.Robot;
import org.wvrobotics.logomotion.control.ButtonEvent;
import org.wvrobotics.logomotion.control.Controller;
import org.wvrobotics.logomotion.control.ButtonListener;
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
 */
public class Driver extends RobotThread
        implements ButtonListener, JoystickListener, Slave {
    private static volatile Driver instance;

    private volatile TaskList queue;

    private Motor frontRight;
    private Motor rearRight;
    private Motor frontLeft;
    private Motor rearLeft;
    private RobotDrive driveTrain;

    private int direction = 1;

    private Driver() {
        frontRight = new Motor(2);
        rearRight  = new Motor(3);
        frontLeft  = new Motor(4);
        rearLeft   = new Motor(5);
        driveTrain = new RobotDrive(frontLeft.controller, rearLeft.controller,
                frontRight.controller, rearRight.controller);

        ControllerManager cm = ControllerManager.getInstance();
        Controller c = cm.getController(1);
        c.addButtonListener(this);
        c.addJoystickListener(this);

        queue = new TaskList();

        start();
    }

    public static Driver getInstance() {
        if (instance == null) instance = new Driver();
        return instance;
    }

    public void drive(double speed, double curve) {
        drive(speed, curve, 0, false);
    }

    public void drive(double speed, double curve, long time) {
        drive(speed, curve, time, false);
    }

    public void drive(double speed, double curve, long time, boolean blocking) {
        Robot.addTask(new DriveTask(speed, curve, time, blocking));
    }

    // change state values to normal
    public void reset() {
        direction = 1;
        queue.clear();
    }

    public void buttonPressed(ButtonEvent e) {}

    public void buttonReleased(ButtonEvent e) {}

    public void buttonTyped(ButtonEvent e) {
        if (e.getButton() == 6) {
            direction *= -1; // change direction
        }
    }

    public void joystickMoved(JoystickEvent e) {
        if (Robot.isTeleop()) {
            driveTrain.drive(direction * e.getY(), direction * e.getX());
        }
    }

    public void throttleMoved(JoystickEvent e) {}

    public void execute(Task task) {
        queue.add(task);
    }

    public void run() {
        while (!interrupted()) {
            if (Robot.isAutonomous() && queue.hasTasks()) {
                DriveTask task = (DriveTask) queue.pop();
                task.status = Task.PROCESSING;
                if (task.time > 0) { // move for a certain time
                    driveTrain.drive(task.speed, task.curve);
                    Robot.pause(task.time);
                    driveTrain.drive(0, 0);
                } else { // move indefinitely
                    driveTrain.drive(task.speed, task.curve);
                }

                task.status = Task.COMPLETED;
            } else {
                // stop robot if disabled
                if (Robot.isDisabled()) driveTrain.drive(0, 0);
                Robot.pause(10); // don't hog cpu
            }
        }
    }
}
