package org.wvrobotics.logomotion.control;

/**
 *
 * @author Vineel
 */
public interface ButtonListener {
    public void buttonPressed(ButtonEvent e);
    public void buttonReleased(ButtonEvent e);
    public void buttonTyped(ButtonEvent e);
}
