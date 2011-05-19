/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wvrobotics.logomotion.task;

/**
 *
 * @author Vineel
 */
public class PauseTask extends Task {
    public long time;

    public PauseTask(long time) {
        this.time = time;
        blocking = true; // this should be obvious
    }
    
    public String type() {
        return "Pause";
    }
}
