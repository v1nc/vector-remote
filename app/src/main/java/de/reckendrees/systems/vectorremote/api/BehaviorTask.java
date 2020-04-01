package de.reckendrees.systems.vectorremote.api;

import android.os.AsyncTask;

import com.chaquo.python.PyObject;

public class BehaviorTask  extends AsyncTask<String, Integer, Integer> {

    private PyObject behavior;

    public BehaviorTask(PyObject behavior) {
        this.behavior = behavior;
    }

    protected Integer doInBackground(String... inputs) {
        if(inputs.length == 1){
            behavior.callAttr(inputs[0]);
        }else if (inputs.length == 2) {
            behavior.callAttr(inputs[0],inputs[1]);
        }
        return 1;
    }


}
