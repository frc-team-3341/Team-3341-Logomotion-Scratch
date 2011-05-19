package org.wvrobotics.logomotion.control;

import java.util.Hashtable;

/**
 *
 * @author Vineel
 */
public class ControllerManager {
    private static ControllerManager instance;

    private Hashtable controllers;

    private ControllerManager() {
        controllers = new Hashtable();
    }

    public static ControllerManager getInstance() {
        if (instance == null) instance = new ControllerManager();
        return instance;
    }

    public Controller getController(int port) {
        Integer key = new Integer(port);
        if (!controllers.containsKey(key))
            controllers.put(key, new Controller(port));
        return (Controller) controllers.get(key);
    }
}
