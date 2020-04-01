package de.reckendrees.systems.vectorremote.api;

import android.os.AsyncTask;
import android.util.Log;

import com.chaquo.python.PyObject;

import de.reckendrees.systems.vectorremote.interfaces.HomeInterface;

public class StatsTask  extends AsyncTask<String, Integer, Integer> {

    private PyObject connection;
    private HomeInterface home;

    public StatsTask(PyObject connection, HomeInterface home) {
        this.connection = connection;
        this.home = home;
    }

    protected Integer doInBackground(String... inputs) {
        PyObject batteryStats = connection.callAttr("get_battery_state");
        String vector = batteryStats.get("battery_level").toString();
        String cube = batteryStats.get("cube_battery").get("level").toString();
        home.updateStats(vector,cube);
        return 1;
    }


}