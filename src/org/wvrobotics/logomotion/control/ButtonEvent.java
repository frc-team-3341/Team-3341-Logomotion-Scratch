/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wvrobotics.logomotion.control;

/**
 *
 * @author Vineel
 */
public class ButtonEvent {
    private Controller source;
    private int button;
    private boolean state;
    private long time;

    public ButtonEvent(Controller source, int button, boolean state) {
        this.source = source;
        this.button = button;
        this.state = state;
        time = System.currentTimeMillis();
    }

    public Controller getSource() {
        return source;
    }

    public int getButton() {
        return button;
    }

    public boolean getState() {
        return state;
    }

    public long getTime() {
        return time;
    }
}
