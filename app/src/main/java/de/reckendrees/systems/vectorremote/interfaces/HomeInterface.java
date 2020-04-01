package de.reckendrees.systems.vectorremote.interfaces;

import com.chaquo.python.PyObject;

public interface HomeInterface {

    void updateCertStatus(Integer status);
    void updateConnectionStatus(Integer status, PyObject connection);
    void updateStats(String vector, String cube);
}
