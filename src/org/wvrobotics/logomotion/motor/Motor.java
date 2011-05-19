package org.wvrobotics.logomotion.motor;

import edu.wpi.first.wpilibj.CANJaguar;

/**
 *
 * @author Vineel
 */
public class Motor {
    public CANJaguar controller;

    public Motor(int port) {
        try {
            System.out.print("Initializing Jaguar on CAN " + port + "...");
            controller = new CANJaguar(port);
            System.out.println("Success");
        } catch (Exception e) {
            System.err.println("Error!!");
            controller = null;
        }
    }

    public void setX(double value) {
        try {
            controller.setX(value);
        } catch (Exception e) {}
    }
}
