package de.reckendrees.systems.vectorremote.api;

import android.os.AsyncTask;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import de.reckendrees.systems.vectorremote.interfaces.HomeInterface;

public class DownloadCertTask extends AsyncTask<String, Integer, Integer> {

    private HomeInterface home;

    public DownloadCertTask(HomeInterface home){

        super();
        this.home = home;
    }
    protected Integer doInBackground(String... inputs) {
        if(inputs.length != 5){
            return -1;
        }
        Python py =  Python.getInstance();
        PyObject wrapper = py.getModule("config_wrapper");
        try{
            wrapper.callAttr(
                    "generate_config",
                    inputs[0], inputs[1], inputs[2], inputs[3], inputs[4]
            );
            return 1;
        }catch(Exception e){
            return -2;
        }

    }
    protected void onPostExecute(Integer status){
       home.updateCertStatus(status);
    }

}

