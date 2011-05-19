/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.wvrobotics.logomotion;


import org.wvrobotics.logomotion.drive.Driver;
import org.wvrobotics.logomotion.arm.Arm;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.wvrobotics.logomotion.task.TaskManager;

/**
 * @author Vineel
 * 
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Main extends IterativeRobot {
    private Driver driver;
    private Arm arm;

    /**
     * This function is run when the robot is first started up.
     *
     * It does the following:
     *  - set the robot state
     *  - initialize the driver
     *  - initialize the arm
     *  - initialize the controllers
     */
    public void robotInit() {
        driver = Driver.getInstance();
        arm = Arm.getInstance();

        TaskManager taskManager = TaskManager.getInstance();
        taskManager.addSlave("Driver", driver);
        taskManager.addSlave("Arm", arm);
    }

    /**
     * This function is called at the beginning of autonomous.
     */
    public void autonomousInit() {
        driver.reset();
        arm.reset();
    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        //TODO: write autonomous code
    }

    /**
     * This function is called at the beginning of operator control.
     */
    public void teleopInit() {
        driver.reset();
        arm.reset();
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {}

    /**
     * This function is called immediately once disabled.
     */
    public void disabledInit() {
        arm.reset();
        driver.reset();
    }
}
