package de.reckendrees.systems.vectorremote.interfaces;

import com.chaquo.python.PyObject;

public interface MainInterface {
    void setRobot(PyObject robot);
    PyObject getRobot();
    boolean isConnected();
}
